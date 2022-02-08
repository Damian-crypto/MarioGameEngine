package com.damian.game;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

	private Matrix4f projectionMatrix, viewMatrix;
	public Vector2f position;
	
	public Camera(Vector2f position) {
		this.position = position;
		this.projectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		adjustProjection();
	}
	
	public void adjustProjection() {
		projectionMatrix.identity();
		projectionMatrix.ortho(0, 32 * 40.0f, 0, 32 * 21.0f, 0, 100.0f);
	}
	
	public Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f); // camera is seeing inner part of the box -1 = z
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		viewMatrix.identity();
		// lookAt(where camera in world space, what direction is looking at, hanger of the camera)
		viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f), cameraFront.add(position.x, position.y, 0.0f), cameraUp);
		return viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
