package org.mcsg.bot.skype.web;

import org.mcsg.bot.skype.util.WebClient;

import com.google.gson.Gson;

public class DuckDuckGo {

	public static DuckDuckResult search(String search){
		String url = "http://api.duckduckgo.com/?q="+search+"&format=json&pretty=1";
		String json = WebClient.request(url);

		return new Gson().fromJson(json, DuckDuckResult.class);
	}



	public class DuckDuckResult{
		
		public String Definition;
		public String DefinitionSorce;
		public String Heading;
		public String AbstractSource;
		public String Image;
		public String AbstractText;
		public String AnswerType;
		public String Type;
		public String Redirect;
		public String DefinitionURL;
		public String Answer;
		public Results[] Results;
		public RelatedTopics[] RelatedTopics;

	}

	public class Results{
		public String Result;
		public Icon Icon;
		public String FirstURL;
		public String Text;
	}

	public class RelatedTopics{
		public String Result;
		public Icon Icon;
		public String FirstURL;
		public String Text;
	}

	public class Icon{
		public String URL;
		public String Height;
		public String Width;
	}
}
