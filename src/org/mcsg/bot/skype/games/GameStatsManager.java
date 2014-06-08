package org.mcsg.bot.skype.games;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.mcsg.bot.skype.games.GameStatsManager.GameStats.Chat;
import org.mcsg.bot.skype.games.GameStatsManager.GameStats.Chat.Game;
import org.mcsg.bot.skype.games.GameStatsManager.GameStats.Chat.Player;
import org.mcsg.bot.skype.util.FileUtils;

import com.google.gson.Gson;

public class GameStatsManager {

	private static Gson gson = new Gson();
	private static File folder = new File("stats/");
	
	private static HashMap<String, GameStats> stats = new HashMap<String, GameStats>();
	
	public static GameStats getStat(String game) throws IOException{
		if(stats.containsKey(game)) return stats.get(game);
		File file = new File(folder, "stat_"+game+".json");
		String json = FileUtils.readFile(file);
		GameStats stat = gson.fromJson(json, GameStats.class);
		stats.put(game, stat);
		return stat;
	}
	
	public static void saveStat(GameStats stat) throws IOException{
		stats.put(stat.game_name, stat);
		File file = new File(folder, "stat_"+stat.game_name+".json");
		FileUtils.writeFile(file, gson.toJson(stat, stat.getClass()));
	}
	
	public static void addGameResult(String game, String chat, String player1, String player2, String winner) throws IOException{
		GameStats stat = getStat(game);
		if(stat == null){
			stat = new GameStats();
		}
		if(stat.game_name == null){
			stat.game_name = game;
		}
		Chat global = stat.chats.getOrDefault("__global__", new Chat());
		Chat local  = stat.chats.getOrDefault(chat, new Chat());
		
		addGameResult0(global, local, player1, player2, winner);
		
		stat.chats.put(chat, local);
		stat.chats.put("__global__", global);
		saveStat(stat);
	}
	
	private static void addGameResult0(Chat global, Chat local, String player1, String player2, String winner){
		Game game = new Game();
		game.player1 = player1;
		game.player2 = player2;
		game.winnerno = (winner.equals(player1))? 1: 2;
		game.winner = winner;
		
		Player lPlayer1 = local.players.getOrDefault(player1, new Player(player1));
		Player lPlayer2 = local.players.getOrDefault(player2, new Player(player2));
		
		Player gPlayer1 = global.players.getOrDefault(player1, new Player(player1));
		Player gPlayer2 = global.players.getOrDefault(player2, new Player(player2));
		
		doWinLose(local, lPlayer1, winner);
		doWinLose(local, lPlayer2, winner);
		doWinLose(global, gPlayer1, winner);
		doWinLose(global, gPlayer2, winner);
		
		global.games.add(game);
		local.games.add(game);
		
	}
	
	private static void doWinLose(Chat chat, Player player, String winner){
		if(player.name.equals(winner)){
			player.win += 1;
		} else {
			player.loss +=1;
		}
		chat.players.put(player.name, player);
	}
	
	public static class GameStats {
		public String game_name;
		public HashMap<String, Chat> chats = new HashMap<>();
		
		public static class Chat {
			public ArrayList<Game> games = new ArrayList<Game>();
			public HashMap<String, Player> players = new HashMap<String, Player>();
			
			
			public static class Game {
				public String player1;
				public String player2;
				public String winner;
				public int winnerno;
			}
			
			public static class Player {
				public Player(){ }
				public Player(String name){
					this.name = name;
				}
				public String name;
				public int win, loss;
			}
		}
	}
	
}
