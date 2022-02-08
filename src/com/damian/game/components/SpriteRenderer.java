package com.damian.game.components;

import org.joml.Vector4f;

import com.damian.game.Component;

public class SpriteRenderer extends Component {
	
	private Vector4f color;
	
	public SpriteRenderer() {}
	
	public SpriteRenderer(Vector4f color) {
		this.color = color;
	}
	
	public Vector4f getColor() {
		return color;
	}
	
	@Override
	public void start() {
	}
	
	@Override
	public void update(float dt) {
	}
}
