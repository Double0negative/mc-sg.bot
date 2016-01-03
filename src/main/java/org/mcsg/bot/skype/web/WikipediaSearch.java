package org.mcsg.bot.skype.web;

import com.google.gson.Gson;

public class WikipediaSearch {

	public static WikiQuery search(String search) throws Exception{
		String url = "http://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="+search+"&srprop=&format=json";
		String src = WebClient.request(url);
		return new Gson().fromJson(src, WikiQuery.class);
	}
	
	public class WikiQuery{
		public Query query;
	}
	public class Query{
		public Search[] search;
	}
	public class Search{
		public int ns;
		public String size;
		public String title;
		public int wordcount;
		public String snippet;
	}
}
