package com.damian.game;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

	private String name;
	private List<Component> components;
	public Transform transform;
	
	public GameObject(String name) {
		this.name = name;
		this.components = new ArrayList<>();
	}
	
	public GameObject(String name, Transform transform) {
		this.name = name;
		this.components = new ArrayList<>();
		this.transform = transform;
	}
	
	public <T extends Component> T getComponent(Class<T> componentClass) {
		for (Component comp : components) {
			if (componentClass.isAssignableFrom(comp.getClass())) {
				try {
					return componentClass.cast(comp);
				} catch (Exception ex) {
					ex.printStackTrace();
					assert false : "ERROR::COMPONENT CLASS CASTING";
				}
			}
		}
		
		return null;
	}
	
	public <T extends Component> void removeComponent(Class<T> componentClass) {
		for (int i = 0; i < components.size(); i++) {
			if (componentClass.isAssignableFrom(components.get(i).getClass())) {
				components.remove(i);
				return;
			}
		}
	}
	
	public void addComponent(Component comp) {
		components.add(comp);
		comp.gameObject = this;
	}
	
	public void update(float dt) {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).update(dt);
		}
	}
	
	public void start() {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).start();
		}
	}
}
