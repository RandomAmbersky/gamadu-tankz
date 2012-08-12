package com.tankz.systems.rendering;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.tankz.components.SpatialForm;
import com.tankz.systems.camera.CameraSystem;
import com.tankz.systems.rendering.spatials.Bullet;
import com.tankz.systems.rendering.spatials.Crate;
import com.tankz.systems.rendering.spatials.Explosion;
import com.tankz.systems.rendering.spatials.MammothTank;
import com.tankz.systems.rendering.spatials.Spatial;
import com.tankz.systems.rendering.spatials.Wall;

public class RenderSystem extends EntityProcessingSystem {
	private Graphics graphics;
	private Bag<Spatial> spatials;
	private ComponentMapper<SpatialForm> spatialFormMapper;
	private CameraSystem cameraSystem;
	private GameContainer container;

	public RenderSystem(GameContainer container) {
		super(Aspect.getAspectFor(SpatialForm.class));
		this.container = container;
		this.graphics = container.getGraphics();
	}

	@Override
	public void initialize() {
		spatialFormMapper = world.getMapper(SpatialForm.class);

		cameraSystem = world.getSystem(CameraSystem.class);

		spatials = new Bag<Spatial>();
	}

	@Override
	protected void begin() {
		graphics.scale(cameraSystem.getZoom(), cameraSystem.getZoom());
		graphics.translate(-cameraSystem.getStartX(), -cameraSystem.getStartY());
	}

	@Override
	protected void process(Entity e) {
		Spatial spatial = spatials.get(e.getId());
		spatial.render(graphics);
	}

	@Override
	protected void end() {
		graphics.resetTransform();
	}

	@Override
	protected void added(Entity e) {
		Spatial spatial = getSpatial(e);
		if (spatial != null) {
			spatial.initalize();
			spatials.set(e.getId(), spatial);
		}
	}

	@Override
	protected void removed(Entity e) {
		spatials.set(e.getId(), null);
	}

	private Spatial getSpatial(Entity e) {
		SpatialForm spatialForm = spatialFormMapper.get(e);
		String spatialFormFile = spatialForm.getSpatialFormFile();
		if (spatialFormFile.equalsIgnoreCase("crate")) {
			return new Crate(world, e);
		} else if (spatialFormFile.equalsIgnoreCase("mammothTank")) {
			return new MammothTank(world, e);
		} else if (spatialFormFile.equalsIgnoreCase("bullet")) {
			return new Bullet(world, e);
		} else if (spatialFormFile.equalsIgnoreCase("explosion")) {
			return new Explosion(world, e);
		} else if (spatialFormFile.equalsIgnoreCase("wall")) {
			return new Wall(world, e);
		}
		return null;
	}
}
