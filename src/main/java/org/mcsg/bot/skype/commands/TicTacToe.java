package org.mcsg.bot.skype.commands;

import java.util.HashMap;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.games.GameStatsManager;
import org.mcsg.bot.skype.games.TicTacToeGame;
import org.mcsg.bot.skype.games.TicTacToeGame.BoardFullException;
import org.mcsg.bot.skype.games.TicTacToeGame.IllegalTileException;
import org.mcsg.bot.skype.games.TicTacToeGame.Tile;
import org.mcsg.bot.skype.games.TicTacToeGame.TileIsFilledException;
import org.mcsg.bot.skype.games.TicTacToeManager;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.user.User;

public class TicTacToe implements SubCommand {
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
        TicTacToeGame game = TicTacToeManager.getInstance().getGame(chat.getIdentity(), sender.getUsername());
        if (game != null) {
            int col = Integer.parseInt(args[1]) - 1;
            int row = rowMap.get(args[0]);
            if (!game.isMove(sender.getUsername())) {
                ChatManager.chat(chat, sender, "Not your move!");
                return;
            }
            boolean win = false;
            try {
                win = game.makeMove(game.getTile(sender.getUsername()), row, col);
            } catch (IllegalTileException e) {
                ChatManager.chat(chat, sender, "Column doesn't exist!");
            } catch (TileIsFilledException e) {
                ChatManager.chat(chat, sender, "Tile is already filled!");
            } catch (BoardFullException e) {
                printGame(chat, game, true);
                ChatManager.chat(chat, "Draw! ");
                TicTacToeManager.getInstance().removeGame(chat.getIdentity(), game);
                return;
            }
            printGame(chat, game, win);
            if (win) {
                GameStatsManager.addGameResult("tictactoe", chat.getIdentity(), game.getPlayer1(), game.getPlayer2(),
                        sender.getUsername());

                chat.sendMessage(sender.getUsername() + " WINS!!!!!!!");
                TicTacToeManager.getInstance().removeGame(chat.getIdentity(), game);
            }
        } else {
            try {
                Integer.parseInt(args[0]);
                ChatManager.chat(chat, sender, "Not in a game!");
            } catch (Exception e) {
                game = TicTacToeManager.getInstance().createGame(chat.getIdentity(), sender.getUsername(), args[0]);
                chat.sendMessage("Created Game");
                printGame(chat, game, false);
            }
        }
    }

    private void printGame(Chat chat, TicTacToeGame game, boolean won) throws SkypeException {
        StringBuilder sb = new StringBuilder();
        StringBuilder border = new StringBuilder();

        sb.append(".\n    1  2  3\n");
        // for(int a = 0; a < 15; a++){
        // border.append("-");
        // }
        //
        // sb.append(border).append("\n");

        Tile[][] tiles = game.getTiles();
        char[] chars = new char[] { 'A', 'B', 'C' };
        int a = 0;
        for (Tile[] row : tiles) {
            sb.append(" ").append(chars[a]).append(" |");
            a++;
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
        // sb.append("   ").append(border);

        sb.append("\n");
        if (!won)
            sb.append("Move: " + game.getMover());
        chat.sendMessage(sb.toString());
    }

    @Override
    public String getHelp() {
        return "Starts or makes a move in a t3 game";
    }

    @Override
    public String getUsage() {
        return ".t3 player, .t3 move";
    }

    @Override
    public String getCommand() {
        return "t3";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
