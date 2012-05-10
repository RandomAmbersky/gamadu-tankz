package com.tankz.systems.network;

import com.esotericsoftware.kryonet.Connection;

public interface Handler {

	void handle(Connection connection, Object object);
	
}
