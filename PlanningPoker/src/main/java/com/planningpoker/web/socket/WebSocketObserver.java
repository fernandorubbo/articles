package com.planningpoker.web.socket;

import jakarta.json.JsonObject;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.Callback;

import com.planningpoker.model.Game;
import com.planningpoker.model.Play;
import com.planningpoker.model.Player;
import com.planningpoker.model.observer.Observer;

public class WebSocketObserver implements Observer {

	private final Session session;

	public WebSocketObserver(Session session){
		this.session = session;
	}

	@Override
	public void newGame(Game game) {
		send(new JSFunction("newGame", "id", game.getId()));
	}

	@Override
	public void newPlayerHasEnteredInTheGame(Player player) {
		send(new JSFunction("newPlayerHasEnteredInTheGame", player));
	}

	@Override
	public void newPlayHasBeenInitiated(Play play) {
		send(new JSFunction("newPlayHasBeenInitiated", play));
	}

	@Override
	public void gameHasBeenFinished() {
		send(new JSFunction("gameHasBeenFinished", null));
	}

	@Override
	public void playerHasVoteInCurrentPlay(final Player player, final int points) {
		send(new JSFunction("playerHasVoteInCurrentPlay", player));
	}

	@Override
	public void showResult(Play play) {
		send(new JSFunction("showResult", play));
	}

	@Override
	public void playerIsOffline(Player player) {
		send(new JSFunction("playerIsOffline", player));
	}

	@Override
	public void onError(Throwable e) {
		Throwable cause = e;
		while(cause.getCause()!=null){
			cause = cause.getCause();
		}
		send(new JSFunction("onError", "message", cause.getMessage()));
	}

	private void send(JSFunction jsFunction) {
		if(session.isOpen()) {
			JsonObject json = jsFunction.toJson();
			String remoteAddr = session.getRemoteSocketAddress().toString();
			System.out.println("[" + remoteAddr + "] Sending message " + json);
			session.sendText(json.toString(), null);
		}
	}

}
