package org.mcsg.bot.skype.commands;

import java.util.ArrayList;
import java.util.List;

import org.mcsg.bot.skype.util.Permissions;
import org.mcsg.bot.skype.util.Settings;

import com.skype.Chat;
import com.skype.User;

public class GitHubListener implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		if(Permissions.hasPermission(sender, chat, "github_hook")){
			if(args.length > 0){
				List<String> list = Settings.Root.Github.github_update_chat.getOrDefault(args[0], new ArrayList<String>());
				list.add(chat.getId());
				Settings.save();
				chat.send("Added chat to Github Listener");
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
