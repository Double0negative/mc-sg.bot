package org.mcsg.bot.skype.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class Permissions {


	//private static HashMap<String, UserPerm> perms = new HashMap<>();

	//private static HashMap<String, ArrayList<String>> perms = new HashMap<>();
	//chat, <map<user>, list<perm>>

	private static HashMap<Chat, HashMap<String, ArrayList<String>>> perms = new HashMap<>();
	private static File pfile = new File("Bot_Permissions.json");

	static boolean loaded = false;
	
	public static boolean hasPermission(User user, Chat chat, String perm) throws Exception{
		if(!loaded){
			load();
			loaded = true;
		}
		return user.getId().equals("drew.foland") || ( perms.containsKey(chat) && perms.get(chat).containsKey(user.getId()) && 
				(perms.get(chat).get(user.getId()).contains(perm) ||  perms.get(chat).get(user.getId()).contains("*")));
	}


	public static void addPerm(User user, Chat chat, String perm) throws IOException{
		addPerm(user.getId(), chat, perm);
		save();
	}

	public static void addPerm(String user, Chat chat, String perm) throws IOException{
		getArray(user, chat).add(perm);
		save();
	}

	public static void removePerms(User user, Chat chat, String perm) throws IOException{
		removePerms(user.getId(), chat, perm);
		save();
	}

	public static void removePerms(String user, Chat chat, String perm) throws IOException{
		getArray(user, chat).remove(perm);
		save();
	}

	public static ArrayList<String> getArray(String user, Chat chat){
		HashMap<String, ArrayList<String>> map = perms.get(chat);
		if(map == null){
			map = new HashMap<>();
		}
		ArrayList<String> list = map.get(user);
		if(list == null){
			list = new ArrayList<String>();
		}
		map.put(user, list);
		perms.put(chat, map);
		return list;
	}

	public static void save() throws IOException{
		Gson gson = new Gson();
		SkypePermission permissions = new SkypePermission();
		ArrayList<ChatPermission> chatperms = new ArrayList<ChatPermission>();
		for(Chat chat : perms.keySet()){
			ChatPermission chatperm = new ChatPermission();
			HashMap<String, ArrayList<String>> map = perms.get(chat);
			ArrayList<UserPerm> userPerm = new ArrayList<UserPerm>();
			for(String str : map.keySet()){
				UserPerm user = new UserPerm();
				user.user = str;
				user.perms = map.get(str).toArray(new String[0]);
				userPerm.add(user);
			}
			chatperm.userPerm = userPerm.toArray(new UserPerm[0]);
			chatperms.add(chatperm);
		}
		permissions.chatPermission = chatperms.toArray(new ChatPermission[0]);


		String json = gson.toJson(permissions, SkypePermission.class);
		FileUtils.writeFile(pfile, json);
	}
	
	public static void load() throws Exception {
		String json = FileUtils.readFile(pfile);
		SkypePermission permissions = new Gson().fromJson(json, SkypePermission.class);
		
		HashMap<Chat, HashMap<String, ArrayList<String>>> newperm = new HashMap<>();
		
		if(permissions.chatPermission == null){
			perms = newperm;
			return;
		}
		
		for(ChatPermission chatperms : permissions.chatPermission){
			HashMap<String, ArrayList<String>> map = new HashMap<>();
			for(UserPerm userperms : chatperms.userPerm){
				map.put(userperms.user, new ArrayList<String>(Arrays.asList(userperms.perms)));
			}
			newperm.put(getChat(chatperms.chat), map);
		}
		
		perms = newperm;
	}
	
	public static Chat getChat(String id) throws SkypeException{
		Chat[] chats = Skype.getAllChats();
		for(Chat chat : chats){
			if(chat.getId().equals(id)){
				return chat;
			}
		} 
		return null;
	}


	public static class SkypePermission{
		ChatPermission [] chatPermission;
	}
	public static class ChatPermission{
		String chat;
		UserPerm [] userPerm;
	}

	public static class UserPerm {
		String user;
		String [] perms;
	}


}
