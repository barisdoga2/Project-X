package dev.engine.renderEngine.terrains;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.entities.Camera;
import dev.engine.entities.Light;
import dev.engine.renderEngine.ShaderProgram;
import dev.engine.utils.Maths;

public class TerrainShader extends ShaderProgram{
	
	private static final String vertexShaderFile = "src/dev/engine/renderEngine/terrains/terrainVertexShader.vs";
	private static final String fragmentShaderFile = "src/dev/engine/renderEngine/terrains/terrainFragmentShader.fs";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMapTexture;
	
	public TerrainShader() {
		super(vertexShaderFile, fragmentShaderFile);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_viewMatrix = super.getUniformLocation("viewMatrix");
		this.location_lightPosition = super.getUniformLocation("lightPosition");
		this.location_lightColor = super.getUniformLocation("lightColor");
		this.location_shineDamper = super.getUniformLocation("shineDamper");
		this.location_reflectivity = super.getUniformLocation("reflectivity");
		this.location_skyColor = super.getUniformLocation("skyColor");
		this.location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		this.location_rTexture = super.getUniformLocation("rTexture");
		this.location_gTexture = super.getUniformLocation("gTexture");
		this.location_bTexture = super.getUniformLocation("bTexture");
		this.location_blendMapTexture = super.getUniformLocation("blendMapTexture");
		
	}
	
	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix4f(this.location_transformationMatrix, transformationMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix4f(this.location_projectionMatrix, projectionMatrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix4f(this.location_viewMatrix, viewMatrix);
	}
	
	public void loadLight(Light light) {
		super.loadVector3f(location_lightPosition, light.getPosition());
		super.loadVector3f(location_lightColor, light.getColor());
	}
	
	public void loadShineValues(float shineDamper, float reflectivity) {
		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadSkyColor(Vector3f skyColor) {
		super.loadVector3f(location_skyColor, skyColor);
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMapTexture, 4);
	}

}