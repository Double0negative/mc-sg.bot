package org.mcsg.bot.skype.web;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.mcsg.bot.skype.util.HttpHeader;
import org.mcsg.bot.skype.util.Settings;
import org.mcsg.bot.skype.util.WebClient;

import com.google.gson.Gson;

public class ImgurUpload {

	
	public static String upload(byte [] img){
		try {
			List<HttpHeader> headers = new ArrayList<HttpHeader>();
			headers.add(new HttpHeader("Authorization", "Client-ID "+Settings.Root.Image.IMGUR_CLIENT_ID));
			String json = WebClient.postArgs("https://api.imgur.com/3/image", headers, "image", Base64.getEncoder().encodeToString(img));
			return new Gson().fromJson(json, ImgurResponse.class).data.link;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public  class ImgurResponse {
		public Data data;
		
		public class Data {
			String link;
		}
	}
}