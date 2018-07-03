package dev.engine.loaders.mapLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dev.engine.loaders.ImageLoader;
import dev.engine.models.ModelTexture;

public class ModelTextureLoader {
	
	private static final String XML_PATH = "res/xml/modelTextures.xml";
	
	protected static List<ModelTexture> loadAll(){
		List<ModelTexture> all = new ArrayList<ModelTexture>();
		try{
			File fXmlFile = new File(XML_PATH);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();			
			NodeList nList = doc.getElementsByTagName("modelTexture");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					
					String name = eElement.getAttribute("name");
					String textureLocation = eElement.getElementsByTagName("textureLocation").item(0).getTextContent();
					boolean transparency = Boolean.parseBoolean(eElement.getElementsByTagName("transparency").item(0).getTextContent());
					boolean fakeLighting = Boolean.parseBoolean(eElement.getElementsByTagName("fakeLighting").item(0).getTextContent());
					float shineDamper = Float.parseFloat(eElement.getElementsByTagName("shineDamper").item(0).getTextContent());
					float reflectivity = Float.parseFloat(eElement.getElementsByTagName("reflectivity").item(0).getTextContent());
					int rowSize = Integer.parseInt(eElement.getElementsByTagName("rowSize").item(0).getTextContent());
					int textureID = ImageLoader.loadTexture(textureLocation);
					
					all.add(new ModelTexture(name, textureID, transparency, fakeLighting, shineDamper, reflectivity, rowSize));
					
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return all;
	}
	
}