package com.myapp;  
  
import static java.lang.System.out;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
  
@WebSocket  
public class DashboardWebSocket  {
 
	@OnWebSocketClose
	public void onClose(final Session session, int x, String text) {
		out.println("==>> Closing connection "  + session.getRemoteAddress());
        EventGenerator.unregistry(new EventObserver(session));
	}
	
    @OnWebSocketConnect
    public void onOpen(final Session session) {
    	out.println("==>> Opening connection "  + session.getRemoteAddress());
    	EventGenerator.registry(new EventObserver(session));
    }  
}  