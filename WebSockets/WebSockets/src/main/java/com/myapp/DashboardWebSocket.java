package com.myapp;

import static java.lang.System.out;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/dashboard")
public class DashboardWebSocket {

    @OnOpen
    public void onOpen(Session session) {
        out.println("==>> Opening connection " + session.getId());
        EventGenerator.registry(new EventObserver(session));
    }

    @OnClose
    public void onClose(Session session) {
        out.println("==>> Closing connection " + session.getId());
        EventGenerator.unregistry(new EventObserver(session));
    }
}
