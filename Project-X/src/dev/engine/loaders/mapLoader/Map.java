package dev.engine.loaders.mapLoader;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import dev.engine.entities.Entity;
import dev.engine.entities.Light;
import dev.engine.terrains.Terrain;
import dev.engine.waters.WaterTile;

public class Map {

	private List<Entity> allEntities;
	private List<Terrain> allTerrains;
	private List<Light> allLights;
	private List<WaterTile> allWaters;

	private float tilingFactor;
	private float fogDensity;
	private float fogGradient;
	private Vector3f skyColor;
	private float minDiffuseLighting;
	private float minSpecularLighting;

	private float skyboxSize;
	private String skyBox[];
	
	private int waterDUDVTextureID;
	private int waterNormalTextureID;
	private float waterWaveMoveSpeed;
	private float waterWaveStrength;
	private float waterTilingFactor;
	private float waterReflectivityFactor;
	private float waterShineDamper;
	private float waterReflectivity;
	
	public Map() {
		allEntities = new ArrayList<Entity>();
		allTerrains = new ArrayList<Terrain>();
		allLights = new ArrayList<Light>();
		allWaters = new ArrayList<WaterTile>();
	}

	public void addEntities(List<Entity> e) {
		allEntities.addAll(e);
	}

	public void addEntity(Entity e) {
		allEntities.add(e);
	}

	public void addTerrains(List<Terrain> t) {
		allTerrains.addAll(t);
	}

	public void addTerrain(Terrain t) {
		allTerrains.add(t);
	}

	public void addLights(List<Light> l) {
		allLights.addAll(l);
	}

	public void addLight(Light l) {
		allLights.add(l);
	}
	
	public void addWaters(List<WaterTile> w) {
		allWaters.addAll(w);
	}

	public void addWater(WaterTile w) {
		allWaters.add(w);
	}
	
	public List<WaterTile> getAllWaters(){
		return allWaters;
	}

	public List<Entity> getAllEntities() {
		return allEntities;
	}

	public List<Terrain> getAllTerrains() {
		return allTerrains;
	}

	public List<Light> getAllLights() {
		return allLights;
	}

	public float getTilingFactor() {
		return tilingFactor;
	}

	public void setTilingFactor(float tilingFactor) {
		this.tilingFactor = tilingFactor;
	}

	public float getFogDensity() {
		return fogDensity;
	}

	public void setFogDensity(float fogDensity) {
		this.fogDensity = fogDensity;
	}

	public float getFogGradient() {
		return fogGradient;
	}

	public void setFogGradient(float fogGradient) {
		this.fogGradient = fogGradient;
	}

	public Vector3f getSkyColor() {
		return skyColor;
	}

	public void setSkyColor(Vector3f skyColor) {
		this.skyColor = skyColor;
	}

	public float getMinDiffuseLighting() {
		return minDiffuseLighting;
	}

	public void setMinDiffuseLighting(float minDiffuseLighting) {
		this.minDiffuseLighting = minDiffuseLighting;
	}

	public float getMinSpecularLighting() {
		return minSpecularLighting;
	}

	public void setMinSpecularLighting(float minSpecularLighting) {
		this.minSpecularLighting = minSpecularLighting;
	}
	
	public float getSkyboxSize() {
		return skyboxSize;
	}
	
	public void setSkyboxSize(float skyboxSize) {
		this.skyboxSize = skyboxSize;
	}

	public String[] getSkyBox() {
		return skyBox;
	}

	public void setSkyBox(String[] skyBox) {
		this.skyBox = skyBox;
	}

	public int getWaterDUDVTextureID() {
		return waterDUDVTextureID;
	}

	public void setWaterDUDVTextureID(int waterDUDVTextureID) {
		this.waterDUDVTextureID = waterDUDVTextureID;
	}

	public int getWaterNormalTextureID() {
		return waterNormalTextureID;
	}

	public void setWaterNormalTextureID(int waterNormalTextureID) {
		this.waterNormalTextureID = waterNormalTextureID;
	}

	public float getWaterWaveMoveSpeed() {
		return waterWaveMoveSpeed;
	}

	public void setWaterWaveMoveSpeed(float waterWaveMoveSpeed) {
		this.waterWaveMoveSpeed = waterWaveMoveSpeed;
	}

	public float getWaterWaveStrength() {
		return waterWaveStrength;
	}

	public void setWaterWaveStrength(float waterWaveStrength) {
		this.waterWaveStrength = waterWaveStrength;
	}

	public float getWaterTilingFactor() {
		return waterTilingFactor;
	}

	public void setWaterTilingFactor(float waterTilingFactor) {
		this.waterTilingFactor = waterTilingFactor;
	}

	public float getWaterReflectivityFactor() {
		return waterReflectivityFactor;
	}

	public void setWaterReflectivityFactor(float waterReflectivityFactor) {
		this.waterReflectivityFactor = waterReflectivityFactor;
	}

	public float getWaterShineDamper() {
		return waterShineDamper;
	}

	public void setWaterShineDamper(float waterShineDamper) {
		this.waterShineDamper = waterShineDamper;
	}

	public float getWaterReflectivity() {
		return waterReflectivity;
	}

	public void setWaterReflectivity(float waterReflectivity) {
		this.waterReflectivity = waterReflectivity;
	}
	
}
