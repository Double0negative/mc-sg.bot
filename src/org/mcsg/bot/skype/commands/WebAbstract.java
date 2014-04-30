package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.search.DuckDuckGo;
import org.mcsg.bot.skype.search.DuckDuckGo.DuckDuckResult;
import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class WebAbstract implements SubCommand {

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		DuckDuckResult result = DuckDuckGo.search(StringUtils.implode(args));
		String abstractt = result.AbstractText;
		if(abstractt != null && !abstractt.equals("")){
			ChatManager.chat(chat, abstractt);
			ChatManager.chat(chat, " -"+result.AbstractSource);
		} else {
			ChatManager.chat(chat, "No abstract for "+StringUtils.implode(args));
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
