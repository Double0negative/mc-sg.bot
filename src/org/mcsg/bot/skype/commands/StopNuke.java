package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Bot;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class StopNuke implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		if("drew.foland".equalsIgnoreCase(sender.getId())){
			Bot.killnuke = true;
		}
		
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
