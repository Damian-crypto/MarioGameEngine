package com.damian.game.renderer;

import java.util.ArrayList;
import java.util.List;

import com.damian.game.GameObject;
import com.damian.game.components.SpriteRenderer;

public class Renderer {
	
	private final int MAX_BATCH_SIZE = 1000;
	private List<RenderBatch> batches;

	public Renderer() {
		batches = new ArrayList<>();
	}
	
	public void add(GameObject obj) {
		SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);
		if (sprite != null) {
			add(sprite);
		}
	}
	
	public void render() {
		for (RenderBatch batch : batches) {
			batch.render();
		}
	}
	
	private void add(SpriteRenderer sprite) {
		boolean added = false;
		for (RenderBatch batch : batches) {
			if (batch.hasRoom()) {
				batch.addSprite(sprite);
				added = true;
				break;
			}
		}
		
		if (!added) {
			RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
			newBatch.start();
			batches.add(newBatch);
			newBatch.addSprite(sprite);
		}
	}
}
