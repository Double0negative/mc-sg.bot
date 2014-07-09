package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.Permissions;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class GetUsers implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		if(Permissions.hasPermission(sender, chat, "getusers")){
			StringBuilder sb = new StringBuilder();
			for(User user : chat.getAllMembers()){
				if(args.length == 0){
					sb.append(user.getId()+":"+user.getFullName()+" ");
				} else {
					sb.append(user.getId() + " ");

				}
			}
			ChatManager.chat(chat, sb.toString());
		}
	}

	@Override
	public String getHelp() {
		return "Get all users in a chat";
	}

	@Override
	public String getUsage() {
		return ".getusers [-noid]";
	}

}
