package org.mcsg.bot.skype.util;

import org.mcsg.bot.skype.util.GistPaster.GistPaste.Files;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class GistPaster {

	private static Gson gson = new Gson();
	
	public static String paste(String msg) throws Exception{
		GistPaste paste = new GistPaste();
		paste.files = new Files[1];
		paste.files[0] = new Files();
		paste.files[0].content = msg;
		
		return paste(paste);
	}
	
	public static String paste(GistPaste paste) throws Exception{
		String json = gson.toJson(paste,  GistPaste.class);
		String json2 = WebClient.post("http://api.github.com/gists", json, null);
		GistResponse response = gson.fromJson(json2, GistResponse.class);
		return response.url;
	}
	
	
	public static class GistPaste {
		public String description = "MC-SG.bot PASTE";
	    @SerializedName("public")
		public boolean isPublic = true;
		public Files[] files;
		
		public static class Files{
			public String content;
		}
	}
	
	public static class GistResponse {
		public String url;
	}
}
