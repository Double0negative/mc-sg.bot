package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.search.Google;
import org.mcsg.bot.skype.search.Google.GoogleResult;
import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;

import com.skype.Chat;
import com.skype.User;

public class ImageSearch implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		GoogleResult result = Google.search("images", StringUtils.implode(args), 0);
		org.mcsg.bot.skype.search.Google.Results[] results = result.responseData.results;
		if(results != null && results.length > 0){
			ChatManager.chat(chat, results[0].url);
		} else {
			ChatManager.chat(chat, "No results for "+StringUtils.implode(args));
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
