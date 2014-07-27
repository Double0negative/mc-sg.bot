package org.mcsg.bot.skype.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.mcsg.bot.skype.ChatManager;

import com.skype.Chat;
import com.skype.SkypeException;

public class ShellCommand {

	private static int id = 0;

	private static int getId(){
		return ++id;
	}

	private static HashMap<Integer, Process> procs = new HashMap<>();
	private static ConcurrentHashMap<Integer, ArrayList<Chat>> chats = new ConcurrentHashMap<>();

	public static int exec(final Chat chat, String args){
		return exec(chat, args, 0, true);
	}
	
	public static int exec(final Chat chat, String args, long limit,  boolean displayCommand){
	  return exec(chat, args, limit, displayCommand, false);
	}
	
	public static int exec(final Chat chat, String args, long limit,  boolean displayCommand, boolean wait) {
		return exec(chat, args, limit, displayCommand, wait, null);
	}
	
	public static int exec(final Chat chat, String args, long limit, boolean displayCommand, boolean wait, ReaderRunnable readerRunnable){
		int id = getId();
		try{
			String name = "files/temp"+System.currentTimeMillis()+".sh";
			File file = new File(name);
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			pw.println(args);
			pw.flush();
			if (displayCommand) ChatManager.chat(chat, "Running command: \""+ args +"\" ID: "+id);
			final Process proc = Runtime.getRuntime().exec("bash "+name);
			if (wait) proc.waitFor();
			procs.put(id, proc);
			chats.put(id, new ArrayList<Chat>());
			readToChat(chat, id);
			startReaders(id, readerRunnable);

			if (limit != 0) {
				new Limiter(proc, chat, limit, id).start();
			}

			file.deleteOnExit();
			pw.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return id;
	}

	public static void startReaders(int id) {
		startReaders(id, null);
	}

	public static void startReaders(int id, ReaderRunnable readerRunnable) {
		Process proc = procs.get(id);

		if (readerRunnable == null) {
			readStream("", id, proc.getInputStream());
			readStream("ERROR: ", id, proc.getErrorStream());
		} else {
			readStream("", id, proc.getInputStream(), readerRunnable);
			readStream("ERROR: ", id, proc.getErrorStream(), readerRunnable);
		}
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

	private static void readStream(final String prefix, final int id,  final InputStream input) {
        ReaderRunnable readerRunnable = new ReaderRunnable() {
            public void run() {
            	for (String message : this.messages) {
            		for (Chat chat : this.chats) ChatManager.chat(chat, this.prefix + message);
            	}
            }
        };
        readStream(prefix, id, input, readerRunnable);
	}

    private static void readStream(final String prefix, final int id, final InputStream input, final ReaderRunnable runnable) {
        new Thread() {
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(input));
                    List<String> lines = new ArrayList<String>();
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        lines.add(line);
                    }
                    runnable.setMessages(lines).setChats(chats.get(id)).setPrefix(prefix).run();
                    in.close();
                } catch (Exception ex) {
                }
            }
        }.start();
    }
	
	public static Process getProcess(int id){
	  return procs.get(id);
	}
	
	public static int getProcessID(Process process) {
	  if (process != null) {
	      for (Map.Entry<Integer, Process> entryProcess : procs.entrySet()) {
	          if (entryProcess.getValue().equals(process)) return entryProcess.getKey();
	      }
	  }
	  return -1;
	}

	public static void kill(int id){
		procs.get(id).destroy();
	}

	public static void forceKill(int id){
		procs.get(id).destroyForcibly();
	}
	
	static class  Limiter extends Thread{
		Process process;
		long time;
		int id;
		Chat chat;
		public Limiter(Process process, Chat chat, long time, int id){
			this.process = process;
			this.time = time;
			this.id = id;
			this.chat = chat;
		}
		
		public void run(){
			try { Thread.sleep(time); } catch (Exception e){ ChatManager.printThrowable(chat, e);}
			if(process.isAlive()){
				process.destroyForcibly();
				try {
					chat.send("Process "+id+" exceeded time limit. Process was killed");
				} catch (SkypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
    public abstract static class ReaderRunnable implements Runnable {
        protected List<String> chatMessages = new ArrayList<String>();
        protected ArrayList<Chat> chats = new ArrayList<Chat>();
        protected String prefix = "";

        public ArrayList<Chat> getChats() {
            return this.chats;
        }

        public List<String> getMessages() {
            return this.chatMessages;
        }
        
        public String getPrefix() {
        	return this.prefix;
        }

        public ReaderRunnable setChats(ArrayList<Chat> chats) {
            this.chats = chats;
            return this;
        }

        public ReaderRunnable setMessages(List<String> messages) {
            this.chatMessages = messages;
            return this;
        }
        
        public ReaderRunnable setPrefix(String prefix) {
        	if (prefix != null) this.prefix = prefix;
        	return this;
        }
    }

}
