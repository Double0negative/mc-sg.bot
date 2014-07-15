package org.mcsg.bot.skype.util;

import java.util.HashMap;
import java.util.Map;

public class MapWrapper extends HashMap<String, String> {

	private static final long serialVersionUID = 7504344109599254698L;


	public MapWrapper(){

	}

	public MapWrapper(Map<String, String> map){
		this.putAll( map );
	}

	public int getInt(String key){
		return Integer.parseInt(get(key));
	}

	public int getInt(String key, int def){
		return Integer.parseInt(getOrDefault(key, def+""));
	}

	public double getDouble(String key){
		return Double.parseDouble(get(key));
	}

	public double getDouble(String key, double def){
		return Double.parseDouble(getOrDefault(key, def+""));
	}

	public long getLong(String key){
		return Long.parseLong(get(key));
	}

	public long getLoing(String key, long def){
		return Long.parseLong(getOrDefault(key, def+""));
	}

	public boolean getBoolean(String key){
		return Boolean.parseBoolean(key);
	}

	public boolean getBoolean(String key, boolean def){
		return Boolean.parseBoolean(getOrDefault(key, def+""));
	}

	public void put(String key, Object value){
		if(value != null)
			put(key, value.toString());
		else 
			put(key, null);
	}

	public boolean containsKey(String key){
		return containsKey(key);
	}


	public MapWrapper clone(){
		return new MapWrapper(this);
	}

}
