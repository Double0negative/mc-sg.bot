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
			ServerListPing17 pinger = new ServerListPing17();

			try{
				String server[] = args[0].split(":");
				long time = System.currentTimeMillis();
				pinger.setAddress(new InetSocketAddress(server[0], ((server.length > 1) ? Integer.parseInt(server[1]) : 25565 )));
				StatusResponse response = pinger.fetchData();
				ChatManager.chat(chat,"(" + args[0] + ") " + response.getDescription().replaceAll("(\u00A7([a-fk-or0-9]))", "").trim() + " - " + response.getVersion().getName() + " - " + response.getPlayers().getOnline() + "/" + response.getPlayers().getMax() + " players. Ping: "+ (System.currentTimeMillis() - time)+"ms");
				//ChatManager.chat(chat, pinger.getJson());
			} catch (Exception e){
				ChatManager.chat(chat, pinger.getJson());
				ChatManager.printThrowable(chat, e);

				ChatManager.chat(chat,"\"" + args[0]+"\" is not online or is not a Minecraft server.");
			}
		}
	}

	@Override
	public String getHelp() {
		return "Ping a Minecraft Server";
	}

	@Override
	public String getUsage() {
		return ".mcping <server:port>";
	}

}
