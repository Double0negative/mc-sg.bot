package org.mcsg.bot.skype.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class WebClient {

	public static String request(String urls){
		StringBuilder sb = new StringBuilder();

		try{
			URL url = new URL(urls.replace(" ", "%20"));

			BufferedReader br = new BufferedReader(new InputStreamReader(
					url.openStream()));

			char[] buff = new char[512];

			while (true) {
				int len = br.read(buff, 0, buff.length);
				if (len == -1) {
					break;
				}
				sb.append(buff, 0, len);
			}
			br.close();
		}catch (Exception e){

		}
		return sb.toString();

	}


}
