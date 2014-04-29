package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.util.Permissions;
import org.mcsg.bot.skype.util.ShellCommand;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Shell implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		if(Permissions.hasPermission(sender, chat, "sh")){
			StringBuilder sb = new StringBuilder();
			for(String arg : args){
				sb.append(arg).append(" ");
			}
			ShellCommand.exec(chat, sb.toString());
			//ShellCommand.exec(chat, args);
		} else {
			chat.send("No permissions to execute command");
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
