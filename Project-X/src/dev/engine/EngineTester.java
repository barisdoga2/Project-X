package dev.engine;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.entities.Camera;
import dev.engine.entities.Player;
import dev.engine.loaders.ImageLoader;
import dev.engine.loaders.Loader;
import dev.engine.loaders.mapLoader.GameStructs;
import dev.engine.loaders.mapLoader.Map;
import dev.engine.loaders.objLoader.OBJLoader;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.renderEngine.MasterRenderer;
import dev.engine.terrains.Terrain;
import dev.engine.textures.ModelTexture;
import dev.engine.utils.Maths;
import dev.engine.utils.MousePicker;

public class EngineTester {

	private static Map testMap;

	public static void main(String[] args) {

		DisplayManager.CreateDisplay();

		// Map Loader Test //

		GameStructs.init();
		testMap = GameStructs.loadMap("testMap");
		Terrain testTerrain = testMap.getAllTerrains().get(0);

		Player player = new Player("Player",
				new TexturedModel(OBJLoader.LoadObjModel("/models/objs/player"),
						new ModelTexture(ImageLoader.loadTexture("/models/textures/playerTexture"))),
				new Vector3f(0, testTerrain.getHeight(0, 0), 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), testMap);
		player.setPosition(new Vector3f(819, 0, 647));

		Camera testCamera = new Camera(player);

		// --------------- //
		MousePicker mousePicker = new MousePicker(testCamera, Maths.createProjectionMatrix(), testMap.getAllTerrains());
		MasterRenderer masterRenderer = new MasterRenderer(testMap);
		
		
		while (!Display.isCloseRequested()) {
			player.update();
			testCamera.update();
			mousePicker.update();
			
			Vector3f vec = mousePicker.getCurrentTerrainPoint();
			if(vec != null)
				System.out.println(vec.x + " " + vec.y + " " + vec.z);
			
			masterRenderer.processEntity(player);
			masterRenderer.processEntities(testMap.getAllEntities());
			masterRenderer.processTerrains(testMap.getAllTerrains());
			masterRenderer.processWaters(testMap.getAllWaters());

			
			masterRenderer.renderWaterEffects(testCamera, testMap.getAllLights());
			masterRenderer.renderScene(testCamera, testMap.getAllLights());
			masterRenderer.renderWaters(testCamera, testMap.getAllLights());

			masterRenderer.cleanRenderObjects();
			DisplayManager.UpdateDisplay();
		}
		
		masterRenderer.cleanUp();
		ImageLoader.cleanUp();
		Loader.cleanUp();
		DisplayManager.DestroyDisplay();

	}

}
