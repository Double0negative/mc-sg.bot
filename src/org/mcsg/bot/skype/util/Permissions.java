package org.mcsg.bot.skype.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.skype.Chat;
import com.skype.User;

public class Permissions {

	
	//private static HashMap<String, UserPerm> perms = new HashMap<>();
	
	private static HashMap<String, ArrayList<String>> perms = new HashMap<>();
	
	public static boolean hasPermission(User user, Chat chat, String perm){
		return user.getId().equals("drew.foland") || ( perms.containsKey(getPerm(user, chat)) &&
				( perms.get(getPerm(user, chat)).contains(perm) ||  perms.get(getPerm(user, chat)).contains("*")));
	}
	
	private static String getPerm(User user, Chat chat){
		return user.getId() +":"+ chat.getId();
	}
	
	private static String getPerm(String user, Chat chat){
		return user +":"+ chat.getId();
	}
	
	public static void addPerm(User user, Chat chat, String perm){
		addPerm(user.getId(), chat, perm);
	}
	
	public static void addPerm(String user, Chat chat, String perm){
		getArray(user, chat).add(perm);
	}
	
	public static void removePerms(User user, Chat chat, String perm){
		removePerms(user.getId(), chat, perm);
	}
	
	public static void removePerms(String user, Chat chat, String perm){
		getArray(user, chat).remove(perm);
	}
	
	public static ArrayList<String> getArray(String user, Chat chat){
		ArrayList<String> list = perms.get(getPerm(user, chat));
		if(list == null){
			list = new ArrayList<String>();
		}
		perms.put(getPerm(user, chat), list);
		return list;
	}
	
	class UserPerm {
		User user;
		Chat chat;
		boolean global;
	}
	
	
}
