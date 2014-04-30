package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.search.DuckDuckGo;
import org.mcsg.bot.skype.search.DuckDuckGo.DuckDuckResult;
import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Define implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
			DuckDuckResult result = DuckDuckGo.search(StringUtils.implode(args));
			String def = result.Definition;
			if(def != null && !def.equals("")){
				ChatManager.chat(chat, def);
				ChatManager.chat(chat, result.DefinitionURL);
			} else {
				ChatManager.chat(chat, "No definition for "+StringUtils.implode(args));
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
