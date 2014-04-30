package org.mcsg.bot.skype.util;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

	public static String implode(String ... strs){
		StringBuilder sb = new StringBuilder();
		for(String str : strs){
			sb.append(str).append(" ");
		}
		return sb.toString().trim();
	}
	
	public static String implode(int start, String ... strs){
		List<String> list = Arrays.asList(strs);
		for(int a = 0; a < start; a++){
			list.remove(0);
		}
		return implode(list.toArray(new String[0]));
	}
	
	
}
