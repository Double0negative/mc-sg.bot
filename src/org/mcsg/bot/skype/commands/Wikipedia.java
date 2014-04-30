package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.util.WebClient;

import com.google.gson.Gson;
import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Wikipedia implements SubCommand {

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		String search = StringUtils.implode(args).replace(" ", "%20");
		String url = "http://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="+search+"&srprop=&format=json";
		String src = WebClient.request(url);

		WikiQuery query = new Gson().fromJson(src, WikiQuery.class);

		Search[] result = query.query.search; 
		if(result.length > 0){
			ChatManager.chat(chat, "http://en.wikipedia.org/wiki/"+result[0].title.replace(" ", "%20"));
		} else {
			ChatManager.chat(chat, "No wikipedia results found for \""+args[0]+"\"");
		}
	}

	@Override
	public String getHelp() {
		return "<search> - Search for a wikipage";
	}

	@Override
	public String getName() {
		return "wiki";
	}



	class WikiQuery{
		Query query;
	}
	class Query{
		Search[] search;
	}
	class Search{
		int ns;
		String title;
	}

}
