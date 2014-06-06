package org.mcsg.bot.skype.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.mcsg.bot.skype.util.Connect4Game.Tile;

public class Connect4Manager {

	private static Connect4Manager instance = new Connect4Manager();
	
	private HashMap<String, ArrayList<Connect4Game>> games = new HashMap<String, ArrayList<Connect4Game>>();
	
	private Connect4Manager(){
		
	}
	
	public static Connect4Manager getInstance(){
		return instance;
	}
	
	public Connect4Game createGame(String chat, String p1, String p2){
		Connect4Game c4 = new Connect4Game(p1, p2, Tile.RED, Tile.BLUE);
		ArrayList<Connect4Game> gamea = games.get(chat);
		if(gamea == null){
			gamea = new ArrayList<Connect4Game>();
		}
		gamea.add(c4);
		games.put(chat, gamea);
		return c4;		
	}
	
	public Connect4Game getGame(String chat, String p1){
		ArrayList<Connect4Game> gamea = games.get(chat);
		if(gamea == null) return null;
		for(Connect4Game game : gamea){
			if(game.getPlayer1().equals(p1) || game.getPlayer2().equals(p1)){
				return game;
			}
		}
		return null;
	}
	
	
	public void removeGame(String chat, Connect4Game game){
		ArrayList<Connect4Game> gamea = games.get(chat);
		gamea.remove(game);
	}
	
	
}
