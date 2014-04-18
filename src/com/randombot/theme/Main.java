package com.randombot.theme;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	
	public static void main(String[] args) {		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "skin packer";
		cfg.width = 1280;
		cfg.height = 800;

		new LwjglApplication(new HoloThemePacker(), cfg);
	}
	
}
