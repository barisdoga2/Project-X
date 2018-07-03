package dev.engine.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.EngineConfig;
import dev.engine.entities.Camera;
import dev.engine.entities.Entity;
import dev.engine.entities.Light;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.entities.EntityRenderer;
import dev.engine.renderEngine.entities.EntityShader;
import dev.engine.renderEngine.terrains.TerrainRenderer;
import dev.engine.renderEngine.terrains.TerrainShader;
import dev.engine.terrains.Terrain;
import dev.engine.utils.Maths;

public class MasterRenderer {

	private Matrix4f projectionMatrix;

	private EntityShader entityShader;
	private EntityRenderer entityRenderer;
	private Map<TexturedModel, List<Entity>> allEntities = new HashMap<TexturedModel, List<Entity>>();

	private TerrainShader terrainShader;
	private TerrainRenderer terrainRenderer;
	private List<Terrain> allTerrains = new ArrayList<Terrain>();

	private Vector3f skyColor;

	public MasterRenderer(dev.engine.loaders.mapLoader.Map map) {
		MasterRenderer.EnableCulling();
		this.projectionMatrix = Maths.createProjectionMatrix();

		this.entityShader = new EntityShader();
		this.entityRenderer = new EntityRenderer(entityShader, projectionMatrix, map);

		this.terrainShader = new TerrainShader();
		this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix, map);

		EngineConfig config = EngineConfig.getInstance();
		skyColor = config.getVector3f("sky_color");
	}

	public void render(List<Light> lights, Camera camera) {
		prepare();

		// Rendering Terrains
		terrainShader.start();
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);

		terrainRenderer.render(allTerrains);

		terrainShader.stop();
		allTerrains.clear();

		// Rendering Entities
		entityShader.start();
		entityShader.loadLights(lights);
		entityShader.loadViewMatrix(camera);

		entityRenderer.render(allEntities);

		entityShader.stop();
		allEntities.clear();
	}

	public void processEntities(List<Entity> entities) {
		for (Entity entity : entities)
			processEntity(entity);
	}

	public void processEntity(Entity entity) {
		TexturedModel texturedModel = entity.getTexturedModel();
		List<Entity> batch = this.allEntities.get(texturedModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			this.allEntities.put(texturedModel, newBatch);
		}
	}

	public void processTerrains(List<Terrain> terrains) {
		for (Terrain terrain : terrains)
			processTerrain(terrain);
	}

	public void processTerrain(Terrain terrain) {
		this.allTerrains.add(terrain);
	}

	public static void EnableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void DisableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
	}

	public void cleanUp() {
		entityShader.cleanUp();
	}

}
