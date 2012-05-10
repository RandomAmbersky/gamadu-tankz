package com.tankz.systems.rendering.spatials;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.artemis.Entity;
import com.artemis.World;
import com.tankz.components.Physics;

public class Wall extends Spatial {
	private static Image img = null;

	static {
		try {
			img = new Image("wall.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
	private Physics physics;

	public Wall(World world, Entity owner) {
		super(world, owner);
	}

	@Override
	public void initalize() {
		physics = owner.getComponent(Physics.class);
	}

	@Override
	public void render(Graphics g) {
		img.setRotation(physics.getRotation());
		img.draw(physics.getX()-img.getCenterOfRotationX(), physics.getY()-img.getCenterOfRotationY());
	}

}
