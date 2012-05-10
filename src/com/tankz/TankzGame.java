package com.tankz;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.Manager;
import com.artemis.SystemManager;
import com.artemis.World;
import com.tankz.managers.Player;
import com.tankz.managers.PlayerManager;
import com.tankz.systems.camera.CameraSystem;
import com.tankz.systems.misc.AmmoRegenerationSystem;
import com.tankz.systems.misc.BoundarySystem;
import com.tankz.systems.misc.ExpirationSystem;
import com.tankz.systems.misc.HealthSystem;
import com.tankz.systems.misc.SoundSystem;
import com.tankz.systems.physics.PhysicsSystem;
import com.tankz.systems.player.PlayerTankMovementSystem;
import com.tankz.systems.player.PlayerTankTowerSystem;
import com.tankz.systems.rendering.CrosshairRenderSystem;
import com.tankz.systems.rendering.HealthRenderSystem;
import com.tankz.systems.rendering.HudRenderSystem;
import com.tankz.systems.rendering.RenderSystem;
import com.tankz.systems.rendering.TerrainRenderSystem;

public class TankzGame extends BasicGame {
	private World world;

	private Random rand;

	private EntitySystem renderSystem;
	private EntitySystem physicsSystem;
	private EntitySystem expirationSystem;
	private EntitySystem playerTankTowerSystem;
	private EntitySystem hudRenderSystem;
	private EntitySystem soundSystem;
	private EntitySystem playerTankMovementSystem;
	private EntitySystem healthRenderSystem;
	private EntitySystem healthSystem;
	private EntitySystem boundarySystem;
	private EntitySystem cameraSystem;

	private EntitySystem terrainRenderSystem;

	private EntitySystem ammoRegenerationSystem;

	private EntitySystem clientNetworkSystem;

	private EntitySystem crosshairRenderSystem;

	public TankzGame(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		world = new World();
		
		world.setManager(new PlayerManager());
		
		SystemManager systemManager = world.getSystemManager();
		
		boundarySystem = systemManager.setSystem(new BoundarySystem(0, 0, 4096, 4096));
		physicsSystem = systemManager.setSystem(new PhysicsSystem());
		healthSystem = systemManager.setSystem(new HealthSystem());
		soundSystem = systemManager.setSystem(new SoundSystem());
		expirationSystem = systemManager.setSystem(new ExpirationSystem());
		playerTankTowerSystem = systemManager.setSystem(new PlayerTankTowerSystem(container));
		playerTankMovementSystem = systemManager.setSystem(new PlayerTankMovementSystem(container));
		cameraSystem = systemManager.setSystem(new CameraSystem(container));
		ammoRegenerationSystem = systemManager.setSystem(new AmmoRegenerationSystem());
		//clientNetworkSystem = systemManager.setSystem(new ClientNetworkSystem(10, ""));
		
		terrainRenderSystem = systemManager.setSystem(new TerrainRenderSystem(container));
		renderSystem = systemManager.setSystem(new RenderSystem(container));
		hudRenderSystem = systemManager.setSystem(new HudRenderSystem(container));
		healthRenderSystem = systemManager.setSystem(new HealthRenderSystem(container));
		crosshairRenderSystem = systemManager.setSystem(new CrosshairRenderSystem(container));

		systemManager.initializeAll();

		Entity playerTank = EntityFactory.createMammothTank(world, 300f, 400f);
		world.getManager(PlayerManager.class).setPlayer(playerTank, new Player(new Color(51, 204, 69), "APPEL"));
		playerTank.setTag("PLAYER");
		playerTank.refresh();


		{
			Entity e = EntityFactory.createMammothTank(world, 600f, 800f);
			world.getManager(PlayerManager.class).setPlayer(e, new Player(new Color(255,128,0), "COMPUTER 1"));
			e.refresh();
		}
		{
			Entity e = EntityFactory.createMammothTank(world, 1000, 200);
			world.getManager(PlayerManager.class).setPlayer(e, new Player(new Color(0,128,255), "COMPUTER 2"));
			e.refresh();
		}
		
		
		EntityFactory.createWall(world, 756, 540);
		EntityFactory.createWall(world, 756+(5*108), 540);
		EntityFactory.createWall(world, 756+(10*108), 540);

		EntityFactory.createWall(world, 756, 540+(10*108));
		EntityFactory.createWall(world, 756+(5*108), 540+(10*108));
		EntityFactory.createWall(world, 756+(10*108), 540+(10*108));
		

		rand = new Random(3);
		for (int i = 0; 50 > i; i++) {
			int gx = rand.nextInt(4096);
			int gy = rand.nextInt(4096);
			int g = rand.nextInt(5);
			
			for(int a = 0; g > a; a++) {
				EntityFactory.createCrate(world, gx+rand.nextInt(100), gy+rand.nextInt(100), rand.nextInt(360));
			}
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		world.loopStart();

		world.setDelta(delta);

		soundSystem.process();
		healthSystem.process();
		physicsSystem.process();
		expirationSystem.process();
		playerTankTowerSystem.process();
		playerTankMovementSystem.process();
		boundarySystem.process();
		cameraSystem.process();
		ammoRegenerationSystem.process();
		//clientNetworkSystem.process();
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		terrainRenderSystem.process();
		renderSystem.process();
		healthRenderSystem.process();
		crosshairRenderSystem.process();
		hudRenderSystem.process();
	}

	public static void main(String[] args) throws SlickException {
		TankzGame tankz = new TankzGame("Tankz");
		AppGameContainer container = new AppGameContainer(tankz);
		container.setDisplayMode(Math.round(container.getScreenWidth() * 0.7f), Math.round(container.getScreenHeight() * 0.8f), false);
		container.setAlwaysRender(true);
		//container.setDisplayMode(1920, 1200, true);
		//container.setTargetFrameRate(60);
		//container.setMaximumLogicUpdateInterval(1);
		//container.setMinimumLogicUpdateInterval(1);
		
		container.setMusicOn(false);
		container.setMusicVolume(0.1f);
		container.setSoundOn(false);
		container.setSoundVolume(0.1f);
		
		container.start();
	}
}
