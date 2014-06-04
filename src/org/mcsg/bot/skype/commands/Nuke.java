package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Bot;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Nuke implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		if("drew.foland".equalsIgnoreCase(sender.getId())){
			Bot.killnuke = false;
			StringBuilder sb = new StringBuilder();
			for(String str : args){
				sb.append(str).append(" ");
			}
			String msg = sb.toString();
			while(!Bot.killnuke){
				for(int a = 0; a < 100; a++){
				chat.send(msg);
				}
				try{ Thread.sleep (60000); } catch (Exception e){}
			}
		}
	}

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public String getUsage() {
		return null;
	}

}
