package dev.engine.entities;

import org.lwjgl.util.vector.Vector3f;

import dev.engine.loaders.mapLoader.Map;
import dev.engine.models.TexturedModel;

public class Entity {

	private String name;
	protected Map currentMap;

	private TexturedModel model;

	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;

	private int textureAtlasIndex = 0;
	/*
	 * Atlas Indexing Example Atlases must be square. [0 , 1 , 2] [3 , 4 , 5] [6 , 7
	 * , 8]
	 */

	public Entity(String name, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.name = name;
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Entity(String name, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale,
			int textureAtlasIndex) {
		this.name = name;
		this.textureAtlasIndex = textureAtlasIndex;
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public void update() {

	}

	public float getTextureXOffset() {
		int column = textureAtlasIndex % model.getModelTexture().getAtlasNumberOfRows();
		return (float) column / (float) model.getModelTexture().getAtlasNumberOfRows();
	}

	public float getTextureYOffset() {
		int row = textureAtlasIndex / model.getModelTexture().getAtlasNumberOfRows();
		return (float) row / (float) model.getModelTexture().getAtlasNumberOfRows();
	}

	public void increasePosition(Vector3f position) {
		this.increasePosition(position.x, position.y, position.z);
	}

	public void increaseRotation(Vector3f rotation) {
		this.increasePosition(rotation.x, rotation.y, rotation.z);
	}

	public void increaseScale(Vector3f scale) {
		this.increaseScale(scale.x, scale.y, scale.z);
	}

	public void increasePosition(float dX, float dY, float dZ) {
		this.position.x += dX;
		this.position.y += dY;
		this.position.z += dZ;
	}

	public void increaseRotation(float rX, float rY, float rZ) {
		this.rotation.x += rX;
		this.rotation.y += rY;
		this.rotation.z += rZ;
	}

	public void increaseScale(float sX, float sY, float sZ) {
		this.scale.x += sX;
		this.scale.y += sY;
		this.scale.z += sZ;
	}

	public TexturedModel getTexturedModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public String getName() {
		return name;
	}
	
	public int getTextureAtlasIndex() {
		return textureAtlasIndex;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public void setName(String text) {
		this.name = text;
	}

}
