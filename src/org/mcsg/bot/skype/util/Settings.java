package org.mcsg.bot.skype.util;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

public class Settings {

	private static Gson gson = new Gson(); 
	public static File file = new File("bot_settings.json");
	public static Root Root = new Root();
	
	
	public static class Root{
		public Bot Bot = new Bot();
		public Weather Weather = new Weather();
		public Image Image = new Image();
	}
	
	public static class Bot {
		public String Owner = "drew.foland";
		public Chat chat = new Chat();
		
		public static class Chat{
			public int paste = 6;
			public int time = 5;
			public String pastemethod = "pastebinit";
		}
	}
	
	public static class Image {
		public String IMGUR_CLIENT_ID = "";
		public String MCSG_UPLOAD_KEY = "";
		public String IMAGE_UPLOAD_METHOD = "imgur";
	}
	
	public static class Weather {
		public String API_KEY = "";
		public int max_forecast = 4;
	}
	
	
	
	
	
	
	
	
	
	public static void load(){
		try {
			String json = FileUtils.readFile(file);
			Root root = gson.fromJson(json, Root.class);
			if(root != null){
				Root = root;
			}
			save();
		} catch (IOException e) {		}
	}
	
	public static void save(){
		try { 
			String json = gson.toJson(Root, Root.class);
			FileUtils.writeFile(file, json);
		} catch (Exception e){ }
	}
	
	
}
