package org.mcsg.bot.skype.web;

import org.mcsg.bot.skype.web.GistPaster.GistPaste.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class GistPaster {

	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static String paste(String msg) throws Exception{
//		System.out.println("Yesa?");
		GistPaste paste = new GistPaste();
	
		paste.files.botpaste.content = msg;
		
		return paste(paste);
	}
	
	public static String paste(GistPaste paste) throws Exception{
		String json = gson.toJson(paste,  GistPaste.class);
//		System.out.println("posted" + json);
		String json2 = WebClient.post("https://api.github.com/gists", json, null);
//		System.out.println("response" + json2);
		GistResponse response = gson.fromJson(json2, GistResponse.class);
		return response.html_url;
	}
	
	
	public static class GistPaste {
		public String description = "MC-SG.bot PASTE";
	    @SerializedName("public")
		public boolean isPublic = true;
		public Files files = new Files(); //should be using a map here but oh well, were only making one paste at a time
		
		public static class Files{
			public Paste botpaste = new Paste();
			
			public static class Paste {
				public String content;
			}
		}
	}
	
	public static class GistResponse {
		public String html_url;
	}
}
