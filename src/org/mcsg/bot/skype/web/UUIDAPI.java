package org.mcsg.bot.skype.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UUIDAPI {

	/* By Daniel, denialmc */
	
	private static final String API_LINK = "http://mineskin.ca/uuid/?players=%s";
	private static final String UUID_FORMAT = "%s-%s-%s-%s-%s";
	private static final JSONParser jsonParser = new JSONParser();

	private static String convertToArray(List<String> list) {
		StringBuilder builder = new StringBuilder();

		builder.append("[");

		for (String string : list) {
			builder.append(",");
			builder.append('"');
			builder.append(string);
			builder.append('"');
		}

		builder.append("]");

		return "[" + builder.toString().substring(2);
	}

	public static HashMap<String, String> getUUIDs(List<String> users, int timeout){
		HashMap<String, String> uuids = new HashMap<String, String>();
		int count = users.size();
		try{
			for (int i = 0; i < count; i += 100) {
				List<String> query = users.subList(i, Math.min(count, i + 100));
				String array = convertToArray(query);


				JSONArray jsonArray = (JSONArray) jsonParser.parse(WebClient.request(String.format(API_LINK, array)));
				Iterator<?> iterator = jsonArray.iterator();

				while (iterator.hasNext()) {
					JSONObject result = (JSONObject) iterator.next();
					String uuid = (String) result.get("uuid");

					if (uuid.length() == 32) {
						String name = (String) result.get("name");
						uuid = String.format(UUID_FORMAT, uuid.substring(0, 8), uuid.substring(8, 12), uuid.substring(12, 16), uuid.substring(16, 20), uuid.substring(20, 32));

						uuids.put(name, uuid);
					}

				}
			}

		}catch (Exception e){}
		return uuids;

	}

	public static HashMap<String, String> getUUIDs(List<String> users) {
		return getUUIDs(users, 1000);
	}

	public static String getUUID(String name, int timeout) {
		return getUUIDs(Arrays.asList(name), timeout).get(name);
	}

	public static String getUUID(String name) {
		return getUUIDs(Arrays.asList(name)).get(name);
	}
}