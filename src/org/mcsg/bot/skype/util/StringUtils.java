package org.mcsg.bot.skype.util;

import java.util.ArrayList;
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
		List<String> list = new ArrayList<String>(Arrays.asList(strs));
		for(int a = 0; a < start; a++){
			list.remove(0);
		}
		return implode(list.toArray(new String[0]));
	}


	public static String replaceVars(String str, Object ... args){
		int a = 0;
		for(Object ob: args){
			str = str.replace("{"+a+"}", ob.toString());
			a++;
		}
		return str;

	}
}
