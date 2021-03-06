package dev.engine.waters;

public class WaterTile {
	
	public static final float WATER_HEIGHT = -1f;
	public static final float WATER_SCALE = 100f;
	
	private String name;
	
	private float XYZScale;
	private float midX;
	private float midZ;
	private float height;
	
	public WaterTile(float midX, float midZ){
		this.midX = midX;
		this.midZ = midZ;
		this.height = WATER_HEIGHT;
		this.XYZScale = WATER_SCALE;
	}
	
	public WaterTile(String name, float midX, float midZ){
		this.name = name;
		this.midX = midX;
		this.midZ = midZ;
		this.height = WATER_HEIGHT;
		this.XYZScale = WATER_SCALE;
	}
	
	public float getHeight() {
		return height;
	}

	public float getMidX() {
		return midX;
	}

	public float getMidZ() {
		return midZ;
	}
	
	public float getXYZScale(){
		return XYZScale;
	}
	
	public String getName(){
		return name;
	}
	
}
