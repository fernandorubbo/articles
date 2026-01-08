package com.planningpoker.web.socket;

import static java.lang.System.out;

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketOpen;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.planningpoker.model.Game;
import com.planningpoker.model.Play;
import com.planningpoker.model.Player;
import com.planningpoker.model.observer.Observer;
import com.planningpoker.web.Games;

@WebSocket
public class PlanningPokerWebSocket {

	@OnWebSocketOpen
	public void onOpen(final Session session) {
		final Map<String, List<String>> params = session.getUpgradeRequest().getParameterMap();
		List<String> id = params.get("gameId");
		final Integer gameId = id!=null && !"null".equals(id.get(0)) ? Integer.valueOf(id.get(0)) : null;
		final String playerName = params.get("playerName").get(0);

		out.println("==>> Opening connection " + session.getRemoteSocketAddress() + " - " + gameId + " - " + playerName);

		WebSocketObserver observer = new WebSocketObserver(session);
		if (gameId == null) {
			Games.nextGame(playerName, observer);
		} else {
			final Game game = Games.getGame(gameId);
			if(game==null)
				throw new IllegalArgumentException("Game com id " + gameId + " nao existe ou ja foi finalizado!");
			game.addPlayer(playerName, observer);
		}
	}

	@OnWebSocketMessage
	public void onMessage(final Session session, final String json) {
		final Map<String, List<String>> params = session.getUpgradeRequest().getParameterMap();
		List<String> id = params.get("gameId");
		final Integer gameId = id!=null && !"null".equals(id.get(0)) ? Integer.valueOf(id.get(0)) : null;
		final String playerName = params.get("playerName").get(0);

		out.println("==>> Opening connection " + session.getRemoteSocketAddress() + " - " + gameId + " - " + playerName);

		out.println("==>> On vote " + session.getRemoteSocketAddress() + " - " + json);

		JsonObject obj = Json.createReader(new StringReader(json)).readObject();
		String method = obj.getString("action");
		for(Method m : this.getClass().getMethods()){
			if(m.getName().equals(method)) {
				Class<?>[] parameterTypes = m.getParameterTypes();
				Annotation[][] parameterAnns = m.getParameterAnnotations();
				Object[] args = new Object[parameterTypes.length];
				int i = 0;
				for(Class<?> paramType : parameterTypes) {
					String parName = ((ParName)parameterAnns[i][0]).value();
					if(paramType.isAssignableFrom(Integer.class)) {
						args[i++] = obj.getInt(parName);
					} else {
						args[i++] = obj.getString(parName);
					}
				}
				try {
					m.invoke(this, args);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void vote(@ParName("gameId") Integer gameId, @ParName("playerName")String playerName, @ParName("points")Integer points){
		final Game game = Games.getGame(gameId);
		final Player player = game.getPlayer(playerName);
		final Play currentPlay = game.getCurrentPlay();
		currentPlay.vote(player, points);
	}

	public void newPlay(@ParName("gameId") Integer gameId, @ParName("playerName")String playerName){
		final Game game = Games.getGame(gameId);
		final Player player = game.getPlayer(playerName);
		if(player.isManager()) {
			game.newPlay();
		}
	}

	public void showIncompleteResult(@ParName("gameId") Integer gameId, @ParName("playerName")String playerName){
		final Game game = Games.getGame(gameId);
		final Player player = game.getPlayer(playerName);
		if(player.isManager()) {
			game.getCurrentPlay().showResult();
		}
	}

	@OnWebSocketError
	public void onErro(final Session session, final Throwable cause) {
		final Map<String, List<String>> params = session.getUpgradeRequest().getParameterMap();
		List<String> id = params.get("gameId");
		final Integer gameId = id!=null && !"null".equals(id.get(0)) ? Integer.valueOf(id.get(0)) : null;
		final String playerName = params.get("playerName").get(0);

		out.println("==>> On Error " + session.getRemoteSocketAddress() + " - " + gameId + " - " + playerName + " - " + cause.getMessage());

		new WebSocketObserver(session).onError(cause);
	}

	@OnWebSocketClose
	public void onClose(final Session session, int x, String text) {
		final Map<String, List<String>> params = session.getUpgradeRequest().getParameterMap();
		List<String> id = params.get("gameId");
		final Integer gameId = id!=null && !"null".equals(id.get(0)) ? Integer.valueOf(id.get(0)) : null;
		final String playerName = params.get("playerName").get(0);

		out.println("==>> Closing connection " + session.getRemoteSocketAddress() + " - " + gameId + " - " + playerName);

		Game game = Games.getGame(gameId);
		if(game==null) {
			game = Games.findGame(g -> {
				for(Player p : g.getPlayers()) {
					Observer obs = p.getObserver();
					if (obs instanceof WebSocketObserver && ((WebSocketObserver)obs).getSession().equals(session)) {
						return true;
					}
				}
				return false;
			});
		}

		if(game!=null)
			game.getPlayer(playerName).offline();
	}
}
