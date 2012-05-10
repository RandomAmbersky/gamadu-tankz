package com.tankz.systems.misc;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.IntervalEntityProcessingSystem;
import com.tankz.components.Ammo;

public class AmmoRegenerationSystem extends IntervalEntityProcessingSystem {

	private ComponentMapper<Ammo> ammoMapper;

	public AmmoRegenerationSystem() {
		super(100, Ammo.class);
	}

	@Override
	public void initialize() {
		ammoMapper = new ComponentMapper<Ammo>(Ammo.class, world);
	}
	
	protected void process(Entity e) {
		Ammo ammo = ammoMapper.get(e);
		ammo.addAmmo(1);
	}
	
}
