package org.mcsg.bot.skype.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.mcsg.bot.skype.Bot;
import org.mcsg.bot.skype.Permissions;
import org.mcsg.bot.skype.util.FileUtils;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Stop implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		if(Permissions.hasPermission(sender, chat, "stop")){
			chat.send("Stopping...");
			FileUtils.writeFile(new File(Bot.LAST_FILE), chat.getId());			
			System.exit(0);
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
