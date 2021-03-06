package dev.engine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.EngineConfig;
import dev.engine.loaders.mapLoader.Map;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.terrains.Terrain;

public class Player extends Entity {

	private float mSpeed;
	private float mTurnSpeed;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	public Player(String name, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale, Map currentMap) {
		super(name, model, position, rotation, scale);

		EngineConfig config = EngineConfig.getInstance();
		this.mSpeed = config.getFloat("player_run_speed");
		this.mTurnSpeed = config.getFloat("player_turn_speed");
		this.currentMap = currentMap;
	}
  
	@Override
	public void update() {
		move();
	}

	private void move() {
		checkInputs();

		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getDeltaTimeSeconds(), 0);

		float distance = currentSpeed * DisplayManager.getDeltaTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(getRotation().y)));
		float dz = (float) (distance * Math.cos(Math.toRadians(getRotation().y)));
		super.increasePosition(dx, 0, dz);
		
		Terrain currentTerrain = null;
		
		for(Terrain terrain : currentMap.getAllTerrains()) 
			if(terrain.getX() / Terrain.SIZE == Math.ceil(getPosition().x / Terrain.SIZE) - 1 && terrain.getZ() / Terrain.SIZE == Math.ceil(getPosition().z / Terrain.SIZE) - 1) {
				currentTerrain = terrain;
				break;
			}

		if (currentTerrain != null)
			super.getPosition().y = currentTerrain.getHeight(getPosition().x, getPosition().z);
	}

	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = mSpeed;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -mSpeed;
		}else {
			this.currentSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = mTurnSpeed;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -mTurnSpeed;
		} else {
			this.currentTurnSpeed = 0;
		}
		
	}

}
