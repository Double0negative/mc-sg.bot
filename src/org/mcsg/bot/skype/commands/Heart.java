package org.mcsg.bot.skype.commands;


import java.util.Random;

import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.web.Google;
import org.mcsg.bot.skype.web.Google.GoogleResult;
import org.mcsg.bot.skype.web.Google.Results;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Heart implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		GoogleResult result = Google.search("images", "heart", new Random().nextInt(60));
		Results[] results = result.responseData.results;
		if(results != null && results.length > 0){
			ChatManager.chat(chat, results[0].url);
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
