package com.tankz.systems.rendering;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.tankz.components.Ammo;
import com.tankz.components.Health;
import com.tankz.components.Physics;
import com.tankz.systems.camera.CameraSystem;
import com.tankz.systems.misc.BoundarySystem;

public class HudRenderSystem extends EntitySystem {
	private GameContainer container;
	private Graphics g;
	private ComponentMapper<Health> healthMapper;
	private ComponentMapper<Ammo> ammoMapper;
	private Font font;
	private Color healthColor;
	private Entity player;
	private Image bar;
	private Color bgColor;
	private Image hudBg;
	private Image statusBar;
	private BoundarySystem boundarySystem;
	private CameraSystem cameraSystem;
	private Image minimapBg;
	private ComponentMapper<Physics> physicsMapper;

	public HudRenderSystem(GameContainer container) {
		super(Aspect.getAspectFor());

		this.container = container;
		this.g = container.getGraphics();
	}

	@Override
	public void initialize() {
		physicsMapper = world.getMapper(Physics.class);
		healthMapper = world.getMapper(Health.class);
		ammoMapper = world.getMapper(Ammo.class);
		
		healthColor = new Color(72f / 255f, 1f, 0f, 1f);
		bgColor = new Color(1f, 1f, 1f, 0.2f);

		try {
			font = new AngelCodeFont("fonts/normal.fnt", "fonts/normal_0.png");
			bar = new Image("bar.png");
			statusBar = new Image("statusBar.png");
			hudBg = new Image("hudBg.png");
			minimapBg = new Image("minimapBg.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

		ensurePlayerEntity();
		
		boundarySystem = world.getSystem(BoundarySystem.class);
		cameraSystem = world.getSystem(CameraSystem.class);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		ensurePlayerEntity();

		if (player != null) {
			hudBg.draw(0, container.getHeight()-hudBg.getHeight(), container.getWidth(), hudBg.getHeight());

			renderHealth();
			renderAmmo();
		}
		
		renderMinimap();
	}
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}

	private void renderMinimap() {
		int minimapWidth = minimapBg.getWidth();
		int minimapHeight = minimapBg.getHeight();
		
		int boundaryWidth = boundarySystem.getBoundaryWidth();
		int boundaryHeight = boundarySystem.getBoundaryHeight();
		
		float scaleX = (float)minimapWidth/(float)boundaryWidth;
		float scaleY = (float)minimapHeight/(float)boundaryHeight;
		
		g.setColor(Color.white);
		
		g.translate(container.getWidth()-minimapWidth-20,20);
		{
			minimapBg.draw();
			

			float offsetX = cameraSystem.getStartX();
			float offsetY = cameraSystem.getStartY();
			g.drawRect(offsetX*scaleX, offsetY*scaleY, scaleX*cameraSystem.getWidth(), scaleY*cameraSystem.getHeight());
			
			ImmutableBag<Entity> entities = world.getManager(GroupManager.class).getEntities("crates");
			for(int i = 0; entities.size() > i; i++) {
				Entity crate = entities.get(i);
				Physics cratePhysics = physicsMapper.get(crate);
				float crateX = cratePhysics.getX()*scaleX;
				float crateY = cratePhysics.getY()*scaleY;
				g.fillRect(crateX-1, crateY-1, 2, 2);
			}
			
			ImmutableBag<Entity> tanks = world.getManager(GroupManager.class).getEntities("tanks");
			for(int i = 0; tanks.size() > i; i++) {
				Entity t = tanks.get(i);
				String tp = world.getManager(PlayerManager.class).getPlayer(t);
				Physics physics = physicsMapper.get(t);
				g.setColor(Color.green);
				float tx = physics.getX()*scaleX;
				float ty = physics.getY()*scaleY;
				g.fillRect(tx-3, ty-3, 6, 6);
			}
			
		}
		g.translate(-container.getWidth()+minimapWidth+20,-20);
	}

	private void renderHealth() {
		Health health = healthMapper.get(player);
		g.translate(35, container.getHeight()-45);
		{
			font.drawString(-26, 8, "Health");
			g.rotate(0, 0, -90);
			float healthStatus = health.getHealthStatus();
			g.setDrawMode(Graphics.MODE_ADD);
			statusBar.draw(0,0,statusBar.getWidth()*healthStatus, statusBar.getHeight(), 0,0,statusBar.getWidth()*healthStatus, statusBar.getHeight(), healthStatus<0.25?Color.red:healthStatus<0.6?Color.yellow:Color.green);
			statusBar.draw(statusBar.getWidth()*healthStatus,0,statusBar.getWidth(), statusBar.getHeight(), statusBar.getWidth()*healthStatus,0,statusBar.getWidth(), statusBar.getHeight(), new Color(0.15f,0.15f,0.15f));
			g.setDrawMode(Graphics.MODE_NORMAL);
			g.rotate(0, 0, 90);
		}
		g.translate(-35, -container.getHeight()+45);
	}

	private void renderAmmo() {
		Ammo ammo = ammoMapper.get(player);
		g.translate(container.getWidth()-64, container.getHeight()-45);
		{
			font.drawString(-16, 8, "Ammo");
			g.rotate(0, 0, -90);
			float ammoStatus = ammo.getAmmoStatus();
			g.setDrawMode(Graphics.MODE_ADD);
			statusBar.draw(0,0,statusBar.getWidth()*ammoStatus, statusBar.getHeight(), 0,0,statusBar.getWidth()*ammoStatus, statusBar.getHeight(), ammoStatus<0.25?Color.red:ammoStatus<0.6?Color.yellow:Color.green);
			statusBar.draw(statusBar.getWidth()*ammoStatus,0,statusBar.getWidth(), statusBar.getHeight(), statusBar.getWidth()*ammoStatus,0,statusBar.getWidth(), statusBar.getHeight(), new Color(0.15f,0.15f,0.15f));
			g.setDrawMode(Graphics.MODE_NORMAL);
			g.rotate(0, 0, 90);
		}
		g.translate(-container.getWidth()+64, -container.getHeight()+45);
	}
	
	private void ensurePlayerEntity() {
		if (player == null || !player.isActive())
			player = world.getManager(TagManager.class).getEntity("PLAYER");
	}

}

