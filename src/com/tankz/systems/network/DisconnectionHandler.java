package com.tankz.systems.network;

import com.esotericsoftware.kryonet.Connection;

public interface DisconnectionHandler {

	void handle(Connection connection);
	
}
