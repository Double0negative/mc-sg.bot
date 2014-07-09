

package org.mcsg.bot.skype.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import com.google.gson.Gson;

public class Googler {

	public static GoogleResults search(String type, String query) throws IOException {
		String address = "http://ajax.googleapis.com/ajax/services/search/"+type+"?v=1.0&q=";
		String charset = "UTF-8";

		URL url = new URL(address + URLEncoder.encode(query, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);
		GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

		return results;
	}



}