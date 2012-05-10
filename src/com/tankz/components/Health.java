package com.tankz.components;

import com.artemis.Component;

public class Health extends Component {
	private float health;
	private float maximumHealth;
	
	public Health(float health) {
		this(health,health);
	}
	
	public Health(float health, float maximumHealth) {
		this.health = Math.min(health, maximumHealth);
		this.maximumHealth = maximumHealth;
	}
	
	public float getHealth() {
		return health;
	}
	
	public float getMaximumHealth() {
		return maximumHealth;
	}
	
	public float getHealthStatus() {
		return health/maximumHealth;
	}

	public void addDamage(float damage) {
		health -= damage;
		if(health < 0)
			health = 0;
	}
	
	public boolean isAlive() {
		return health > 0;
	}

}
