package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.util.Permissions;
import org.mcsg.bot.skype.util.ShellCommand;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class ShellWrite implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		if(Permissions.hasPermission(sender, chat, "out")){
			StringBuilder sb = new StringBuilder();
			for(int a = 1; a < args.length; a ++){
				sb.append(args[a]).append(" ");
			}
			ShellCommand.writeStream(Integer.parseInt(args[0]), sb.toString());
		} else {
			chat.send("No permission");
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
