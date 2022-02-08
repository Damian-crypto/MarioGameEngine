package com.damian.game;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
	
	private static MouseListener listener;
	private double scrollX, scrollY;
	private double xPos, yPos, lastX, lastY;
	private boolean mousePressed[] = new boolean[3];
	private boolean isDragging;
	
	private MouseListener() {
		this.scrollX = 0.0;
		this.scrollY = 0.0;
		this.xPos = 0.0;
		this.yPos = 0.0;
		this.lastX = 0.0;
		this.lastY = 0.0;
	}
	
	public static MouseListener get() {
		if (MouseListener.listener == null) {
			MouseListener.listener = new MouseListener();
		}
		
		return listener;
	}
	
	public static void mousePosCallback(long window, double xPos, double yPos) {
		get().lastX = get().xPos;
		get().lastY = get().yPos;
		get().xPos = xPos;
		get().yPos = yPos;
		get().isDragging = get().mousePressed[0] || get().mousePressed[1] || get().mousePressed[2];
	}
	
	public static void mouseButtonCallback(long window, int button, int action, int mods) {
		if (button < get().mousePressed.length) {
			if (action == GLFW_PRESS) {
					get().mousePressed[button] = true;							
			} else if (action == GLFW_RELEASE) {
				get().mousePressed[button] = false;
				get().isDragging = false;
			}
		}
	}
	
	public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
		get().scrollX = xOffset;
		get().scrollY = yOffset;
	}
	
	public static void endFrame() {
		get().scrollX = 0;
		get().scrollY = 0;
		get().lastX = get().xPos;
		get().lastY = get().yPos;
	}
	
	public static float getX() {
		return (float)get().xPos;
	}
	
	public static float getY() {
		return (float)get().yPos;
	}
	
	public static float getDx() {
		return (float)(get().lastX - get().xPos);
	}
	
	public static float getDy() {
		return (float)(get().lastY - get().yPos);
	}
	
	public static float getScrollX() {
		return (float)get().scrollX;
	}
	
	public static float getScrollY() {
		return (float)get().scrollY;
	}
	
	public static boolean isDragging() {
		return get().isDragging;
	}
	
	public static boolean mouseDown(int btn) {
		if (btn < get().mousePressed.length) {
			return get().mousePressed[btn];
		}
		
		return false;
	}

}
