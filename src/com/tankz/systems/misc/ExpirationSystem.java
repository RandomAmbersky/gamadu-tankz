package com.tankz.systems.misc;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.DelayedEntityProcessingSystem;
import com.tankz.components.Expiration;

public class ExpirationSystem extends DelayedEntityProcessingSystem {

	private ComponentMapper<Expiration> expirationMapper;
	private int smallestLifeTime;

	public ExpirationSystem() {
		super(Aspect.getAspectFor(Expiration.class));
	}

	@Override
	public void initialize() {
		expirationMapper = world.getMapper(Expiration.class);
	}

	@Override
	protected void begin() {
		smallestLifeTime = Integer.MAX_VALUE;
	}

	@Override
	protected void process(Entity e, int accumulatedDelta) {
		Expiration expiration = expirationMapper.get(e);

		expiration.reduceLifeTime(accumulatedDelta);

		if (expiration.isExpired()) {
			world.deleteEntity(e);
		} else if (expiration.getLifeTime() < smallestLifeTime) {
			smallestLifeTime = expiration.getLifeTime();
		}
	}

	@Override
	protected void end() {
		if (smallestLifeTime < Integer.MAX_VALUE && smallestLifeTime > 0) {
			startDelayedRun(smallestLifeTime);
		}
	}

	@Override
	protected void added(Entity e) {
		Expiration expiration = expirationMapper.get(e);
		if (!isRunning() || expiration.getLifeTime() < getRemainingTimeUntilProcessing()) {
			startDelayedRun(expiration.getLifeTime());
		}
	}

}
