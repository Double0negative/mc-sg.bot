package org.mcsg.bot.skype.web;

import java.util.HashMap;

import org.mcsg.bot.skype.web.GistPaster.GistPaste.Paste;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class GistPaster {

	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static String paste(String msg) throws Exception {
		return paste("file.txt", msg);
	}
	
	public static String paste(String name, String msg) throws Exception{
		GistPaste gitpaste = new GistPaste();
		Paste paste = new Paste();
		paste.content = msg;
		gitpaste.files.put(name, paste);

		return paste(gitpaste);
	}

	public static String paste(GistPaste paste) throws Exception{
		String json = gson.toJson(paste,  GistPaste.class);
		String json2 = WebClient.post("https://api.github.com/gists", json, null);
		GistResponse response = gson.fromJson(json2, GistResponse.class);
		return response.html_url;
	}


	public static class GistPaste {
		public String description = "MC-SG.bot PASTE";
		@SerializedName("public")
		public boolean isPublic = true;
		public HashMap<String, Paste> files = new HashMap<>(); 
		
		
		public static class Paste {
			public String content;
		}

	}

	public static class GistResponse {
		public String html_url;
	}
}
