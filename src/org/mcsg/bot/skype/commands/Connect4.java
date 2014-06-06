package org.mcsg.bot.skype.commands;

import java.util.HashMap;

import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.Connect4Game;
import org.mcsg.bot.skype.util.Connect4Game.Tile;
import org.mcsg.bot.skype.util.Connect4Manager;

import com.skype.Chat;
import com.skype.User;

public class Connect4 implements SubCommand{

	private HashMap<String, Connect4Game> games = new HashMap<String, Connect4Game>();

	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		Connect4Game game = Connect4Manager.getInstance().getGame(chat.getId(), sender.getId());
		if(game != null){
			int col = Integer.parseInt(args[0]) -1;
			if(!game.isMove(sender.getId())){
				ChatManager.chat(chat, sender, "Not your move!");
				return;
			}
			boolean win = game.makeMove(game.getTile(sender.getId()), col);


			printGame(chat, game, win);
			if (win){
				ChatManager.chat(chat, sender.getId() +" WINS!!!!!!!");
				Connect4Manager.getInstance().removeGame(chat.getId(), game);
			}
		} else {
			try{ 
				Integer.parseInt(args[0]);
				ChatManager.chat(chat, sender, "Not in a game!");
			} catch (Exception e){
				game = Connect4Manager.getInstance().createGame(chat.getId(), sender.getId(), args[0]);
				ChatManager.chat(chat, "Created Game");
				printGame(chat, game, false);
			}
		}
	}

	private void printGame(Chat chat, Connect4Game game, boolean won){
		StringBuilder sb = new StringBuilder();
		StringBuilder border = new StringBuilder();

		sb.append(".\n    1  2  3  4  5  6  7\n   ");
		for(int a = 0; a < 26; a++){
			border.append("-");
		}

		sb.append(border).append("\n");

		Tile[][] tiles  = game.getTiles();
		for(Tile[] row : tiles){
			sb.append("   |");
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
		ChatManager.chat(chat, sb.toString());
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
