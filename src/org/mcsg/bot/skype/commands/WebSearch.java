package org.mcsg.bot.skype.commands;

import java.util.Random;

import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.web.DuckDuckGo;
import org.mcsg.bot.skype.web.Google;
import org.mcsg.bot.skype.web.DuckDuckGo.DuckDuckResult;
import org.mcsg.bot.skype.web.DuckDuckGo.RelatedTopics;
import org.mcsg.bot.skype.web.DuckDuckGo.Results;
import org.mcsg.bot.skype.web.Google.GoogleResult;

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
			String search = "";
			int start = 0;
			if(args[0].equals("-rand")){
				search = StringUtils.implode(1, args);
				start = new Random().nextInt(50);
			}else if(args[0].equals("-result") || args[0].equals("-start") || args[0].equals("-num")){
				search = StringUtils.implode(2, args);
				start = Integer.parseInt(args[1]) - 1;
			}else {
				search = StringUtils.implode(args);
			}
			GoogleResult result = Google.search("web", search, start);
			org.mcsg.bot.skype.web.Google.Results[] results = result.responseData.results;
			if(results != null && results.length > 0){
				ChatManager.chat(chat, start+1+". "+results[0].unescapedUrl);
				ChatManager.chat(chat, results[0].titleNoFormatting);
			} else {
				ChatManager.chat(chat, "No results for "+StringUtils.implode(args));
			}
		}
	}

	@Override
	public String getHelp() {
		return "Search the web";
	}

	@Override
	public String getUsage() {
		return ".search <search>";
	}

}
