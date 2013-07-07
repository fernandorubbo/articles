package com.planningpoker.web;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.planningpoker.web.socket.PlanningPokerWebSocket;

@SuppressWarnings("serial")
@WebServlet(name = "Planning Poker WebSocket Servlet", urlPatterns = { "/planningpoker" })
public class PlanningPokerServet extends WebSocketServlet {

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(PlanningPokerWebSocket.class);
	}

}
