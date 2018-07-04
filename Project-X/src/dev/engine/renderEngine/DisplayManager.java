package dev.engine.renderEngine;

import java.lang.reflect.Method;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import dev.engine.EngineConfig;

public class DisplayManager {

	private static int fpsCap;
	private static long lasftFrameTime;
	private static float deltaTimeSeconds;

	public static void CreateDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

		EngineConfig config = EngineConfig.getInstance();

		int width = config.getInt("screen_width");
		int height = config.getInt("screen_height");
		String osName = System.getProperty( "os.name" ).toLowerCase();
		String className = null;
		String methodName = "getUsername";

		if( osName.contains( "windows" ) ){
		    className = "com.sun.security.auth.module.NTSystem";
		    methodName = "getName";
		}else if( osName.contains( "linux" ) ){
		    className = "com.sun.security.auth.module.UnixSystem";
		}else if( osName.contains( "solaris" ) || osName.contains( "sunos" ) ){
		    className = "com.sun.security.auth.module.SolarisSystem";
		}
		
		if(className != null) {
			try {
				Class<?> c = Class.forName( className );
			    Method method = c.getDeclaredMethod( methodName );
			    Object o = c.newInstance();
			    String systemUserName = method.invoke( o ).toString();
			    if(systemUserName.equals("Baris")) {
			    	width = 1920;
			    	height = 1080;
			    }
			}catch (Exception e) {}
		}
			

		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat().withSamples(config.getInt("aa_samples")), attribs);
			Display.setTitle(config.getString("title"));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		fpsCap = config.getInt("fps_cap");

		GL11.glViewport(0, 0, width, height);
		lasftFrameTime = getCurrentTime();
	}

	public static void UpdateDisplay() {
		Display.sync(fpsCap);
		Display.update();

		long currentFrameTime = getCurrentTime();
		deltaTimeSeconds = (currentFrameTime - lasftFrameTime) / 1000f;
		lasftFrameTime = currentFrameTime;
	}

	public static void DestroyDisplay() {
		Display.destroy();
	}

	public static boolean IsDestroyRequested() {
		return Display.isCloseRequested();
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	public static float getDeltaTimeSeconds() {
		return deltaTimeSeconds;
	}

}
