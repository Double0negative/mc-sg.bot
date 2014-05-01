package org.mcsg.bot.skype.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.mcsg.bot.skype.Bot;
import org.mcsg.bot.skype.util.Permissions;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Stop implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		if(Permissions.hasPermission(sender, chat, "stop")){
			chat.send("Stopping...");
			
			File file = new File(Bot.LAST_FILE);
			PrintWriter pw;
			try {
				pw = new PrintWriter(new FileWriter(file));
				pw.println(chat.getId());
				pw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
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
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
