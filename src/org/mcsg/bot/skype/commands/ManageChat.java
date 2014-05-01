package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.Permissions;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class ManageChat implements SubCommand {

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		if(Permissions.hasPermission(sender, chat, "chatmanage")){
			if(args[0].equalsIgnoreCase("sec") || args[0].equalsIgnoreCase("seconds")){
				ChatManager.setSeconds(Integer.parseInt(args[1]));
			} else if (args[0].equalsIgnoreCase("paste")){
				ChatManager.setPaste(Integer.parseInt(args[1]));
			}
			chat.send("Set settings");
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
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
