package org.mcsg.bot.skype.games;

import java.util.ArrayList;
import java.util.HashMap;

import org.mcsg.bot.skype.games.Connect4Game.Tile;

import com.samczsun.skype4j.chat.Chat;

public class Connect4Manager {

    private static Connect4Manager instance = new Connect4Manager();

    private HashMap<String, ArrayList<Connect4Game>> games = new HashMap<String, ArrayList<Connect4Game>>();
    private HashMap<String, Connect4AI> c4_ai = new HashMap<>();

    private Connect4Manager() {
        registerAI("ai", new C4MiniMaxAI());
    }

    public static Connect4Manager getInstance() {
        return instance;
    }

    public void registerAI(String name, Connect4AI ai) {
        c4_ai.put(name, ai);
    }

    public void unregisterAI(String name) {
        c4_ai.remove(name);
    }

    public Connect4AI getAI(String name, Chat chat, Tile side, Connect4Game game) {
        return c4_ai.get(name).load(chat, side, game);
    }

    public Connect4Game createGame(Chat chat, String p1, String p2) {
        Connect4Game c4 = new Connect4Game(chat, p1, p2, Tile.SQUARE, Tile.CIRCLE);
        ArrayList<Connect4Game> gamea = games.get(chat.getIdentity());
        if (gamea == null) {
            gamea = new ArrayList<Connect4Game>();
        }
        gamea.add(c4);
        games.put(chat.getIdentity(), gamea);
        return c4;
    }

    public Connect4Game getGame(String chat, String p1) {
        ArrayList<Connect4Game> gamea = games.get(chat);
        if (gamea == null)
            return null;
        for (Connect4Game game : gamea) {
            if (game.getPlayer1().equals(p1) || game.getPlayer2().equals(p1)) {
                return game;
            }
        }
        return null;
    }

    public void removeGame(String chat, Connect4Game game) {
        ArrayList<Connect4Game> gamea = games.get(chat);
        gamea.remove(game);
    }

}
