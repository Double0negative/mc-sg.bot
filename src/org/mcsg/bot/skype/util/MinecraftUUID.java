package org.mcsg.bot.skype.util;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class MinecraftUUID {

	private static Gson gson = new Gson();
	private static List<HttpHeader> headers = new ArrayList<>();

	public static void main(String args[]){
		headers.add(new HttpHeader("Content-Type", "application/json"));
		try {
			long time = System.currentTimeMillis();
			System.out.println(getUUID("Double0negative").id + " "+ (System.currentTimeMillis() - time));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static Profile getUUID(String name) throws Exception{
		Search search = new Search();
		search.agent = "minecraft";
		search.name = name;

		String json = gson.toJson(search, Search.class);
		System.out.println(json);
		MinecraftProfile p = gson.fromJson(post("https://api.mojang.com/profiles/page/1", json, headers), MinecraftProfile.class);

		if(p.profiles.length > 0){
			return p.profiles[0];
		} else {
			return null;
		}

	}


	public static class Search{
		String agent;
		String name;
	}

	public static class MinecraftProfile{
		Profile[] profiles;
	}

	public static class Profile{
		String id;
		String name;
	}




	public static String post(String url, String body, List<HttpHeader> headers) throws Exception{
		long time = System.currentTimeMillis();
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		System.out.println("open connection "+ (System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		connection.setRequestMethod("POST");

		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		for (HttpHeader header : headers) {
			connection.setRequestProperty(header.getName(), header.getValue());
		}
		System.out.println("settings & haders "+ (System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		BufferedOutputStream writer = new BufferedOutputStream(connection.getOutputStream());
		writer.write(body.getBytes());
		writer.flush();
		writer.close();
		System.out.println("write "+ (System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));

		StringBuilder sb = new StringBuilder();
		char [] buff = new char[512];

		while (true) {
			int len = br.read(buff, 0, buff.length);
			if (len == -1) {
				break;
			}
			sb.append(buff, 0, len);
		}
		System.out.println("read "+ (System.currentTimeMillis() - time));


		br.close();
		return sb.toString();
	}

	public static class HttpHeader {
		private String name;
		private String value;

		public HttpHeader(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
