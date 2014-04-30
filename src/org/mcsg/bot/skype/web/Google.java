package org.mcsg.bot.skype.web;

import org.mcsg.bot.skype.util.WebClient;

import com.google.gson.Gson;

public class Google {

	public static GoogleResult search(String type, String search, int start){
		String url = "http://ajax.googleapis.com/ajax/services/search/"+type+"?v=2.0&safe=off&q="+search+"&start="+start;
		String json = WebClient.request(url);
		
		return new Gson().fromJson(json, GoogleResult.class);
		
	}
	
	
	public class GoogleResult{
		public ResponseData responseData;
	}
	public class ResponseData{
		public Results[] results;
	}
	public class Results{
		public String cacheUrl;
		public String content;
		public String title;
		public String titleNoFormatting;
		public String unescapedUrl;
		public String url;
		public String visibleUrl;
	}
	
}
