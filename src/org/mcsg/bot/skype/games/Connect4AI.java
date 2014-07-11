package org.mcsg.bot.skype.games;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.games.Connect4Game.BoardFullException;
import org.mcsg.bot.skype.games.Connect4Game.ColumnFullException;
import org.mcsg.bot.skype.games.Connect4Game.IllegalColumnException;
import org.mcsg.bot.skype.games.Connect4Game.Tile;
import org.mcsg.bot.skype.util.ArrayUtil;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class Connect4AI {

	private static final int MAX_DEPTH = 5;
	private static final int MAX_VALUE = 5;

	private static final int [][] SCORE = new int [][]{
		new int [] { 1,2,3,4,3,2,1},
		new int [] { 2,3,4,5,4,3,2},
		new int [] { 3,4,5,6,5,4,3},
		new int [] { 3,4,5,6,5,4,3},
		new int [] { 1,2,3,4,3,2,1},
		new int [] { 0,1,2,3,2,1,0}
	};


	private Tile side;
	private Connect4Game game;
	private Chat chat;

	public Connect4AI(Chat chat, Tile side, Connect4Game game){
		this.chat = chat;
		this.side = side;
		this.game = game;
	}

	private int getScore(Tile[][] tiles){
		int score = 0;
		for(int row = 0; row < Connect4Game.ROWS; row++){
			for(int col = 0; col < Connect4Game.COLS; col++){
				if(tiles[row][col] == side){
					score -= SCORE[row][col];
				} else if(tiles[row][col] != null){
					score += SCORE[row][col];
				}
			}
		}
		return score;
	}

	private Tile getOtherTile(Tile tile){
		return (tile == Tile.CIRCLE) ? Tile.SQUARE : Tile.CIRCLE;
	}

	public boolean makeMove(){

		TreeMap<Integer, Integer> results = new TreeMap<Integer, Integer>();
		Tile[][] current = ArrayUtil.clone2DArray(game.getTiles());

		for(int a = 0; a < Connect4Game.COLS; a++){
			int row = game.getFirstRow(current, a);		
			current[row][a] = side;

			if(row != -1){
				int value = alphabet(current, a, getOtherTile(side), -MAX_VALUE, MAX_VALUE, MAX_DEPTH);

				if(!results.containsKey(value))
					results.put(value, a);
			}
			current[row][a] = null;

		}
		int move = results.firstEntry().getValue();
		try {
			chat.send(".c4 "+(move+1));
			for(Entry<Integer, Integer> ent: results.entrySet()){
				//				chat.send(ent.getValue()+":" +ent.getKey()+"  ");
			}
		} catch (SkypeException e1) {
			e1.printStackTrace();
		}
		try {
			return game.makeMove(side, move); //since the Skype API doesn't detect self-message for some reason
		} catch ( IllegalColumnException | ColumnFullException | BoardFullException e) {
			e.printStackTrace();
		}
		return false;
	}


	private int alphabet(Tile[][] tiles, int col,  Tile player, int alpha, int beta, int depth){
		Tile winner = game.checkForVictory(tiles);
		//		ChatManager.chat(chat, "Col: "+col);
		//		for(Tile[] row : tiles)
		//			ChatManager.chat(chat, Arrays.toString(row));

		if(winner == side)
			return -MAX_VALUE;
		else if (winner != null)
			return MAX_VALUE;


		if (depth <= 0){
			//			ChatManager.chat(chat, "Score: "+getScore(tiles));
			return getScore(tiles);
		}


		if(player != side){
			for(int a = 0; a < Connect4Game.COLS; a++){
				int row = game.getFirstRow(tiles, a);		
				if(row != -1){
					tiles[row][a] = player;
					alpha = Math.max(alpha, alphabet(tiles, a, getOtherTile(player), alpha, beta, depth - 1));
					tiles[row][a] = null;

					if(alpha >= beta){
						return alpha;
					}

				}
			}
			return alpha;
		} else {
			for(int a = 0; a < Connect4Game.COLS; a++){
				int row = game.getFirstRow(tiles, a);		
				if(row != -1){
					tiles[row][a] = player;
					beta = Math.min(beta, alphabet(tiles, a, getOtherTile(player), alpha, beta, depth - 1));
					tiles[row][a] = null;

					if(alpha >= beta ){
						return beta;
					}

				}
			}
			return beta;
		}
	}




}
