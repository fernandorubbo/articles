package com.planningpoker.model;

import javax.json.Json;
import javax.json.JsonObject;

import com.planningpoker.io.JsonSerializable;
import com.planningpoker.model.observer.Observer;

public class Player implements Comparable<Player>, JsonSerializable{
	private String name;
	private boolean manager;
	private boolean online = true;
	private Game game;
	private Observer observer;
	
	public Player(String name, Observer observer) {
		this(name, observer, false);
	}
	
	public Player(String name, Observer observer, boolean isManager) {
		super();
		this.name = name;
		this.observer = observer;
		this.manager = isManager;		
	}

	public String getName() {
		return name;
	}

	public boolean isManager() {
		return manager;
	}

	public boolean isOnline() {
		return online;
	}
	
	public void offline() {
		if(!game.isFinished()){
			this.online  = false;	
			notifyAboutPlayerOffline();
			
			if(isManager()) {
				game.finish();
			} else {
				Play play = game.getCurrentPlay();
				play.getPlays().remove(this);
				if(play.hasFinished()){
					play.showResult();
				}
			}
		}
	}

	private void notifyAboutPlayerOffline() {
		for (Player p : game.getPlayers()) {
			if(!this.equals(p)) {
				Observer observer = p.getObserver();
				if(observer!=null) {
					observer.playerIsOffline(this);
				}
			}
		}
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Observer getObserver() {
		return observer;
	}

	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Player o) {
		return this.name.compareTo(o.name);
	}

	@Override
	public String toString() {
		return "Player [" + toJson() + "]";
	}
	
	@Override
	public JsonObject toJson() {		
		return Json.createObjectBuilder()
			.add("name", name )
	    	.add("manager", manager)
	    	.add("online", online )
	    	.build();
	}

}
