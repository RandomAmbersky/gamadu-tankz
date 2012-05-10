package com.tankz.managers;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Entity;
import com.artemis.Manager;

public class PlayerManager implements Manager {
	private Map<Entity,Player> playerByEntity;
	
	public PlayerManager() {
		playerByEntity = new HashMap<Entity, Player>();
	}
	
	public void setPlayer(Entity e, Player player) {
		playerByEntity.put(e, player);
	}
	
	public Player getPlayer(Entity e) {
		return playerByEntity.get(e);
	}

}
