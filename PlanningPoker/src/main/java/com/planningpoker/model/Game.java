package com.planningpoker.model;

import java.util.LinkedList;
import java.util.Map;

import com.planningpoker.model.observer.Observer;

public class Game  {

	private final int id;
	private boolean finished;
	private final LinkedList<Player> players = new LinkedList<>();
	private final LinkedList<Play> plays = new LinkedList<>();
	
	public Game(int id, String managerName, Observer observer) {
		this.id = id;
		addManager(managerName, observer);
	}
	
	public int getId() {
		return id;
	}
	
	public Player getManager(){
		for (Player player : players) {
			if(player.isManager()){
				return player;
			}			
		}
		return null;
	}

	public LinkedList<Player> getPlayers() {
		return players;
	}

	public LinkedList<Play> getPlays() {
		return plays;
	}

	private Player addManager(String name, Observer observer){
		observer.newGame(this);
		return addPlayer(new Player(name, observer, true));
	}
	
	public Player addPlayer(String name, Observer observer){
		for (Player p : players) {
			if(p.getName().equals(name)){
				if(p.isOnline())
					throw new IllegalArgumentException("Ja existe um Jogador com o nome " + name);
				else {
					players.remove(p);
				}
			}
		}
		
		for (Player p : players) {
			if(p.isOnline()) {
				observer.newPlayerHasEnteredInTheGame(p);
			}
		}
		Map<Player, Integer> playersInCurrentPlay = getCurrentPlay().getPlays();
		for (Player p : playersInCurrentPlay.keySet()){
			if(p.isOnline()) {
				observer.playerHasVoteInCurrentPlay(p, playersInCurrentPlay.get(p));
			}			
		}
		return addPlayer(new Player(name, observer));
	}
	
	public Player addPlayer(Player newPlayer){
		if(!isFinished()){		
			newPlayer.setGame(this);
			players.add(newPlayer);
			notifyAboutNewPlayerHasEnteredInTheGame(newPlayer);			
			return newPlayer;
		}
		return null;
	}

	public Player getPlayer(String name){
		for (Player player : players) {
			if(player.getName().equals(name)){
				return player;
			}
		}
		throw new IllegalArgumentException("Player " + name + " not found!");
	}
	
	public void newPlay(){
		newPlay(null);
	}
	
	public void newPlay(String descr){
		if(!isFinished()){
			Play newPlay = new Play(this, plays.size(), descr);
			plays.add(newPlay);
			notifyAboutnewPlayHasBeenInitiated(newPlay);
		}
	}
		
	public Play getCurrentPlay(){		
		if(plays.isEmpty()){
			newPlay();
		}
		return plays.getLast();
	}

	int getNumberOfPlayers(){
		int count = 0;
		for (Player p : players) {
			count += p.isOnline()?1:0;
		}
		return count;
	}
	
	void finish() {
		if(!isFinished()){
			finished = true;
			for (Player p : players) {
				Observer observer = p.getObserver();
				if(observer!=null) {
					observer.gameHasBeenFinished();;
				}
			}
		}
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	@Override
	public String toString() {
		return "Game [id=" + id + ", players=" + players + "]";
	}

	private void notifyAboutNewPlayerHasEnteredInTheGame(Player newPlayer) {
		for (Player p : players) {
			Observer observer = p.getObserver();
			if(observer!=null) {
				observer.newPlayerHasEnteredInTheGame(newPlayer);
			}
		}
	}
	
	private void notifyAboutnewPlayHasBeenInitiated(Play newPlay) {
		for (Player p : players) {
			Observer observer = p.getObserver();
			if(observer!=null) {
				observer.newPlayHasBeenInitiated(newPlay);
			}
		}
	}
	
}
