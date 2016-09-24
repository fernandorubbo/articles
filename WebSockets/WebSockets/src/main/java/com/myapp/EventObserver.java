package com.myapp;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;

public class EventObserver extends Observer {

	private Session session;

	public EventObserver(Session session) {
		super(session.getRemoteAddress().toString());
		this.session = session;
	}

	@Override
	public void notify(Event event) {
		try {
			if (session.isOpen()) {
				System.out.println("Sending '" + event + "' to '" + name + "'");
				session.getRemote().sendString(event.toJson());
			} else {
				session.close();
			}
		} catch (IOException e) {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

}
