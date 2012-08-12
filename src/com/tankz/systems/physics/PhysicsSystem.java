package com.tankz.systems.physics;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.CollisionEvent;
import net.phys2d.raw.CollisionListener;
import net.phys2d.raw.World;
import net.phys2d.raw.strategies.QuadSpaceStrategy;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.tankz.EntityFactory;
import com.tankz.components.Health;
import com.tankz.components.Physics;
import com.tankz.components.Transform;

public class PhysicsSystem extends IntervalEntitySystem implements CollisionListener {
	private ComponentMapper<Physics> physicsMapper;
	private ComponentMapper<Transform> transformMapper;
	private World physicsWorld;

	public PhysicsSystem() {
		super(Aspect.getAspectFor(Physics.class), 20);
	}

	@Override
	public void initialize() {
		physicsMapper = world.getMapper(Physics.class);
		transformMapper = world.getMapper(Transform.class);

		physicsWorld = new World(new Vector2f(0, 0), 10, new QuadSpaceStrategy(8, 6));
		physicsWorld.enableRestingBodyDetection(1f, 1f, 1f);
		physicsWorld.addListener(this);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		physicsWorld.step();
	}
	
	@Override
	protected void added(Entity e) {
		Physics collidable = e.getComponent(Physics.class);
		Body body = collidable.getBody();
		body.setUserData(e);
		physicsWorld.add(body);
	}

	@Override
	protected void removed(Entity e) {
		Physics collidable = e.getComponent(Physics.class);
		physicsWorld.remove(collidable.getBody());
	}

	@Override
	public void collisionOccured(CollisionEvent event) {
		Body bodyA = event.getBodyA();
		Body bodyB = event.getBodyB();

		Entity entityA = Entity.class.cast(bodyA.getUserData());
		Entity entityB = Entity.class.cast(bodyB.getUserData());

		ImmutableBag<String> groupsA = world.getManager(GroupManager.class).getGroups(entityA);
		ImmutableBag<String> groupsB = world.getManager(GroupManager.class).getGroups(entityB);
		
		if(groupsA.contains("crates") && groupsB.contains("bullets")) {
			handleBulletHittingTarget(entityB, entityA);
		}
		else if(groupsB.contains("crates") && groupsA.contains("bullets")) {
			handleBulletHittingTarget(entityA, entityB);
		}
		else if(groupsB.contains("tanks") && groupsA.contains("bullets")) {
			handleBulletHittingTarget(entityA, entityB);
		}
		else if(groupsB.contains("bullets") && groupsA.contains("tanks")) {
			handleBulletHittingTarget(entityB, entityA);
		}
		else if(groupsB.contains("bullets") && groupsA.contains("walls")) {
			handleBulletHittingTarget(entityB, entityA);
		}
		else if(groupsB.contains("walls") && groupsA.contains("bullets")) {
			handleBulletHittingTarget(entityA, entityB);
		}
	}

	private void handleBulletHittingTarget(Entity bullet, Entity target) {
		addDamageToTarget(target);
		
		Physics bp = bullet.getComponent(Physics.class);
		EntityFactory.createExplosion(world, bp.getX(), bp.getY());

		world.deleteEntity(bullet);
	}
	
	private void addDamageToTarget(Entity crate) {
		Health h = crate.getComponent(Health.class);
		if(h != null)
			h.addDamage(10f);
	}



}
