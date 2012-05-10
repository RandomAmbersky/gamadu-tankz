package com.tankz.systems.misc;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.tankz.components.Health;

public class HealthSystem extends EntityProcessingSystem {
	private ComponentMapper<Health> healthMapper;

	public HealthSystem() {
		super(Health.class);
	}

	@Override
	public void initialize() {
		healthMapper = new ComponentMapper<Health>(Health.class, world);
	}

	@Override
	protected void process(Entity e) {
		Health health = healthMapper.get(e);
		
		if(!health.isAlive()) {
			world.deleteEntity(e);
		}
		
	}


}
