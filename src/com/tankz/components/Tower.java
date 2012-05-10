package com.tankz.components;

import com.artemis.Component;

public class Tower extends Component {
	private float rotation;
	private float recoil;
	
	public Tower() {
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setRecoil(float recoil) {
		this.recoil = recoil;
	}

	public float getRecoil() {
		return recoil;
	}
	
	public void addRotation(float angle) {
		rotation = (rotation + angle) % 360;
	}

}
