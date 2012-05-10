package com.tankz.systems.physics;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.CollisionEvent;
import net.phys2d.raw.CollisionListener;
import net.phys2d.raw.World;
import net.phys2d.raw.strategies.QuadSpaceStrategy;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.IntervalEntitySystem;
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
		super(20, Physics.class);
	}

	@Override
	public void initialize() {
		physicsMapper = new ComponentMapper<Physics>(Physics.class, world);
		transformMapper = new ComponentMapper<Transform>(Transform.class, world);

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

		String groupA = world.getGroupManager().getGroupOf(entityA);
		String groupB = world.getGroupManager().getGroupOf(entityB);
		
		if("crates".equalsIgnoreCase(groupA) && "bullets".equalsIgnoreCase(groupB)) {
			handleBulletHittingTarget(entityB, entityA);
		} else if("crates".equalsIgnoreCase(groupB) && "bullets".equalsIgnoreCase(groupA)) {
			handleBulletHittingTarget(entityA, entityB);
		} else if("tanks".equalsIgnoreCase(groupB) && "bullets".equalsIgnoreCase(groupA)) {
			handleBulletHittingTarget(entityA, entityB);
		} else if("bullets".equalsIgnoreCase(groupB) && "tanks".equalsIgnoreCase(groupA)) {
			handleBulletHittingTarget(entityB, entityA);
		} else if("bullets".equalsIgnoreCase(groupB) && "walls".equalsIgnoreCase(groupA)) {
			handleBulletHittingTarget(entityB, entityA);
		} else if("walls".equalsIgnoreCase(groupB) && "bullets".equalsIgnoreCase(groupA)) {
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
