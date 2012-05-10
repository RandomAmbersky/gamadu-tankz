package com.tankz.components;

import com.artemis.Component;

public class SoundFile extends Component {
	private String soundFile;
	
	public SoundFile(String audioFile) {
		this.soundFile = audioFile;
	}

	public String getSoundFile() {
		return soundFile;
	}
}
