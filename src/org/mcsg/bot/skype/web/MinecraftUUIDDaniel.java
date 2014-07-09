package org.mcsg.bot.skype.web;
import com.google.gson.Gson;

public class MinecraftUUIDDaniel {

	private static final String API_URL = "http://mcapi.ca/uuid/?players=";
	private static Gson gson = new Gson();

	public static class Profile{
		private static final String UUID_FORMAT = "%s-%s-%s-%s-%s";

		public String uuid;
		public String name;
		
		public String getUuid(){
			return String.format(UUID_FORMAT, uuid.substring(0, 8), uuid.substring(8, 12), uuid.substring(12, 16), uuid.substring(16, 20), uuid.substring(20, 32));
		}
		
	}

	public static Profile[] getUUIDs(String[] name) throws Exception{
		String json = gson.toJson(name, String[].class);
		Profile[] p = gson.fromJson(WebClient.request(API_URL + json), Profile[].class);
		return p;
	}

}