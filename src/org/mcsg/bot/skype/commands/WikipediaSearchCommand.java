package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.web.WikipediaSearch;
import org.mcsg.bot.skype.web.WikipediaSearch.Search;
import org.mcsg.bot.skype.web.WikipediaSearch.WikiQuery;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class WikipediaSearchCommand implements SubCommand {

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		String search = StringUtils.implode(args);


		WikiQuery query = WikipediaSearch.search(search);

		Search[] result = query.query.search; 
		if(result.length > 0){
			ChatManager.chat(chat, sender, "http://en.wikipedia.org/wiki/"+result[0].title.replace(" ", "%20"));
			ChatManager.chat(chat,  sender,result[0].snippet);
		} else {
			ChatManager.chat(chat,  sender,"No wikipedia results found for \""+args[0]+"\"");
		}
	}

	@Override
	public String getHelp() {
		return "Search for a wikipage";
	}

	@Override
	public String getUsage() {
		return ".wiki <search>";
	}



	

}
