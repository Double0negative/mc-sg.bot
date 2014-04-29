package org.mcsg.bot.skype.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.skype.Chat;

public class ShellCommand {

	private static int id = 0;

	private static int getId(){
		return ++id;
	}

	private static HashMap<Integer, Process> procs = new HashMap<>();
	private static ConcurrentHashMap<Integer, ArrayList<Chat>> chats = new ConcurrentHashMap<>();

	public static int exec(final Chat chat, String args){
		int id = getId();
		try{
			String name = "temp"+System.currentTimeMillis()+".sh";
			File file = new File(name);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			pw.println(args);
			pw.flush();
			chat.send("Running command: \""+ args +"\" ID: "+id);
			final Process proc = Runtime.getRuntime().exec("bash "+name);

			procs.put(id, proc);
			chats.put(id, new ArrayList<Chat>());
			readToChat(chat, id);
			startReaders(id);


			file.deleteOnExit();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}

	public static void startReaders(int id){
		Process proc = procs.get(id);

		readStream("", id, proc.getInputStream());
		readStream("ERROR: ", id, proc.getErrorStream());
	}

	public static void readToChat(Chat chat, int id){
		chats.get(id).add(chat);
	}

	public static void quiteChat(Chat chat, int id){
		chats.get(id).remove(chat);
	}

	public static void writeStream(final int id,  final String text){
		new Thread(){
			public void run(){
				PrintWriter pw = new PrintWriter(procs.get(id).getOutputStream());
				pw.println(text);
				pw.flush();
			}
		}.start();
	}

	private static void readStream(final String prefix, final int id,  final InputStream input){
		new Thread(){
			public void run(){
				try{
					String line = "";

					BufferedReader in = new BufferedReader(
							new InputStreamReader(input));
					while ((line = in.readLine()) != null) {
						for(Chat chat : chats.get(id)){
							chat.send(prefix+line);
						}
					}
					in.close();
				}catch (Exception e){}
			}
		}.start();
	}

	public static void kill(Chat chat, int id){
		procs.get(id).destroy();
	}

	public static void forceKill(Chat chat, int id){
		procs.get(id).destroyForcibly();
	}

}
