package com.tankz.managers;

import org.newdawn.slick.Color;

public class Player {
	private Color color;
	private String name;

	public Player(Color color, String name) {
		this.color = color;
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
