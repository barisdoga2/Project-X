package dev.engine.entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {

	private String name;
	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation;

	public Light(String name, Vector3f position, Vector3f color, Vector3f attenuation) {
		this.name = name;
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}

	public void update() {

	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}

	public String getName() {
		return name;
	}

}
