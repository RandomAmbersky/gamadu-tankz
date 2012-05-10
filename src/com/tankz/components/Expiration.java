package com.tankz.components;

import com.artemis.Component;

public class Expiration extends Component {
	private int lifeTime;
	private int initialLifeTime;
	
	public Expiration(int lifeTime) {
		this.initialLifeTime = this.lifeTime = lifeTime;
	}
	
	public int getLifeTime() {
		return lifeTime;
	}
	
	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}
	
	public float getLifeTimePercentage() {
		return (float)lifeTime / (float)initialLifeTime;
	}

	public void reduceLifeTime(int reduction) {
		lifeTime -= reduction;
		if(lifeTime < 0)
			lifeTime = 0;
	}

	public boolean isExpired() {
		return lifeTime <= 0;
	}
	
	
}
