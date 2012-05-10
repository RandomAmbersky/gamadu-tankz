package com.tankz.systems.misc;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.tankz.components.Physics;

public class BoundarySystem extends EntityProcessingSystem {
	
	private ComponentMapper<Physics> physicsMapper;
	private int boundsStartX;
	private int boundsStartY;
	private int boundsEndX;
	private int boundsEndY;

	public BoundarySystem(int boundsStartX, int boundsStartY, int boundsEndX, int boundsEndY) {
		super(Physics.class);

		this.boundsStartX = boundsStartX;
		this.boundsStartY = boundsStartY;
		this.boundsEndX = boundsEndX;
		this.boundsEndY = boundsEndY;
	}

	@Override
	public void initialize() {
		physicsMapper = new ComponentMapper<Physics>(Physics.class, world);
	}

	@Override
	protected void process(Entity e) {
		Physics physics = physicsMapper.get(e);
		
		if(physics.getX() < boundsStartX)
			physics.setLocation(boundsStartX, physics.getY());//physics.applyForce(world.getDelta() * 300, 0);
		else if(physics.getX() > boundsEndX)
			physics.setLocation(boundsEndX, physics.getY());//physics.applyForce(world.getDelta() * -300, 0);
		
		if(physics.getY() < boundsStartY)
			physics.setLocation(physics.getX(), boundsStartY);//physics.applyForce(0, world.getDelta() * 300);
		else if(physics.getY() > boundsEndY)
			physics.setLocation(physics.getX(), boundsEndY);//physics.applyForce(0, world.getDelta() * -300);

	}
	
	public int getBoundsEndX() {
		return boundsEndX;
	}
	
	public int getBoundsEndY() {
		return boundsEndY;
	}
	
	public int getBoundsStartX() {
		return boundsStartX;
	}
	
	public int getBoundsStartY() {
		return boundsStartY;
	}
	
	public int getBoundaryWidth() {
		return boundsEndX-boundsStartX;
	}
	
	public int getBoundaryHeight() {
		return boundsEndY-boundsStartY;
	}

}
