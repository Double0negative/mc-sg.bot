package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Permissions;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Kick implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		if(Permissions.hasPermission(sender, chat, "kick")){
			for(String arg : args){
				chat.send("/kick "+arg);
			}
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

}
