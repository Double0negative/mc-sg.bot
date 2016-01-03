package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.web.MinecraftServerPinger;
import org.mcsg.bot.skype.web.MinecraftServerPinger.MinecraftPing;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class MinecraftPingCommand implements SubCommand {

    @Override
    public void execute(Chat chat, User sender, String[] args) throws Exception {
        if (args.length != 1) {
            chat.sendMessage(".mcping <server>");
        } else {

            String server[] = args[0].split(":");
            MinecraftPing ping = null;
            if (server.length > 1) {
                ping = MinecraftServerPinger.ping(server[0], Integer.parseInt(server[1]));
            } else {
                ping = MinecraftServerPinger.ping(server[0]);
            }

            if (ping.status == false) {
                ChatManager.chat(chat, "Failed to ping server at " + args[0]);
            } else {
                ChatManager.chat(chat, "(" + args[0] + ") " + ping.motd.replace("  ", " ").trim() + " - "
                        + ping.version + " - " + ping.players.online + "/" + ping.players.max + " players. Ping: "
                        + ping.ping + "ms");
            }
        }
    }

    @Override
    public String getHelp() {
        return "Ping a Minecraft Server";
    }

    @Override
    public String getUsage() {
        return ".mcping <server[:port]>";
    }

    @Override
    public String getCommand() {
        return "mcping";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
