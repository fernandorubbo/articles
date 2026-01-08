package com.planningpoker.web;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import com.planningpoker.model.Game;
import com.planningpoker.model.observer.Observer;

public class Games {

	private static final Map<Integer, Game> HOLDER = new HashMap<>();
	private static int count = 0;

	public static synchronized Game findGame(Predicate<Game> predicate){
		for(Game game : HOLDER.values()){
			if(predicate.test(game)) {
				return game;
			}
		}
		return null;
	}

	public static synchronized Game nextGame(String managerName, Observer observer){
		int nextId = ++count;
		Game game = new Game(nextId, managerName, observer);
		HOLDER.put(nextId, game);
		return game;
	}

	public static synchronized Game getGame(Integer gameId){
		if(gameId==null)
			return null;

		Game game = HOLDER.get(gameId);
		if(game.isFinished())
			return null;

		return game;
	}

}
