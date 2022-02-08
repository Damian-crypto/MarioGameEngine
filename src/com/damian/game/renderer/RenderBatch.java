package com.damian.game.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector4f;

import com.damian.game.Window;
import com.damian.game.components.SpriteRenderer;

public class RenderBatch {

	/**
	 * Vertex
	 * ====================================
	 * position		color
	 * float float	float float float float
	 */
	private final int POS_SIZE = 2;
	private final int COLOR_SIZE = 4;
	private final int POS_OFFSET = 0;
	
	private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
	private final int VERTEX_SIZE = 6;
	private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
	
	private SpriteRenderer[] sprites;
	private int numberOfSprites;
	private boolean hasRoom;
	private float[] vertices;
	
	private int vaoID, vboID;
	private int maxBatchSize;
	private Shader shader;
	
	public RenderBatch(int maxBatchSize) {
		this.shader = new Shader("assets/shaders/default.glsl");
		shader.compile();
		this.maxBatchSize = maxBatchSize;
		this.sprites = new SpriteRenderer[maxBatchSize];
		
		// 4 vertices quad(s)
		this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
		
		this.numberOfSprites = 0;
		this.hasRoom = true;
	}
	
	public void start() {
		// Generate and bind a Vertex array object
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		// Allocate space for vertices
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
		
		// Create and upload indices buffer
		int eboID = glGenBuffers();
		int[] indices = generateIndices();
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		// Enable the buffer attribute pointers
		glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
		glEnableVertexAttribArray(1);
	}
	
	public void addSprite(SpriteRenderer sprite) {
		// Get index and add RenderObject
		int idx = numberOfSprites;
		sprites[idx] = sprite;
		numberOfSprites++;
		
		// Add properties to local vertices array
		loadVertexProperties(idx);
		
		if (numberOfSprites >= maxBatchSize) {
			hasRoom = false;
		}
	}
	
	public void render() {
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices); // re-buffering everything for each render
	
		shader.use();
		shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
		shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());
		
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, numberOfSprites * 6, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
		shader.detach();
	}
	
	private void loadVertexProperties(int idx) {
		SpriteRenderer sprite = sprites[idx];
		
		// Find offset within array (4 vertices per sprite)
		int offset = idx * 4 * VERTEX_SIZE;
		
		Vector4f color = sprite.getColor();
		
		// Add vertices with the appropriate properties
		/*
		 * 4  1
		 * 3  2
		 */
		float xAdd = 1.0f;
		float yAdd = 1.0f;
		for (int i = 0; i < 4; i++) {
			switch (i) {
			case 1 -> yAdd = 0.0f;
			case 2 -> xAdd = 0.0f;
			case 3 -> yAdd = 1.0f;
			}
			
			// Load position
			vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
			vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);
			
			// Load color
			vertices[offset + 2] = color.x; // r
			vertices[offset + 3] = color.y; // g
			vertices[offset + 4] = color.z; // b
			vertices[offset + 5] = color.w; // a
			
			offset += VERTEX_SIZE;
		}
	}
	
	public boolean hasRoom() {
		return hasRoom;
	}
	
	private int[] generateIndices() {
		int[] elements = new int[6 * maxBatchSize];
		
		for (int i = 0; i < maxBatchSize; i++) {
			loadElementIndices(elements, i);
		}
		
		return elements;
	}
	
	/**
	 * Don't have a clear idea
	 * 
	 * @param elements
	 * @param index
	 */
	private void loadElementIndices(int[] elements, int index) {
		int offsetArrayIndex = 6 * index; // only for the first element of the each elements array: if index = 0 then, 6 * 0 = 0 and 6 * 1 = 6
		int offset = 4 * index;
		
		// Triangle 1
		elements[offsetArrayIndex] = offset + 3;
		elements[offsetArrayIndex + 1] = offset + 2;
		elements[offsetArrayIndex + 2] = offset + 3;
		
		// Triangle 2
		elements[offsetArrayIndex + 3] = offset + 0;
		elements[offsetArrayIndex + 4] = offset + 2;
		elements[offsetArrayIndex + 5] = offset + 1;
	}
}
