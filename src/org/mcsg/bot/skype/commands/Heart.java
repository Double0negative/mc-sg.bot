package org.mcsg.bot.skype.commands;


import java.util.Random;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Heart implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
	/*	BingSearchServiceClientFactory factory = BingSearchServiceClientFactory.newInstance();
		BingSearchClient client = factory.createBingSearchClient();

		SearchRequestBuilder builder = client.newSearchRequestBuilder();
		builder.withAppId("2db1c46390e84044a84201af5d8cd2fe");
		builder.withQuery("heart");
		builder.withSourceType(SourceType.IMAGE);
		builder.withVersion("2.0");
		builder.withMarket("en-us");
		builder.withAdultOption(AdultOption.OFF);

		builder.withWebRequestCount(100L);
		builder.withWebRequestOffset(0L);
		builder.withWebRequestSearchOption(WebSearchOption.DISABLE_HOST_COLLAPSING);
		builder.withWebRequestSearchOption(WebSearchOption.DISABLE_QUERY_ALTERATIONS);

		SearchResponse response = client.search(builder.getResult());

		int rand = new Random().nextInt(response.getImage().getResults().size());
		
		chat.send(response.getImage().getResults().get(rand).getUrl());
		*/
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
