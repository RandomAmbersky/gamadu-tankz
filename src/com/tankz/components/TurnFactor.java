package com.tankz.components;

import com.artemis.Component;

public class TurnFactor extends Component {
	private float factor;
	
	public TurnFactor() {
	}

	public void setFactor(float factor) {
		this.factor = factor;
	}
	
	public float getFactor() {
		return factor;
	}

}
