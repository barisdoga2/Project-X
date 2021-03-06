package dev.engine.renderEngine.waters;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.EngineConfig;
import dev.engine.loaders.Loader;
import dev.engine.loaders.mapLoader.Map;
import dev.engine.models.RawModel;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.utils.Maths;
import dev.engine.waters.WaterFrameBuffer;
import dev.engine.waters.WaterTile;

public class WaterRenderer {
	
	private float waterWaveMoveFactor;
    private RawModel quad;
    private WaterShader shader;
    private Map map;
    private WaterFrameBuffer waterFrameBuffer;
 
    public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffer waterFrameBuffer, Map map) {
        this.shader = shader;
        this.waterFrameBuffer = waterFrameBuffer;
        this.map = map;
        EngineConfig configInstance = EngineConfig.getInstance();
        shader.start();
        shader.loadNearAndFarPlane(configInstance.getFloat("near_plane"), configInstance.getFloat("far_plane"));
        shader.loadWaterShineValues(map.getMapSettings().getWaterShineDamper(), map.getMapSettings().getWaterReflectivity());
        shader.loadSkyColor(map.getMapSettings().getSkyColor());
        shader.loadFogVariables(map.getMapSettings().getFogDensity(), map.getMapSettings().getFogGradient());
        shader.loadWaterVariables(map.getMapSettings().getWaterTilingFactor(), map.getMapSettings().getWaterWaveStrength(), map.getMapSettings().getWaterReflectivityFactor());
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO();
    }
    
    public void reLoadSettings() {
		boolean isRunning = shader.isRunning();
		if(!isRunning)
			shader.start();
		
		shader.loadWaterShineValues(map.getMapSettings().getWaterShineDamper(), map.getMapSettings().getWaterReflectivity());
        shader.loadSkyColor(map.getMapSettings().getSkyColor());
        shader.loadFogVariables(map.getMapSettings().getFogDensity(), map.getMapSettings().getFogGradient());
        shader.loadWaterVariables(map.getMapSettings().getWaterTilingFactor(), map.getMapSettings().getWaterWaveStrength(), map.getMapSettings().getWaterReflectivityFactor());
		
		if(!isRunning)
			shader.stop();
    }
 
    public void render(List<WaterTile> water) {
    	
    	waterWaveMoveFactor += map.getMapSettings().getWaterWaveMoveSpeed() * DisplayManager.getDeltaTimeSeconds();
    	waterWaveMoveFactor %= 1f;
    	
    	GL30.glBindVertexArray(quad.getVAOID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterFrameBuffer.getReflectionColorTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterFrameBuffer.getRefractionColorTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, map.getMapSettings().getWaterType().getDudvTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, map.getMapSettings().getWaterType().getNormalTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterFrameBuffer.getRefractionDepthTextureID());
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        shader.loadWaterWaveMoveSpeed(waterWaveMoveFactor);
        
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getMidX(), tile.getHeight(), tile.getMidZ()), new Vector3f(0, 0, 0), new Vector3f(tile.getXYZScale(), tile.getXYZScale(), tile.getXYZScale()));
            shader.loadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
 
    private void setUpVAO() {
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = Loader.loadToVAO(null, null, vertices, 2);
    }
 
}