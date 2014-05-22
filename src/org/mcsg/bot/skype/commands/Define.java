package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.web.DuckDuckGo;
import org.mcsg.bot.skype.web.DuckDuckGo.DuckDuckResult;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Define implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
			DuckDuckResult result = DuckDuckGo.search(StringUtils.implode(args));
			String def = result.Definition;
			if(def != null && !def.equals("")){
				ChatManager.chat(chat, sender, def);
				ChatManager.chat(chat, sender, result.DefinitionURL);
			} else {
				ChatManager.chat(chat, sender, "No definition for "+StringUtils.implode(args));
			}
	}

	@Override
	public String getHelp() {
		return "Define a word";
	}

	@Override
	public String getUsage() {
		return ".define <word>";
	}

}
