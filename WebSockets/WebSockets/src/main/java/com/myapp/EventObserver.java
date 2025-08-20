package com.myapp;

import java.io.IOException;

import jakarta.websocket.Session;

public class EventObserver extends Observer {

    private Session session;

    public EventObserver(Session session) {
        super(session.getId());
        this.session = session;
    }

    @Override
    public void notify(Event event) {
        try {
            if (session.isOpen()) {
                System.out.println("Sending '" + event + "' to '" + name + "'");
                session.getBasicRemote().sendText(event.toJson());
            } else {
                session.close();
            }
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException e1) {
                // ignore
            }
        }
    }
}
