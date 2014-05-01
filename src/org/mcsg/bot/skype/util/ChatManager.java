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

	public static void printThrowable(Chat chat, Throwable t){
		ChatManager.chat(chat, t.toString());
		for(StackTraceElement el : t.getStackTrace())
			ChatManager.chat(chat, "\t" + el.toString());

		Throwable e1 = t.getCause();
		if(e1 != null){
			ChatManager.chat(chat, e1.toString());
			for(StackTraceElement el : e1.getStackTrace())
				ChatManager.chat(chat, "\t" + el.toString());
		}

		for(int a  = 0; a < paste ; a++){
			chat(chat, "");
		}
	}


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
							try{
								StringBuilder sb = new StringBuilder();
								for(String msg : chats.get(chat)){
									sb.append(msg).append("\n");
								}
								sb.delete(sb.length() - 1, sb.length());
								if(chats.remove(chat).size() > paste){
									chat.send("Output: "+createPaste(sb.toString()));
								} else {
									try { chat.send(sb.toString()); } catch (SkypeException e) { printThrowable(chat, e); }
								}
							} catch (Exception e){
								printThrowable(chat, e);
							}
						}

						sleep(seconds * 1000);
					}catch (Exception e){

					}

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

	public static String createPaste(final String msg) throws Exception{
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
			return line;
		}
		return line;
	}


}
