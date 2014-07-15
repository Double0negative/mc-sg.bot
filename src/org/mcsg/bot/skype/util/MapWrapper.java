package org.mcsg.api.util;

import java.util.HashMap;
import java.util.Map;

public class MapWrapper {

	private Map<String, String> map;
	
	public MapWrapper(){
		this.map = new HashMap<String, String>();
	}
	
	public MapWrapper(Map<String, String> map){
		this.map = map;
	}
	
	public int getInt(String key){
		return Integer.parseInt(map.get(key));
	}
	
	public double getDouble(String key){
		return Double.parseDouble(map.get(key));
	}
	
	public long getLong(String key){
		return Long.parseLong(map.get(key));
	}
	
	public boolean getBoolean(String key){
		return Boolean.parseBoolean(key);
	}
	
	public String get(String key){
		return map.get(key);
	}
	
	public void put(String key, Object value){
	    if(value != null)
		map.put(key, value.toString());
	    else 
		map.put(key, null);
	}
	
	public boolean containsKey(String key){
		return map.containsKey(key);
	}
	
	public Map<String, String>getMap(){
		return map;
	}
	
	public MapWrapper clone(){
	    return new MapWrapper(this.getMap());
	}
	
}
