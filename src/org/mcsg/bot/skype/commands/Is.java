package org.mcsg.bot.skype.commands;

import java.util.Random;

import org.mcsg.bot.skype.util.StringUtils;

import com.skype.Chat;
import com.skype.User;

public class Is implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		if(new Random(StringUtils.implode(args).hashCode()).nextBoolean()){
			chat.send("Yes");
		} else {
			chat.send("No");
		}
		
	}

	@Override
	public String getHelp() {
		return "Is it?";
	}

	@Override
	public String getUsage() {
		return ".is <query>";
	}

}
