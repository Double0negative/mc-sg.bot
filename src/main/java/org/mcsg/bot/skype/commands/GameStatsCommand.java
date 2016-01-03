package org.mcsg.bot.skype.commands;

import java.util.HashMap;

import org.mcsg.bot.skype.games.GameStatsManager;
import org.mcsg.bot.skype.games.GameStatsManager.GameStats;
import org.mcsg.bot.skype.games.GameStatsManager.GameStats.Chat.Game;
import org.mcsg.bot.skype.games.GameStatsManager.GameStats.Chat.Player;
import org.mcsg.bot.skype.util.Arguments;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class GameStatsCommand implements SubCommand {

    @Override
    public void execute(Chat chat, User sender, String[] args) throws Exception {
        Arguments arge = new Arguments(args, "global/g", "opponent/o", "list/l", "player/p");
        HashMap<String, String> swi = arge.getSwitches();
        args = arge.getArgs();

        String player1 = sender.getUsername();
        player1 = swi.containsKey("player") ? swi.get("player") : player1;
        String gamename = args[0];

        GameStats stats = GameStatsManager.getStat(gamename);
        org.mcsg.bot.skype.games.GameStatsManager.GameStats.Chat gchat = swi.containsKey("global") ? stats.chats
                .get("__global__") : stats.chats.get(chat.getIdentity());

        if (swi.containsKey("opponent")) {
            StringBuilder sb = new StringBuilder();

            String player2 = swi.get("opponent");
            double p1w = 0;
            double p2w = 0;
            for (Game game : gchat.games) {
                if ((game.player1.equals(player1) && game.player2.equals(player2)) || game.player2.equals(player1)
                        && game.player1.equals(player2)) {

                    String player11 = game.player1;
                    String player22 = game.player2;

                    sb.append("  ").append(game.winnerno == 1 ? ">" : " ").append(player11);
                    sb.append("\t").append(game.winnerno == 2 ? ">" : " ").append(player22);
                    sb.append("\n");

                }
            }
            sb.append("   ").append(player1).append(" ").append(Math.round((p1w / (p1w + p2w)) * 100)).append("%");
            sb.append("\t").append(player2).append(" ").append(Math.round(((p2w / p1w * p2w)) * 100)).append("%");
            sb.append("\n");
            chat.sendMessage(sb.toString());

        } else if (swi.containsKey("list")) {
            StringBuilder sb = new StringBuilder();
            sb.append(".\nPlayer stats for " + player1 + " in game " + gamename + "\n");

            for (Game game : gchat.games) {

                if (game.player1.equals(player1) || game.player2.equals(player1)) {

                    String player11 = game.player1;
                    String player22 = game.player2;

                    sb.append("  ").append(game.winnerno == 1 ? ">" : " ").append(player11);
                    sb.append("\t").append(game.winnerno == 2 ? ">" : " ").append(player22);
                    sb.append("\n");
                }
            }

            Player player = gchat.players.get(player1);
            sb.append("   Wins: " + player.win + "   Losses: " + player.loss + " W/L Ratio: "
                    + Math.round((player.win / (player.win + player.loss) * 100)) + "%");
            chat.sendMessage(sb.toString());
        } else {

            Player player = gchat.players.get(player1);
            chat.sendMessage("Player stats for " + player1 + " in game " + gamename);
            chat.sendMessage("   Wins: " + player.win + "   Losses: " + player.loss + " W/L Ratio: "
                    + Math.round((player.win / (player.win + player.loss) * 100)) + "%");

        }

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

    @Override
    public String getCommand() {
        return "stats";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
