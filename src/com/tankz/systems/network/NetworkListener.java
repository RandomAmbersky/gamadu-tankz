package com.tankz.systems.network;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkListener extends Listener {
	private Map<Class<?>, Handler> handlers;
	private ConnectionHandler connectionHandler;
	private DisconnectionHandler disconnectionHandler;
	
	public NetworkListener() {
		handlers = new HashMap<Class<?>, Handler>();
	}
	
	@Override
	public void received(Connection connection, Object object) {
		Handler handler = handlers.get(object.getClass());
		if(handler != null) {
			handler.handle(connection, object);
		} else {
			System.out.println("Unhandled object " + object.getClass());
		}
	}
	
	@Override
	public void connected(Connection connection) {
		if(disconnectionHandler != null) {
			connectionHandler.handle(connection);
		} else {
			System.out.println("Unhandled connection.");
		}
	}
	
	@Override
	public void disconnected(Connection connection) {
		if(disconnectionHandler != null) {
			disconnectionHandler.handle(connection);
		} else {
			System.out.println("Unhandled disconnection.");
		}
	}
	
	public void registerHandler(Class<?> handlerFor, Handler handler) {
		handlers.put(handlerFor, handler);
	}
	
	public void removeHandler(Class<?> handlerFor) {
		handlers.remove(handlerFor);
	}
	
	public void setConnectionHandler(ConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
	}
	
	public void setDisconnectionHandler(DisconnectionHandler disconnectionHandler) {
		this.disconnectionHandler = disconnectionHandler;
	}

}
