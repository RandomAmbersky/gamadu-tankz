package com.tankz;

import com.artemis.World;
import com.tankz.systems.misc.AmmoRegenerationSystem;
import com.tankz.systems.misc.BoundarySystem;
import com.tankz.systems.misc.ExpirationSystem;
import com.tankz.systems.misc.HealthSystem;
import com.tankz.systems.physics.PhysicsSystem;

public class TankzMultiplayerServer implements Runnable {

	private World world;

	public TankzMultiplayerServer() {
		initialize();
	}

	@Override
	public void run() {
		while (true) {
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

		world.setSystem(new HealthSystem());
		world.setSystem(new PhysicsSystem());
		world.setSystem(new ExpirationSystem());
		world.setSystem(new BoundarySystem(0, 0, 4096, 4096));
		world.setSystem(new AmmoRegenerationSystem());

		world.initialize();
	}

	public void update(int delta) {
		world.setDelta(delta);
		world.process();
	}

	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new TankzMultiplayerServer());
		thread.start();
	}

}
