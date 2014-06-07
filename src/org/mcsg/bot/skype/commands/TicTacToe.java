package org.mcsg.bot.skype.commands;

import java.util.HashMap;

import org.mcsg.bot.skype.games.TicTacToeGame;
import org.mcsg.bot.skype.games.TicTacToeGame.IllegalTileException;
import org.mcsg.bot.skype.games.TicTacToeManager;
import org.mcsg.bot.skype.games.TicTacToeGame.BoardFullException;
import org.mcsg.bot.skype.games.TicTacToeGame.Tile;
import org.mcsg.bot.skype.games.TicTacToeGame.TileIsFilledException;
import org.mcsg.bot.skype.util.ChatManager;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class TicTacToe implements SubCommand{
	private HashMap<String, TicTacToeGame> games = new HashMap<String, TicTacToeGame>();
	private HashMap<String, Integer> rowMap = new HashMap<String, Integer>();
	{
		rowMap.put("a", 0);
		rowMap.put("A", 0);
		rowMap.put("b", 1);
		rowMap.put("B", 1);
		rowMap.put("c", 2);
		rowMap.put("C", 2);
	}
	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		TicTacToeGame game = TicTacToeManager.getInstance().getGame(chat.getId(), sender.getId());
		if(game != null){
			int col = Integer.parseInt(args[1]) -1;
			int row = rowMap.get(args[0]);
			if(!game.isMove(sender.getId())){
				ChatManager.chat(chat, sender, "Not your move!");
				return;
			}
			boolean win = false;
			try{
				win = game.makeMove(game.getTile(sender.getId()), row, col);
			}catch (IllegalTileException e){
				ChatManager.chat(chat, sender, "Column doesn't exist!");
			} catch(TileIsFilledException e){
				ChatManager.chat(chat, sender, "Tile is already filled!");
			} catch (BoardFullException e){
				printGame(chat, game, true);
				ChatManager.chat(chat, "Draw! ");
				TicTacToeManager.getInstance().removeGame(chat.getId(), game);
				return;
			}
			printGame(chat, game, win);
			if (win){
				chat.send( sender.getId() +" WINS!!!!!!!");
				TicTacToeManager.getInstance().removeGame(chat.getId(), game);
			}
		} else {
			try{ 
				Integer.parseInt(args[0]);
				ChatManager.chat(chat, sender, "Not in a game!");
			} catch (Exception e){
				game = TicTacToeManager.getInstance().createGame(chat.getId(), sender.getId(), args[0]);
				chat.send("Created Game");
				printGame(chat, game, false);
			}
		}
	}

	private void printGame(Chat chat, TicTacToeGame game, boolean won) throws SkypeException{
		StringBuilder sb = new StringBuilder();
		StringBuilder border = new StringBuilder();

		sb.append(".\n    1  2  3\n   ");
		for(int a = 0; a < 15; a++){ 
			border.append("-");
		}

		sb.append(border).append("\n");

		Tile[][] tiles  = game.getTiles();
		char[] chars = new char[]{'A', 'B', 'C'};
		int a = 0;
		for(Tile[] row : tiles){
			sb.append(" ").append(chars[a]).append(" |");
			a++;			
			for(Tile tile : row){
				if(tile == null){
					sb.append("   ");
				} else {
					sb.append(tile.getSymbol());
				}
				sb.append("|");
			}
			sb.append("\n");
		}
		sb.append("   ").append(border);

		sb.append("\n");
		if(!won) sb.append("Move: "+game.getMover());
		chat.send(sb.toString());
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}


}