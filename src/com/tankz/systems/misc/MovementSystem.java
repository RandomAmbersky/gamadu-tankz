package com.tankz.systems.misc;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.TrigLUT;
import com.tankz.components.Transform;
import com.tankz.components.Velocity;

public class MovementSystem extends EntityProcessingSystem {

	public MovementSystem() {
		super(Aspect.getAspectFor(Transform.class, Velocity.class));
	}

	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Velocity> velocityMapper;

	@Override
	public void initialize() {
		transformMapper = world.getMapper(Transform.class);
		velocityMapper = world.getMapper(Velocity.class);
	}

	@Override
	protected void process(Entity e) {
		Transform t = transformMapper.get(e);
		Velocity velocity = velocityMapper.get(e);

		float r = t.getRotationAsRadians();
		float v = velocity.getVelocity();

		float xn = t.getX() + (TrigLUT.cos(r) * v * world.getDelta());
		float yn = t.getY() + (TrigLUT.sin(r) * v * world.getDelta());

		t.setLocation(xn, yn);
	}

}
