package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.search.DuckDuckGo;
import org.mcsg.bot.skype.search.DuckDuckGo.DuckDuckResult;
import org.mcsg.bot.skype.search.DuckDuckGo.RelatedTopics;
import org.mcsg.bot.skype.search.DuckDuckGo.Results;
import org.mcsg.bot.skype.search.Google;
import org.mcsg.bot.skype.search.Google.GoogleResult;
import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class WebSearch implements SubCommand {

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		if(args[0].equalsIgnoreCase("-duck") || args[0].equalsIgnoreCase("-duckduckgo")){
			DuckDuckResult result = DuckDuckGo.search(StringUtils.implode(args));
			Results[] results = result.Results;
			RelatedTopics[] related = result.RelatedTopics;

			if(results != null && results.length > 0){
				ChatManager.chat(chat, results[0].FirstURL);
				ChatManager.chat(chat, results[0].Text);

			} else if (related != null && related.length > 0){
				ChatManager.chat(chat, "Related: "+related[0].FirstURL);
			} else {
				ChatManager.chat(chat, "No results");
			}
		} else {
			GoogleResult result = Google.search("web", StringUtils.implode(args), 0);
			org.mcsg.bot.skype.search.Google.Results[] results = result.responseData.results;
			if(results != null && results.length > 0){
				ChatManager.chat(chat, results[0].url);
				ChatManager.chat(chat, results[0].titleNoFormatting);
			} else {
				ChatManager.chat(chat, "No results for "+StringUtils.implode(args));
			}
		}
	}

	@Override
	public String getHelp() {
		return "<search> - Search the web";
	}

	@Override
	public String getName() {
		return "search";
	}

}
