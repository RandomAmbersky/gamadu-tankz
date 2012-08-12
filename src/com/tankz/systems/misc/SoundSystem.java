package com.tankz.systems.misc;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.tankz.components.SoundFile;

public class SoundSystem extends EntityProcessingSystem {
	private ComponentMapper<SoundFile> soundMapper;

	public SoundSystem() {
		super(Aspect.getAspectFor(SoundFile.class));
	}

	@Override
	public void initialize() {
		soundMapper = world.getMapper(SoundFile.class);
	}

	@Override
	protected void process(Entity e) {
		world.deleteEntity(e);
	}

	@Override
	protected void added(Entity e) {
		SoundFile soundFile = soundMapper.get(e);

		try {
			Sound sound = new Sound(soundFile.getSoundFile());
			sound.play(1, 0.3f);
		} catch (SlickException e1) {
			e1.printStackTrace();
		}
	}

}
