package com.tankz.systems.misc;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.tankz.components.Health;

public class HealthSystem extends EntityProcessingSystem {
	private ComponentMapper<Health> healthMapper;

	public HealthSystem() {
		super(Aspect.getAspectFor(Health.class));
	}

	@Override
	public void initialize() {
		healthMapper = world.getMapper(Health.class);
	}

	@Override
	protected void process(Entity e) {
		Health health = healthMapper.get(e);
		
		if(!health.isAlive()) {
			world.deleteEntity(e);
		}
		
	}


}
