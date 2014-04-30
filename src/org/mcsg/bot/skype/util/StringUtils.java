package org.mcsg.bot.skype.util;

public class StringUtils {

	public static String implode(String ... strs){
		StringBuilder sb = new StringBuilder();
		for(String str : strs){
			sb.append(str).append(" ");
		}
		return sb.toString().trim();
	}
	
	
}
