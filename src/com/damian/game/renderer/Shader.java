package com.damian.game.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader {
	
	private int shaderProgramID;
	
	private String vertexSource;
	private String fragmentSource;
	private String filePath;
	
	private boolean beignUsed = false;
	private Map<String, Integer> uniformLocationCache = new HashMap<>();

	public Shader(String filePath) {
		this.filePath = filePath;
		
		try {
			String source = new String(Files.readAllBytes(Paths.get(filePath)));
			String[] sources = source.split("(#type)( )+([a-zA-Z]+)");
			
			int index = source.indexOf("#type") + "#type ".length();
			int eol = source.indexOf("\n", index);
			String firstPattern = source.substring(index, eol).trim();
			
			index = source.indexOf("#type", eol) + "#type ".length();
			eol = source.indexOf("\n", index);
			String secondPattern = source.substring(index, eol).trim();
			
			if (firstPattern.equals("vertex")) {
				vertexSource = sources[1];
			} else if (firstPattern.equals("fragment")) {
				
				fragmentSource = sources[1];
			} else {
				throw new IOException("Unexpected token: '" + firstPattern + "'");
			}
			
			if (secondPattern.equals("vertex")) {
				vertexSource = sources[2];
			} else if (secondPattern.equals("fragment")) {
				fragmentSource = sources[2];
			} else {
				throw new IOException("Unexpected token: '" + firstPattern + "'");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			assert false : "ERROR:: Couldn't open shader file: '" + filePath + "'";
		}
	}
	
	public void compile() {
		// ====================================================================
		// Compile and link shaders
		// ====================================================================
		
		int vertexID, fragmentID;
		
		// load vertex shader
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vertexSource); // pass the source to GPU
		glCompileShader(vertexID);
		
		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR::VERTEX SHADER:: vertex shader compilation error");
			System.out.println(glGetShaderInfoLog(vertexID, len));
		}
		
		// load fragment shader
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, fragmentSource); // pass the source to GPU
		glCompileShader(fragmentID);
		
		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR::FRAGMENT SHADER:: fragment shader compilation error");
			System.out.println(glGetShaderInfoLog(fragmentID, len));
		}
		
		// link shaders and check errors
		shaderProgramID = glCreateProgram();
		glAttachShader(shaderProgramID, vertexID);
		glAttachShader(shaderProgramID, fragmentID);
		glLinkProgram(shaderProgramID);
		
		success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR::PROGRAM LINKING:: vertex and fragment shader linking error");
			System.out.println(glGetProgramInfoLog(shaderProgramID, len));
		}
	}
	
	public void use() {
		if (!beignUsed) {
			// Bind shader program
			glUseProgram(shaderProgramID);
			beignUsed = true;
		}
	}
	
	public void detach() {
		// Bind shader program
		glUseProgram(0);
		beignUsed = false;
	}
	
	public int getUniformLocation(String uName) {
		int varLocation = -1;
		if (uniformLocationCache.containsKey(uName)) {
			varLocation = uniformLocationCache.get(uName);
		} else {
			varLocation = glGetUniformLocation(shaderProgramID, uName);
			uniformLocationCache.put(uName, varLocation);
		}
		
		return varLocation;
	}
	
	public void uploadMat4f(String uName, Matrix4f mat) {
		int varLocation = getUniformLocation(uName);
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat.get(matBuffer);
		use();
		glUniformMatrix4fv(varLocation, false, matBuffer);
	}
	
	public void uploadMat3f(String uName, Matrix4f mat) {
		int varLocation = getUniformLocation(uName);
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat.get(matBuffer);
		use();
		glUniformMatrix3fv(varLocation, false, matBuffer);
	}
	
	public void uploadVec4f(String uName, Vector4f vec) {
		int varLocation = getUniformLocation(uName);
		use();
		glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
	}
	
	public void uploadVec3f(String uName, Vector3f vec) {
		int varLocation = getUniformLocation(uName);
		use();
		glUniform3f(varLocation, vec.x, vec.y, vec.z);
	}
	
	public void uploadVec2f(String uName, Vector2f vec) {
		int varLocation = getUniformLocation(uName);
		use();
		glUniform2f(varLocation, vec.x, vec.y);
	}
	
	public void uploadFloat(String uName, float val) {
		int varLocation = getUniformLocation(uName);
		use();
		glUniform1f(varLocation, val);
	}
	
	public void uploadInt(String uName, int val) {
		int varLocation = getUniformLocation(uName);
		use();
		glUniform1i(varLocation, val);
	}
	
	public void uploadTexture(String uName, int slot) {
		int varLocation = getUniformLocation(uName);
		use();
		glUniform1i(varLocation, slot);
	}
}
