package com.star.app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.star.app.StarGame;

public class DesktopLauncher {
	static public float x;
	static public float y;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		x = config.width;
		config.height = 720;
		y = config.height;
		new LwjglApplication(new StarGame(), config);

	}
}
