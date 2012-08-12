package com.tankz.systems.network;

import java.io.IOException;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.tankz.systems.network.messages.ChatMessage;

public class ClientNetworkSystem extends IntervalEntitySystem {
	private Client client;
	private String serverIp;

	public ClientNetworkSystem(int interval, String serverIp) {
		super(Aspect.getAspectFor(), interval);
		this.serverIp = serverIp;
	}
	
	@Override
	public void initialize() {
		NetworkListener networkListener = new NetworkListener();
		
		networkListener.registerHandler(ChatMessage.class, new Handler() {
			@Override
			public void handle(Connection connection, Object object) {
				ChatMessage msg = (ChatMessage) object;
				System.out.println("Got message: " + msg.getMessage());
			}
		});

		networkListener.setConnectionHandler(new ConnectionHandler() {
			@Override
			public void handle(Connection connection) {
			}
		});

		networkListener.setDisconnectionHandler(new DisconnectionHandler() {
			@Override
			public void handle(Connection connection) {
			}
		});


		client = new Client();
		Kryo kryo = client.getKryo();
		registerKryo(kryo);
		
		client.addListener(networkListener);
		
		try {
			client.connect(5000, serverIp, 57007, 57008);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void registerKryo(Kryo kryo) {
		kryo.register(ChatMessage.class);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
	}
}
