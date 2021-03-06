package dev.engine.renderEngine.waters;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.entities.Camera;
import dev.engine.entities.Light;
import dev.engine.renderEngine.ShaderProgram;
import dev.engine.utils.Maths;

public class WaterShader extends ShaderProgram {
 
	private final static String VERTEX_FILE = "src/dev/engine/renderEngine/waters/waterVertexShader.glsl";
    private final static String FRAGMENT_FILE = "src/dev/engine/renderEngine/waters/waterFragmentShader.glsl";
    
	private static final int MAX_LIGHTS = 4;
 
    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dudvTexture;
    private int location_normalTexture;
    private int location_waterWaveMoveSpeed;
    private int location_waterWaveStrength;
    private int location_waterTilingFactor;
    private int location_cameraPosition;
    private int location_waterReflectivityFactor;
    private int location_lightPosition[];
    private int location_lightColor[];
    private int location_lightAttenuation[];
    private int location_depthTexture;
    private int location_densityOfFog;
    private int location_gradientOfFog;
    private int location_skyColor;
    private int location_waterShineDamper;
    private int location_waterReflectivity;
    private int location_nearPlane;
    private int location_farPlane;
    
    public WaterShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
    protected void bindAttributes() {
    	super.bindAttribute(0, "position");
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_modelMatrix = getUniformLocation("modelMatrix");
        location_reflectionTexture = getUniformLocation("reflectionTexture");
        location_refractionTexture = getUniformLocation("refractionTexture");
        location_dudvTexture = getUniformLocation("dudvTexture");
        location_normalTexture = getUniformLocation("normalTexture");
        location_depthTexture = getUniformLocation("depthTexture");
        location_waterWaveMoveSpeed = getUniformLocation("waterWaveMoveSpeed");
        location_waterWaveStrength = getUniformLocation("waterWaveStrength");
        location_waterTilingFactor = getUniformLocation("waterTilingFactor");
        location_cameraPosition = getUniformLocation("cameraPosition");
        location_waterReflectivityFactor = getUniformLocation("waterReflectivityFactor");
        location_densityOfFog = getUniformLocation("densityOfFog");
        location_gradientOfFog = getUniformLocation("gradientOfFog");
        location_skyColor = getUniformLocation("skyColor");
        location_waterShineDamper = getUniformLocation("waterShineDamper");
        location_waterReflectivity = getUniformLocation("waterReflectivity");
        location_nearPlane = getUniformLocation("nearPlane");
        location_farPlane = getUniformLocation("farPlane");
        
		this.location_lightPosition = new int[MAX_LIGHTS];
		this.location_lightColor = new int[MAX_LIGHTS];
		this.location_lightAttenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			this.location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			this.location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			this.location_lightAttenuation[i] = super.getUniformLocation("lightAttenuation[" + i + "]");
		}
    }
    
    @Override
	protected void connectTextureUnits() {
    	super.loadInt(location_reflectionTexture, 0);
    	super.loadInt(location_refractionTexture, 1);
    	super.loadInt(location_dudvTexture, 2);
    	super.loadInt(location_normalTexture, 3);
    	super.loadInt(location_depthTexture, 4);
	}
    
    public void loadNearAndFarPlane(float nearPlane, float farPlane) {
    	super.loadFloat(location_nearPlane, nearPlane);
    	super.loadFloat(location_farPlane, farPlane);
    }
    
	public void loadWaterShineValues(float shineDamper, float reflectivity) {
		super.loadFloat(location_waterShineDamper, shineDamper);
		super.loadFloat(location_waterReflectivity, reflectivity);
	}
    
    public void loadSkyColor(Vector3f skyColor) {
		super.loadVector3f(location_skyColor, skyColor);
	}
    
    public void loadFogVariables(float densityOfFog, float gradientOfFog) {
		super.loadFloat(location_densityOfFog, densityOfFog);
		super.loadFloat(location_gradientOfFog, gradientOfFog);
	}
    
    public void loadLights(List<Light> lights) {
    	for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector3f(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector3f(location_lightColor[i], lights.get(i).getColor());
				super.loadVector3f(location_lightAttenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector3f(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightAttenuation[i], new Vector3f(1, 0, 0));
			}
		}
    }
    
    public void loadWaterWaveMoveSpeed(float waterWaveMoveSpeed) {
    	super.loadFloat(location_waterWaveMoveSpeed, waterWaveMoveSpeed);
    }
    
    public void loadWaterVariables(float waterTilingFactor, float waterWaveStrength, float waterReflectivityFactor) {
    	super.loadFloat(location_waterTilingFactor, waterTilingFactor);
    	super.loadFloat(location_waterWaveStrength, waterWaveStrength);
    	super.loadFloat(location_waterReflectivityFactor, waterReflectivityFactor);
    }
 
    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
    	super.loadMatrix4f(location_projectionMatrix, projectionMatrix);
    }
     
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix4f(location_viewMatrix, viewMatrix);
        super.loadVector3f(location_cameraPosition, camera.getPosition());
    }
 
    public void loadModelMatrix(Matrix4f modelMatrix){
    	super.loadMatrix4f(location_modelMatrix, modelMatrix);
    }
 
}