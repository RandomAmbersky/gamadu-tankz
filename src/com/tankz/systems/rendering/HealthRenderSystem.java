package com.tankz.systems.rendering;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.tankz.components.Health;
import com.tankz.components.Physics;
import com.tankz.systems.camera.CameraSystem;

public class HealthRenderSystem extends EntityProcessingSystem {
	private GameContainer container;
	private Graphics g;
	private ComponentMapper<Health> healthMapper;
	private ComponentMapper<Physics> physicsMapper;

	private Image bar;
	private Color healthColor;
	private Color bgColor;
	private CameraSystem cameraSystem;

	public HealthRenderSystem(GameContainer container) {
		super(Health.class);

		this.container = container;
		this.g = container.getGraphics();
	}

	@Override
	public void initialize() {
		healthMapper = new ComponentMapper<Health>(Health.class, world);
		physicsMapper = new ComponentMapper<Physics>(Physics.class, world);

		healthColor = new Color(72f / 255f, 1f, 0f, 1f);
		bgColor = new Color(1f, 1f, 1f, 0.2f);

		try {
			bar = new Image("healthBar.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		cameraSystem = world.getSystemManager().getSystem(CameraSystem.class);
	}
	
	@Override
	protected void begin() {
		g.scale(cameraSystem.getZoom(), cameraSystem.getZoom());
		g.translate(-cameraSystem.getStartX(), -cameraSystem.getStartY());
	}

	@Override
	protected void process(Entity e) {
		Health health = healthMapper.get(e);
		Physics physics = physicsMapper.get(e);

		float xo = physics.getX()-25;
		float yo = physics.getY()-30;
		g.translate(xo,yo);
		{
			int healthIterations = Math.round(health.getHealthStatus()*10f);
			int i = 0;

			for (i = 0; healthIterations > i; i++) {
				bar.draw(i * 5, 0, healthColor);
			}
			for (; 10 > i; i++) {
				bar.draw(i * 5, 0, bgColor);
			}
		}
		g.translate(-xo,-yo);
	}
	
	@Override
	protected void end() {
		g.resetTransform();
	}

}