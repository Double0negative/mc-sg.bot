package org.mcsg.bot.skype.games;

import java.util.ArrayList;
import java.util.HashMap;

import org.mcsg.bot.skype.games.TicTacToeGame.Tile;

public class TicTacToeManager {
	private static TicTacToeManager instance = new TicTacToeManager();
	
	private HashMap<String, ArrayList<TicTacToeGame>> games = new HashMap<String, ArrayList<TicTacToeGame>>();
	
	private TicTacToeManager(){
		
	}
	
	public static TicTacToeManager getInstance(){
		return instance;
	}
	
	public TicTacToeGame createGame(String chat, String p1, String p2){
		TicTacToeGame c4 = new TicTacToeGame(p1, p2, Tile.SQUARE, Tile.CIRCLE);
		ArrayList<TicTacToeGame> gamea = games.get(chat);
		if(gamea == null){
			gamea = new ArrayList<TicTacToeGame>();
		}
		gamea.add(c4);
		games.put(chat, gamea);
		return c4;		
	}
	
	public TicTacToeGame getGame(String chat, String p1){
		ArrayList<TicTacToeGame> gamea = games.get(chat);
		if(gamea == null) return null;
		for(TicTacToeGame game : gamea){
			if(game.getPlayer1().equals(p1) || game.getPlayer2().equals(p1)){
				return game;
			}
		}
		return null;
	}
	
	
	public void removeGame(String chat, TicTacToeGame game){
		ArrayList<TicTacToeGame> gamea = games.get(chat);
		gamea.remove(game);
	}
	
}
