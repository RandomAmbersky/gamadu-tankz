package com.tankz.systems.network;

import com.esotericsoftware.kryonet.Connection;

public interface ConnectionHandler {

	void handle(Connection connection);
	
}
