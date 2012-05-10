package com.tankz;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Box;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.TrigLUT;
import com.tankz.components.Ammo;
import com.tankz.components.Expiration;
import com.tankz.components.Health;
import com.tankz.components.Physics;
import com.tankz.components.SoundFile;
import com.tankz.components.SpatialForm;
import com.tankz.components.Tower;
import com.tankz.components.Transform;
import com.tankz.components.TurnFactor;
import com.tankz.components.Velocity;

public class EntityFactory {
	public static Entity createExplosion(World world, float x, float y) {
		Entity e = world.createEntity();
		e.addComponent(new Transform(x, y));
		e.addComponent(new SpatialForm("explosion"));
		e.addComponent(new Expiration(200));
		e.refresh();
		return e;
	}

	public static Entity createBullet(World world, float x, float y, float angle, Entity shooter) {
		Entity e = world.createEntity();
		e.setGroup("bullets");
		Transform transform = new Transform(x, y, angle);
		e.addComponent(transform);
		e.addComponent(new SpatialForm("bullet"));
		e.addComponent(new Expiration(1500));

		Body b = new Body(new Box(10, 10), 0.2f);
		b.setUserData(e);
		b.addExcludedBody(shooter.getComponent(Physics.class).getBody());
		b.setBitmask(1);
		b.setPosition(x, y);
		b.setRestitution(0);
		b.setDamping(0.002f);
		b.setFriction(10);
		b.setRotation(angle);
		// b.setForce(10000f*TrigLUT.cosDeg(angle),
		// 10000f*TrigLUT.sinDeg(angle));
		b.adjustVelocity(new Vector2f(1000f * TrigLUT.cosDeg(angle), 1000f * TrigLUT.sinDeg(angle)));

		// b.setDensity(100);
		// b.setFriction(1.1f);
		// b.setDamping(0.002f);
		// b.setRestitution(0);

		e.addComponent(new Physics(b));

		e.refresh();

		return e;
	}

	public static Entity createWall(World world, float x, float y) {
		Entity e = world.createEntity();
		e.setGroup("walls");

		SpatialForm form = new SpatialForm("wall");
		e.addComponent(form);

		Body b = new Body(new Box(214, 214), 0.3f);
		b.setMoveable(false);
		b.setRotatable(false);
		b.setUserData(e);
		b.setPosition(x, y);
		b.setDamping(0.1f);
		b.setRestitution(0);
		b.setRotDamping(10f);
		b.setFriction(100);
		e.addComponent(new Physics(b));

		e.refresh();

		return e;
	}

	public static Entity createCrate(World world, float x, float y, float angleDeg) {
		Entity e = world.createEntity();
		e.setGroup("crates");
		e.addComponent(new Health(100, 160));

		SpatialForm form = new SpatialForm("crate");
		e.addComponent(form);

		Body b = new Body(new Box(50, 50), 0.3f);
		b.setUserData(e);
		b.setPosition(x, y);
		b.setDamping(0.1f);
		b.setRestitution(0);
		b.setRotDamping(10f);
		b.setFriction(100);
		b.setRotation(angleDeg);
		e.addComponent(new Physics(b));

		e.refresh();

		return e;
	}

	public static Entity createMammothTank(World world, float x, float y) {
		Entity e = world.createEntity();
		e.setGroup("tanks");

		e.addComponent(new SpatialForm("mammothTank"));
		e.addComponent(new Velocity());
		e.addComponent(new TurnFactor());
		e.addComponent(new Tower());
		e.addComponent(new Health(110, 150));
		e.addComponent(new Ammo(78, 150));

		Body b = new Body(new Box(125, 104), 1f);
		b.setUserData(e);
		b.setPosition(x, y);
		b.setDamping(0.1f);
		b.setRestitution(0);
		b.setRotDamping(50f);
		b.setFriction(100);
		e.addComponent(new Physics(b));

		return e;
	}

	public static Entity createSound(World world, String soundFileName) {
		Entity sound = world.createEntity();
		sound.addComponent(new SoundFile(soundFileName));
		sound.refresh();
		return sound;
	}
}
