package com.damian.game;

public class LevelScene extends Scene {

	public LevelScene() {
		System.out.println("Inside LevelScene");
		Window.get().r = 1.0f;
		Window.get().g = 0.0f;
		Window.get().b = 1.0f;
	}

	@Override
	public void update(float dt) {
	}
}
