package com.tankz.systems.misc;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntityProcessingSystem;
import com.tankz.components.Ammo;

public class AmmoRegenerationSystem extends IntervalEntityProcessingSystem {

	private ComponentMapper<Ammo> ammoMapper;

	public AmmoRegenerationSystem() {
		super(Aspect.getAspectFor(Ammo.class), 100);
	}

	@Override
	public void initialize() {
		ammoMapper = world.getMapper(Ammo.class);
	}
	
	protected void process(Entity e) {
		Ammo ammo = ammoMapper.get(e);
		ammo.addAmmo(1);
	}
	
}
