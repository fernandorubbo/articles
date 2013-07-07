package com.planningpoker.model;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.planningpoker.io.JsonSerializable;
import com.planningpoker.model.observer.Observer;

public class Play implements JsonSerializable {
	private final int id;
	private final String descr;
	private final Map<Player, Integer> plays = new HashMap<>();
	private final Game game;
	
	public Play(Game game, int id) {
		this(game, id, null);
	}
	
	public Play(Game game, int id, String descr) {
		this.id = id;
		this.descr = descr;
		this.game = game;
	}
	
	public int getId() {
		return id;
	}

	public String getDescr() {
		return descr;
	}

	public Map<Player, Integer> getPlays() {
		return plays;
	}

	public void vote(Player player, int points){
		if(!game.isFinished()){
			plays.put(player, points);
			notifyAboutPlayerHasVoteInCurrentPlay(player, points);
			if(hasFinished()){
				showResult();
			}
		}
	}
	
	public void showResult(){
		if(!game.isFinished()){
			for (Player p : game.getPlayers()) {
				Observer observer = p.getObserver();
				if(observer!=null) {
					observer.showResult(this);
				}
			}
		}
	}
	
	private void notifyAboutPlayerHasVoteInCurrentPlay(Player player, int points) {
		for (Player p : game.getPlayers()) {
			Observer observer = p.getObserver();
			if(observer!=null) {
				observer.playerHasVoteInCurrentPlay(player, points);
			}
		}
	}
	
	public boolean hasFinished(){
		return getNumberOfVotes() == game.getNumberOfPlayers();
	}
	
	private int getNumberOfVotes(){
		return plays.size();
	}


	@Override
	public String toString() {
		return "Play ["+ toJson() +"]";
	}
	
	@Override
	public JsonObject toJson() {
		final JsonObjectBuilder builder = Json.createObjectBuilder()
			.add("id", id )
			.add("descr", descr==null ? "" : descr);
		    	
		JsonArrayBuilder array = Json.createArrayBuilder();
		for (Player player : plays.keySet()) {
			array.add(Json.createObjectBuilder()
						.add("name", player.getName() )
			    		.add("points", plays.get(player)));
		}	
		
		return builder.add("plays", array).build();
	}
}
