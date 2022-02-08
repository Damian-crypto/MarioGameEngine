package com.damian.game;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import com.damian.game.util.Time;

public class Window {
	
	private int width, height;
	private String title;
	private long glfwWindow;
	public float r, g, b, a;
	
	private static Window window = null;
	private static Scene currentScene;

	private Window() {
		this.width = 1024;
		this.height = 768;
		this.title = "Mario";
		this.r = 1.0f;
		this.g = 0.0f;
		this.b = 1.0f;
		this.a = 1.0f;
	}
	
	public static void changeScene(int scene) {
		switch (scene) {
		case 0:
			currentScene = new LevelEditorScene();
			currentScene.init();
			currentScene.start();
			break;
		case 1:
			currentScene = new LevelScene();
			currentScene.init();
			currentScene.start();
			break;
		default:
			assert false : "Unknown scene " + scene + "!";
		}
	}
	
	public static Window get() {
		if (window == null) {
			window = new Window();
		}
		
		return window;
	}
	
	public static Scene getScene() {
		return get().currentScene;
	}
	
	public void run() {
		System.out.println("Hello LWJGL, " + Version.getVersion() + "!");
		
		init();
		loop();
		
		// Free-up the memory
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		
		// Terminate GLFW and free the error callback - these are automatic for java
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void init() {
		// Setup error callback
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}
		
		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // don't visible until done creating the window
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		
		// Create the window
		glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
		if (glfwWindow == NULL) {
			throw new IllegalStateException("Failed to create the GLFW window.");
		}
		
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(glfwWindow);
		// Enable V-sync
		glfwSwapInterval(1); // swap for every single 1 frame(refresh monitor)
		glfwShowWindow(glfwWindow);
		
		/* This line is critical for LWJGL's interoperation with GLFW's
		 * OpenGL context, or any context that is managed externally.
		 * LWJGL's detects the context that is current in the current thread,
		 * create the GLCapabilities instance and makes the OpenGL
		 * bindings available for use.
		 */
		GL.createCapabilities();
		
		Window.changeScene(0);
	}
	
	public void loop() {
		float beginTime = Time.getTime();
		float endTime;
		float dt = -1.0f;
		
		while (!glfwWindowShouldClose(glfwWindow)) {
			// Poll events
			glfwPollEvents();
			
			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT);
			
			if (dt >= 0) {
				currentScene.update(dt);
			}
			
			glfwSwapBuffers(glfwWindow);

			endTime = Time.getTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
	}
}
