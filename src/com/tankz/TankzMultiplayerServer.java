package com.tankz;

import java.util.Random;

import com.artemis.EntitySystem;
import com.artemis.SystemManager;
import com.artemis.World;
import com.tankz.systems.misc.AmmoRegenerationSystem;
import com.tankz.systems.misc.BoundarySystem;
import com.tankz.systems.misc.ExpirationSystem;
import com.tankz.systems.misc.HealthSystem;
import com.tankz.systems.physics.PhysicsSystem;

public class TankzMultiplayerServer implements Runnable {
	
	private World world;
	private EntitySystem boundarySystem;
	private EntitySystem physicsSystem;
	private EntitySystem healthSystem;
	private EntitySystem expirationSystem;
	private EntitySystem ammoRegenerationSystem;
	private Random rand;
	
	public TankzMultiplayerServer() {
		initialize();
	}
	
	@Override
	public void run() {
		while(true) {
			System.out.println("Running game loop");
			update(1000);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initialize() {
		world = new World();
		SystemManager systemManager = world.getSystemManager();

		boundarySystem = systemManager.setSystem(new BoundarySystem(0, 0, 4096, 4096));
		physicsSystem = systemManager.setSystem(new PhysicsSystem());
		healthSystem = systemManager.setSystem(new HealthSystem());
		expirationSystem = systemManager.setSystem(new ExpirationSystem());
		ammoRegenerationSystem = systemManager.setSystem(new AmmoRegenerationSystem());

		systemManager.initializeAll();
	}
	
	public void update(int delta) {
		world.setDelta(delta);

		healthSystem.process();
		physicsSystem.process();
		expirationSystem.process();
		boundarySystem.process();
		ammoRegenerationSystem.process();
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new TankzMultiplayerServer());
		thread.start();
	}



}
