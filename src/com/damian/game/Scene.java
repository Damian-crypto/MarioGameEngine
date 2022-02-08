package com.damian.game;

import java.util.ArrayList;
import java.util.List;

import com.damian.game.renderer.Renderer;

public abstract class Scene {
	
	protected Renderer renderer = new Renderer();
	protected Camera camera;
	protected List<GameObject> gameObjects = new ArrayList<>();
	private boolean isRunning = false;

	public Scene() {}
	
	public void init() {}
	
	public void start() {
		for (GameObject obj : gameObjects) {
			obj.start();
			renderer.add(obj);
		}
		
		isRunning = true;
	}
	
	public void addGameObjectToScene(GameObject obj) {
		if (isRunning) {
			gameObjects.add(obj);
			obj.start();
			renderer.add(obj);
		} else {
			gameObjects.add(obj);
		}
	}
	
	public Camera camera() {
		return camera;
	}
	
	public abstract void update(float dt);
}
