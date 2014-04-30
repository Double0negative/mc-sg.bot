package org.mcsg.bot.skype.commands;

import java.net.InetSocketAddress;

import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.ServerListPing17;
import org.mcsg.bot.skype.util.ServerListPing17.StatusResponse;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class MinecraftPingCommand implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args) throws SkypeException {
		if(args.length != 1){
			chat.send(".mcping <server>");
		}
		else{
			try{
				String server[] = args[0].split(":");
				long time = System.currentTimeMillis();
				ServerListPing17 pinger = new ServerListPing17();
				pinger.setAddress(new InetSocketAddress(server[0], ((server.length > 1) ? 25565 : Integer.parseInt(server[1]))));
				StatusResponse response = pinger.fetchData();
				ChatManager.chat(chat,"(" + args[0] + ") " + response.getDescription() + " - " + response.getVersion().getName() + " - " + response.getPlayers().getOnline() + "/" + response.getPlayers().getMax() + " players. Ping: "+ (System.currentTimeMillis() - time)+"ms");
			} catch (Exception e){
				e.printStackTrace();
				ChatManager.chat(chat, args[0]+" is not online or is not a Minecraft server.");
			}
		}
	}

	@Override
	public String getHelp() {
		return "Ping a Minecraft Server";
	}

	@Override
	public String getName() {
		return "Minecraft ping";
	}

}
