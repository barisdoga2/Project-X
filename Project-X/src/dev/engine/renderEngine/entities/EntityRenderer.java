package dev.engine.renderEngine.entities;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import dev.engine.entities.Entity;
import dev.engine.models.RawModel;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.MasterRenderer;
import dev.engine.textures.ModelTexture;
import dev.engine.utils.Maths;

public class EntityRenderer {

	private EntityShader entityShader;
	private dev.engine.loaders.mapLoader.Map map;

	public EntityRenderer(EntityShader entityShader, Matrix4f projectionMatrix, dev.engine.loaders.mapLoader.Map map) {
		this.entityShader = entityShader;
		this.map = map;

		entityShader.start();
		entityShader.loadFogVariables(map.getMapSettings().getFogDensity(), map.getMapSettings().getFogGradient());
		entityShader.loadMinLightingVariables(map.getMapSettings().getMinDiffuseLighting(), map.getMapSettings().getMinSpecularLighting());
		entityShader.loadSkyColor(map.getMapSettings().getSkyColor());

		entityShader.loadProjectionMatrix(projectionMatrix);
		entityShader.stop();
	}
	
	public void reLoadSettings() {
		boolean isRunning = entityShader.isRunning();
		if(!isRunning)
			entityShader.start();
		
		entityShader.loadFogVariables(map.getMapSettings().getFogDensity(), map.getMapSettings().getFogGradient());
		entityShader.loadMinLightingVariables(map.getMapSettings().getMinDiffuseLighting(), map.getMapSettings().getMinSpecularLighting());
		entityShader.loadSkyColor(map.getMapSettings().getSkyColor());
		
		if(!isRunning)
			entityShader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel texturedModel : entities.keySet()) {
			prepareTexturedModel(texturedModel);
			List<Entity> batch = entities.get(texturedModel);
			for (Entity entity : batch) {
				prepareEntity(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel(TexturedModel texturedModel) {
		RawModel rawModel = texturedModel.getRawModel();
		ModelTexture modelTexture = texturedModel.getModelTexture();
		entityShader.loadAtlasNumberOfRows(modelTexture.getAtlasNumberOfRows());

		GL30.glBindVertexArray(rawModel.getVAOID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		entityShader.loadShineValues(modelTexture.getShineDamper(), modelTexture.getReflectivity());
		entityShader.loadUseFakeLighting(modelTexture.isHasFakeLighting());
		if (modelTexture.isHasTransparency())
			MasterRenderer.DisableCulling();

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelTexture.getBaseTexture().getTextureID());
		
		if(texturedModel.getSpecularMappingTexture() != null) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getSpecularMappingTexture().getTextureID());
			entityShader.loadUseSpecularMapping(true);
		}else {
			entityShader.loadUseSpecularMapping(false);
		}
	}

	private void prepareEntity(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(),
				entity.getScale());
		entityShader.loadTransformationMatrix(transformationMatrix);

		entityShader.loadAtlasOffsets(entity.getTextureXOffset(), entity.getTextureYOffset());
	}

	private void unbindTexturedModel() {
		MasterRenderer.EnableCulling();
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
