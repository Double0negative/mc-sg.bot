package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.util.Permissions;
import org.mcsg.bot.skype.util.ShellCommand;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class ProcIn implements SubCommand {

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		if(Permissions.hasPermission(sender, chat, "proc")){
			if(args.length == 2){
				ShellCommand.quiteChat(chat, Integer.parseInt(args[0]));
			} else {
				ShellCommand.readToChat(chat, Integer.parseInt(args[0]));
			}
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
