package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.util.Permissions;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Perm implements SubCommand {

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		if(sender.getId().equals("drew.foland")){
			if(args[0].equals("add")){
				Permissions.addPerm(args[1], chat.getId(), args[2]);
				chat.send("Adding permission");
			} else 	if(args[0].equals("all")){
				for(User user : chat.getAllMembers()){
					Permissions.addPerm(user, chat, args[1]);
				}
				chat.send("Adding permission");
			}	else {
				Permissions.removePerms(args[1], chat.getId(), args[2]);
				chat.send("Removing permission");
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
