package com.myapp;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
@WebServlet(name = "Dashboard WebSocket Servlet", urlPatterns = { "/dashboard" })
public class DashboardServet  extends WebSocketServlet {
 
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(DashboardWebSocket.class);
    }

}
