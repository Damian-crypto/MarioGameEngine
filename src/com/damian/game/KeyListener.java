package com.damian.game;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {

	private static KeyListener listener;
	private boolean keyPressed[] = new boolean[350]; // GLFW has 350 key bindings something
	
	private KeyListener() {}
	
	public static KeyListener get() {
		if (listener == null) {
			listener = new KeyListener();
		}
		
		return listener;
	}
	
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			get().keyPressed[key] = true;
		} else if (action == GLFW_RELEASE) {
			get().keyPressed[key] = false;
		}
	}
	
	public static boolean isKeyPressed(int keyCode) {
		return get().keyPressed[keyCode];			
	}
}
