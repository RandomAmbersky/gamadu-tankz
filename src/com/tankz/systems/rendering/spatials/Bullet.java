package com.tankz.systems.rendering.spatials;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.artemis.ComponentType;
import com.artemis.ComponentTypeManager;
import com.artemis.Entity;
import com.artemis.World;
import com.tankz.components.Physics;
import com.tankz.components.Transform;

public class Bullet extends Spatial {
	private static Color color = new Color(51, 204, 69);
	private static ComponentType type = ComponentTypeManager.getTypeFor(Physics.class);

	private static Image img = null;
	static {
		try {
			img = new Image("bullet.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
	
	private Physics physics;

	public Bullet(World world, Entity owner) {
		super(world, owner);
	}

	@Override
	public void initalize() {
		physics = (Physics) owner.getComponent(type);
	}

	@Override
	public void render(Graphics g) {
		img.setRotation(physics.getRotation());
		img.draw(physics.getX()-img.getCenterOfRotationX(), physics.getY()-img.getCenterOfRotationY(), color);
	}

}
