package com.tankz.systems.rendering.spatials;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.tankz.components.Expiration;
import com.tankz.components.Transform;

public class Explosion extends Spatial {

	private Transform transform;
	private float radius;
	private Color color;
	private Expiration expiraton;

	public Explosion(World world, Entity owner) {
		super(world, owner);
	}

	@Override
	public void initalize() {
		ComponentMapper<Expiration> expirationMapper = new ComponentMapper<Expiration>(Expiration.class, world);
		expiraton = expirationMapper.get(owner);

		ComponentMapper<Transform> transformMapper = new ComponentMapper<Transform>(Transform.class, world);
		transform = transformMapper.get(owner);
		radius = 5;
		color = new Color(Color.yellow);
	}

	@Override
	public void render(Graphics g) {
		radius += world.getDelta() * 0.1f;
		
		color.a = expiraton.getLifeTimePercentage();
		
		g.setColor(color);
		g.setAntiAlias(true);
		g.fillOval(transform.getX()-radius, transform.getY()-radius, radius*2, radius*2);
	}

}
