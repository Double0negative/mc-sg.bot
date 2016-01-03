package org.mcsg.bot.skype.commands;

import java.util.HashMap;

import org.mcsg.bot.skype.Bot;
import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.games.Connect4Game;
import org.mcsg.bot.skype.games.Connect4Game.BoardFullException;
import org.mcsg.bot.skype.games.Connect4Game.ColumnFullException;
import org.mcsg.bot.skype.games.Connect4Game.IllegalColumnException;
import org.mcsg.bot.skype.games.Connect4Game.Tile;
import org.mcsg.bot.skype.games.Connect4Manager;
import org.mcsg.bot.skype.games.GameStatsManager;

import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.user.User;

public class Connect4 implements SubCommand {

    private HashMap<String, Connect4Game> games = new HashMap<String, Connect4Game>();

    @Override
    public void execute(Chat chat, User sender, String[] args) throws Exception {
        Connect4Game game = Connect4Manager.getInstance().getGame(chat.getIdentity(), sender.getUsername());
        if (game != null) {
            if (args[0].equalsIgnoreCase("leave")) {
                ChatManager.chat(chat, sender, "Left the game!");
                Connect4Manager.getInstance().removeGame(chat.getIdentity(), game);
                return;
            }
            int col = Integer.parseInt(args[0]) - 1;
            Tile[][] pretiles = game.getTiles().clone();
            if (!game.isMove(sender.getUsername())) {
                ChatManager.chat(chat, sender, "Not your move!");
                return;
            }
            boolean win = false;
            try {
                win = game.makeMove(game.getTile(sender.getUsername()), col);
            } catch (IllegalColumnException e) {
                ChatManager.chat(chat, sender, "Column doesn't exist!");
                return;
            } catch (ColumnFullException e) {
                ChatManager.chat(chat, sender, "Column is full!");
                return;
            } catch (BoardFullException e) {
                printGame(chat, game.getTiles(), game, true);
                ChatManager.chat(chat, "Draw! ");
                Connect4Manager.getInstance().removeGame(chat.getIdentity(), game);
                return;
            }
            animateGame(chat, pretiles, game, sender.getUsername(), win);
            if (win) {
                GameStatsManager.addGameResult("connect4", chat.getIdentity(), game.getPlayer1(), game.getPlayer2(),
                        sender.getUsername());
                Connect4Manager.getInstance().removeGame(chat.getIdentity(), game);
            } else {

                // Make AI move
                pretiles = game.getTiles();
                int aiStatus = game.doAiMove();
                if (aiStatus != -1) {
                    animateGame(chat, pretiles, game, Skype.getProfile().getId(), aiStatus == 1);
                    if (aiStatus == 1) {
                        GameStatsManager.addGameResult("connect4", chat.getIdentity(), game.getPlayer1(),
                                game.getPlayer2(), Skype.getProfile().getId());
                        Connect4Manager.getInstance().removeGame(chat.getIdentity(), game);
                    }
                }
            }
        } else {
            try {
                Integer.parseInt(args[0]);
                ChatManager.chat(chat, sender, "Not in a game!");
            } catch (Exception e) {
                if (sender.getUsername().equals(args[0])) {
                    chat.sendMessage("Cannot create game with self!");
                    return;
                }
                game = Connect4Manager.getInstance().createGame(chat, sender.getUsername(), args[0]);
                chat.sendMessage("Created Game");
                printGame(chat, game.getTiles(), game, false);
            }
        }
    }

    private void animateGame(Chat chat, Tile[][] tiles, Connect4Game game, String player, boolean won)
            throws SkypeException {
        tiles[game.getLastRow()][game.getLastCol()] = null;
        for (int row = 0; row <= game.getLastRow(); row++) {
            tiles[row][game.getLastCol()] = game.getTile(player);
            if (row > 0) {
                tiles[row - 1][game.getLastCol()] = null;
            }
            printGame(chat, tiles, game, won);
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
        if (!won)
            game.nextMove();

        printGame(chat, game.getTiles(), game, won);
    }

    private void printGame(Chat chat, Tile[][] tiles, Connect4Game game, boolean won) throws SkypeException {
        StringBuilder sb = new StringBuilder();
        StringBuilder border = new StringBuilder();

        sb.append(".\n    1  2  3  4  5  6  7\n   ");
        for (int a = 0; a < 26; a++) {
            border.append("-");
        }

        sb.append(border).append("\n");

        for (Tile[] row : tiles) {
            sb.append("   |");
            for (Tile tile : row) {
                if (tile == null) {
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
        if (!won)
            sb.append("Move: " + game.getMover());
        else
            sb.append("WINNER: " + game.getMover());

        ChatMessage msg = game.getMsg();
        int lastMessage = game.getLastMessage();

        if (msg != null && lastMessage + 10 > Bot.messageCount.get(chat)) {
            msg.setContent(sb.toString());
        } else {
            if (msg != null) {
                msg.setContent("");
            }
            game.setMsg(chat.sendMessage(sb.toString()));
            game.setLastMessage(Bot.messageCount.get(chat));
        }
    }

    @Override
    public String getUsage() {
        return "Start a game: .c4 <player | ai> \n Make move: .c4 <column>";
    }

    @Override
    public String getCommand() {
        return "c4";
    }

    @Override
    public String[] getAliases() {
        return a("connect4", "connect");
    }

    @Override
    public String getHelp() {
        return "Starts a Connect 4 game or makes a move in a game. Games can be played against other players or AI";
    }

}
