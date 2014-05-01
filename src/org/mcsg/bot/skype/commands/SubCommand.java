package org.mcsg.bot.skype.commands;

import com.skype.Chat;
import com.skype.User;

public interface SubCommand {

	public void execute(Chat chat, User sender, String [] args) throws Exception;
	
	public String getHelp();
	
	public String getUsage();
}
