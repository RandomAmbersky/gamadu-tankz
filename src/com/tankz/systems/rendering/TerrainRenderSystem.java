package com.tankz.systems.rendering;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.tankz.systems.camera.CameraSystem;

public class TerrainRenderSystem extends EntitySystem {
	private GameContainer container;
	private Graphics g;
	private Image tile;
	private CameraSystem cs;

	public TerrainRenderSystem(GameContainer container) {
		super(Aspect.getAspectFor());

		this.container = container;
		this.g = container.getGraphics();
	}

	@Override
	public void initialize() {
		try {
			tile = new Image("tile.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

		cs = world.getSystem(CameraSystem.class);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {

		float offsetX = cs.getStartX() % tile.getWidth();
		float offsetY = cs.getStartY() % tile.getHeight();
		
		int tilesWidth = (int)Math.ceil(cs.getWidth() / tile.getWidth())+1;
		int tilesHeight = (int)Math.ceil(cs.getHeight()/tile.getHeight())+1;

		g.scale(cs.getZoom(), cs.getZoom());
		
		g.translate(-offsetX, -offsetY);
		{
			for (int x = -1; tilesWidth > x; x++) {
				for (int y = -1; tilesHeight > y; y++) {
					tile.draw(x * tile.getWidth(), y * tile.getHeight());
				}
			}
		}
		g.resetTransform();

	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
