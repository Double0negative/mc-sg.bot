package org.mcsg.bot.skype.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.skype.Chat;
import com.skype.SkypeException;

public class ChatManager {

	private static ConcurrentHashMap<Chat, ArrayList<String>> chats = new ConcurrentHashMap<>();

	private static int paste = 6;
	private static int seconds = 5;

	public static void chat(Chat chat, String msg){
		ArrayList<String> msgs = chats.get(chat);
		if(msgs == null){
			msgs = new ArrayList<String>();
		}
		msgs.add(msg);
		chats.put(chat, msgs);
	}


	public static void start(){
		new Thread(){
			public void run(){
				while(true){
					try{
						HashMap<Chat, ArrayList<String>> chats_copy = new HashMap<>(chats); //apparently chm doesn't have .keySet(); probs better way to do this
						for(Chat chat : chats_copy.keySet()){
							StringBuilder sb = new StringBuilder();
							sb.append(" ");
							for(String msg : chats.get(chat)){
								sb.append(msg).append("\n");
							}
							sb.delete(sb.length() - 1, sb.length());
							if(chats.remove(chat).size() > paste){
								createPaste(chat, sb.toString());
							} else {
								try { chat.send(sb.toString()); } catch (SkypeException e) {}
							}
						}

						sleep(seconds * 1000);
					}catch (Exception e){}

				}

			}
		}.start();
	}
	
	public static void setSeconds(int secondss){
		seconds = secondss;
	}
	
	public static void setPaste(int pastes){
		paste = pastes;
	}

	public static void createPaste(final Chat chat, final String msg){
		new Thread(){
			public void run(){
				try {
					String paste = "paste"+System.currentTimeMillis();
					File file1 = new File(paste);
					{
						PrintWriter pw = new PrintWriter(new FileWriter(file1));
						pw.println(msg);
						pw.flush();
						pw.close();
					}
					String script = "script" + System.currentTimeMillis();
					File file2 = new File(script);
					{
						PrintWriter pw1 = new PrintWriter(new FileWriter(file2));
						pw1.println("cat "+paste+" | pastebinit -a \"MC-SG.BOT Output\"");
						pw1.flush();
						pw1.close();
					}
					
					Process proc = Runtime.getRuntime().exec("bash "+script);
					String line = "";

					file2.deleteOnExit();
					file2.deleteOnExit();

					BufferedReader in = new BufferedReader(
							new InputStreamReader(proc.getInputStream()));
					while ((line = in.readLine()) != null) {
						chat.send("Output: "+line);
					}

				} catch (IOException | SkypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();
	}
}
