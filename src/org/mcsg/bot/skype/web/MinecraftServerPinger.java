package org.mcsg.bot.skype.web;

import org.mcsg.bot.skype.util.StringUtils;

import com.google.gson.Gson;

public class MinecraftServerPinger {

	public static final String HOST = "http://mineskin.ca/v2/query/info/?ip={0}&port={1}";
	public static final Gson gson = new Gson();
	
	public static MinecraftPing ping(String host) throws Exception{
		return ping(host, 25565);
	}
	
	
	public static MinecraftPing ping(String host, int port) throws Exception{
		String json = WebClient.request(StringUtils.replaceVars(HOST, host, port));
		return gson.fromJson(json, MinecraftPing.class);
	}
	
	
	public static class MinecraftPing{
		public boolean status;
		public String error; //only exist if status = false;
		public String motd;
		public String version;
		public Players players;
		public long ping;
		
		public class Players{
			public int online, max;
		}
		
	}

}
