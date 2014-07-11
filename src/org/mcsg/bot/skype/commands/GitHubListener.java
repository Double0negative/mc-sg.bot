package org.mcsg.bot.skype.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mcsg.bot.skype.Permissions;
import org.mcsg.bot.skype.Settings;

import com.skype.Chat;
import com.skype.User;

public class GitHubListener implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		if(Permissions.hasPermission(sender, chat, "github_hook")){
			if(args.length > 0){
				HashMap<String, List<String>> map = Settings.Root.Github.github_update_chat;
				List<String> list = map.getOrDefault(args[0], new ArrayList<String>());
				list.add(chat.getId());
				map.put(args[0], list);
				
				
				
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
