package dev.engine.loaders.mapLoader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dev.engine.entities.Entity;
import dev.engine.entities.Light;
import dev.engine.loaders.ImageLoader;
import dev.engine.models.TexturedModel;
import dev.engine.terrains.Terrain;
import dev.engine.terrains.TerrainTexture;
import dev.engine.terrains.TerrainTexturePack;

public class MapLoader {
	
	private static Document doc;
	
	public static Map loadAll(String mapName){
		Map map = new Map();
		
		File fXmlFile = new File("res/" + "xml/" + mapName + "/" + mapName + ".xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
			return map;
		}
		
		loadMapSettings(map);
		map.addTerrains(loadTerrains(mapName));
		map.addEntities(loadEntities(mapName));
		map.addLights(loadLights(mapName));
		
		return map;
	}
	
	private static void loadMapSettings(Map map) {		
		Node nNode = doc.getElementsByTagName("settings").item(0);
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			
			String tilingFactor = eElement.getElementsByTagName("tilingFactor").item(0).getTextContent();
			String fogDensity = eElement.getElementsByTagName("fogDensity").item(0).getTextContent();
			String fogGradient = eElement.getElementsByTagName("fogGradient").item(0).getTextContent();
			String[] skyColorTokens = eElement.getElementsByTagName("skyColor").item(0).getTextContent().split(",");
			String minDiffuseLighting = eElement.getElementsByTagName("minDiffuseLighting").item(0).getTextContent();
			String minSpecularLighting = eElement.getElementsByTagName("minSpecularLighting").item(0).getTextContent();
			

			map.setTilingFactor(Float.parseFloat(tilingFactor));
			map.setFogDensity(Float.parseFloat(fogDensity));
			map.setFogGradient(Float.parseFloat(fogGradient));
			map.setSkyColor(new Vector3f(Float.parseFloat(skyColorTokens[0]), Float.parseFloat(skyColorTokens[1]), Float.parseFloat(skyColorTokens[2])));
			map.setMinDiffuseLighting(Float.parseFloat(minDiffuseLighting));
			map.setMinSpecularLighting(Float.parseFloat(minSpecularLighting));
		}
	}
	
	private static List<Entity> loadEntities(String path) {
		
		List<Entity> entities = new ArrayList<Entity>();
		
		NodeList nList = doc.getElementsByTagName("entity");
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				
				String name = eElement.getAttribute("name");
				TexturedModel texturedModel = GameStructs.getTexturedModel(eElement.getElementsByTagName("texturedModel").item(0).getTextContent());
				int textureAtlasIndex = Integer.parseInt(eElement.getElementsByTagName("textureAtlasIndex").item(0).getTextContent());
				
				boolean xmlValid = true;
				List<Vector3f> positions = new ArrayList<Vector3f>();
				List<Vector3f> rotations = new ArrayList<Vector3f>();
				List<Vector3f> scales = new ArrayList<Vector3f>();
				String[] tokens;
				
				String position = eElement.getElementsByTagName("position").item(0).getTextContent();
				String rotation = eElement.getElementsByTagName("rotation").item(0).getTextContent();
				String scale = eElement.getElementsByTagName("scale").item(0).getTextContent();
				
				tokens = position.split(",");
				if(tokens.length % 3f == 0)
					createAndStoreVector3f(positions, tokens);
				else{
					System.err.println("In " + path + " xml file, entity " + name + " has wrong postion values.");
					xmlValid = false;
				}
				
				tokens = rotation.split(",");
				if(tokens.length % 3f == 0)
					createAndStoreVector3f(rotations, tokens);
				else{
					System.err.println("In " + path + " xml file, entity " + name + " has wrong rotation values.");
					xmlValid = false;
				}
				
				tokens = scale.split(",");
				if(tokens.length % 3f == 0)
					createAndStoreVector3f(scales, tokens);
				else{
					System.err.println("In " + path + " xml file, entity " + name + " has wrong scale values.");
					xmlValid = false;
				}
				
				if(xmlValid){
					if(positions.size() == rotations.size() && rotations.size() == scales.size()){
						
						for(int i = 0 ; i < positions.size() ; i++)
							entities.add(new Entity(name, texturedModel, positions.get(i), rotations.get(i), scales.get(i), textureAtlasIndex));
						
					}else{
						System.err.println("In " + path + " xml file, entity " + name + " has " + positions.size() + " entity positions" + ", " + rotations.size() + " entity rotations" + ", " + scales.size() + " entity scales.");
					}
				}
				
			}
		}
	
		return entities;
	}
	
	private static List<Terrain> loadTerrains(String path){
		List<Terrain> terrains = new ArrayList<Terrain>();

		NodeList nList = doc.getElementsByTagName("terrain");
			
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				
				String name = eElement.getAttribute("name");
				int Xgrid = Integer.parseInt(eElement.getElementsByTagName("Xgrid").item(0).getTextContent());
				int Zgrid = Integer.parseInt(eElement.getElementsByTagName("Zgrid").item(0).getTextContent());
				float tilingFactor = Float.parseFloat(eElement.getElementsByTagName("tilingFactor").item(0).getTextContent());
				
				TerrainTexture backgroundTexture = GameStructs.getTerrainTexture(eElement.getElementsByTagName("backgroundTexture").item(0).getTextContent());
				TerrainTexture rTexture = GameStructs.getTerrainTexture(eElement.getElementsByTagName("rTexture").item(0).getTextContent());
				TerrainTexture gTexture = GameStructs.getTerrainTexture(eElement.getElementsByTagName("gTexture").item(0).getTextContent());
				TerrainTexture bTexture = GameStructs.getTerrainTexture(eElement.getElementsByTagName("bTexture").item(0).getTextContent());
				TerrainTexture aTexture = GameStructs.getTerrainTexture(eElement.getElementsByTagName("aTexture").item(0).getTextContent());
				
				TerrainTexture blendMapTexture = new TerrainTexture(name, ImageLoader.loadTexture(eElement.getElementsByTagName("blendMapLocation").item(0).getTextContent()));
				BufferedImage heightMap = ImageLoader.loadBufferedImage(eElement.getElementsByTagName("heightMapLocation").item(0).getTextContent());
				
				terrains.add(new Terrain(name, Xgrid, Zgrid, tilingFactor, new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture, aTexture), blendMapTexture, heightMap));
			}
		}
		
		return terrains;
	}
	
	private static List<Light> loadLights(String path){
		List<Light> lights = new ArrayList<Light>();
		
		NodeList nList = doc.getElementsByTagName("light");
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String[] tokens;
				
				String name = eElement.getAttribute("name");
				
				tokens = eElement.getElementsByTagName("position").item(0).getTextContent().split(",");
				Vector3f position = new Vector3f(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
				
				tokens = eElement.getElementsByTagName("color").item(0).getTextContent().split(",");
				Vector3f color = new Vector3f(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
				
				tokens = eElement.getElementsByTagName("attenuation").item(0).getTextContent().split(",");
				Vector3f attenuation = new Vector3f(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
			
				lights.add(new Light(name, position, color, attenuation));
			}
		}
		
		return lights;
	}
	
	private static void createAndStoreVector3f(List<Vector3f> toAdd, String[] tokens){
		for(int i = 0 ; i < tokens.length / 3 ; i++)
			toAdd.add(new Vector3f(Float.parseFloat(tokens[i * 3 + 0]), Float.parseFloat(tokens[i * 3 + 1]), Float.parseFloat(tokens[i * 3 + 2])));
	}
	
}