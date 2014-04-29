package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.util.Permissions;
import org.mcsg.bot.skype.util.ShellCommand;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class KillProc implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		if(Permissions.hasPermission(sender, chat, "killproc")){
			if(args.length == 2){
				ShellCommand.forceKill(chat, Integer.parseInt(args[0]));
			} else {
				ShellCommand.kill(chat, Integer.parseInt(args[0]));
			}
			chat.send("Killed");
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
