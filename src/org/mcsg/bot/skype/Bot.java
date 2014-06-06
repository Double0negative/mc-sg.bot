package org.mcsg.bot.skype;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.mcsg.bot.skype.commands.ArgTest;
import org.mcsg.bot.skype.commands.Connect4;
import org.mcsg.bot.skype.commands.Define;
import org.mcsg.bot.skype.commands.GetUsers;
import org.mcsg.bot.skype.commands.Heart;
import org.mcsg.bot.skype.commands.Hi;
import org.mcsg.bot.skype.commands.HostCall;
import org.mcsg.bot.skype.commands.ImageSearch;
import org.mcsg.bot.skype.commands.Is;
import org.mcsg.bot.skype.commands.JavaCommand;
import org.mcsg.bot.skype.commands.Kick;
import org.mcsg.bot.skype.commands.KillProc;
import org.mcsg.bot.skype.commands.Leave;
import org.mcsg.bot.skype.commands.ManageChat;
import org.mcsg.bot.skype.commands.MinecraftPingCommand;
import org.mcsg.bot.skype.commands.Nuke;
import org.mcsg.bot.skype.commands.Perm;
import org.mcsg.bot.skype.commands.Ping;
import org.mcsg.bot.skype.commands.ProcIn;
import org.mcsg.bot.skype.commands.RandomNumber;
import org.mcsg.bot.skype.commands.Shell;
import org.mcsg.bot.skype.commands.ShellWrite;
import org.mcsg.bot.skype.commands.Source;
import org.mcsg.bot.skype.commands.Stop;
import org.mcsg.bot.skype.commands.StopNuke;
import org.mcsg.bot.skype.commands.SubCommand;
import org.mcsg.bot.skype.commands.UUIDCommand;
import org.mcsg.bot.skype.commands.Version;
import org.mcsg.bot.skype.commands.VideoSearch;
import org.mcsg.bot.skype.commands.Weather;
import org.mcsg.bot.skype.commands.WebAbstract;
import org.mcsg.bot.skype.commands.WebSearch;
import org.mcsg.bot.skype.commands.WikipediaSearchCommand;
import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.Settings;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.GlobalChatListener;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class Bot {

	public static final String version ="1.30";

	private HashMap<String, SubCommand> commands = 
			new HashMap<String, SubCommand>();

	public static boolean killnuke = false;

	public static final String LAST_FILE  = "lastchat";

	private ConcurrentHashMap<String, Integer> messages = new ConcurrentHashMap<>();

	public static void main(String[] args) {
		System.out.println("Starting MC-SG.BOT version "+version);
		Skype.setDaemon(false); // to prevent exiting from this program
		try {

			File f = new File(LAST_FILE);
			if(f.exists()){
				Scanner scanner = new Scanner(f);
				String chatid = scanner.nextLine();
				for(Chat chat : Skype.getAllChats()){
					if(chat.getId().equals(chatid)){
						chat.send("Starting MC-SG.BOT version "+version);
						chat.send("/topic MC-SG BOT  v"+version);
					}
					scanner.close();
					f.delete();
				}
			}

			new Bot().start();



		} catch (SkypeException | FileNotFoundException e) {
			System.exit(1);
		}
	}

	public void start() throws SkypeException{

		//new SpamClean().start();
		ChatManager.start();
		Settings.load();

		commands.put("ping", new Ping());
		commands.put("mcping", new MinecraftPingCommand());
		commands.put("nuke", new Nuke());
		commands.put("killnuke", new StopNuke());
		commands.put("host", new HostCall());
		commands.put("hi", new Hi());
		commands.put("rand", new RandomNumber());
		commands.put("<3", new Heart());
		commands.put("kick", new Kick());
		commands.put("getusers", new GetUsers());
		commands.put("shell", new Shell());
		commands.put("sh", new Shell());
		commands.put("perm", new Perm());
		commands.put("out", new ShellWrite());
		commands.put("killproc", new KillProc());
		commands.put("procin", new ProcIn());
		commands.put("stop", new Stop());
		commands.put("src", new Source());
		commands.put("setchat", new ManageChat());
		commands.put("wiki", new WikipediaSearchCommand());
		commands.put("search", new WebSearch());
		commands.put("abstract", new WebAbstract());
		commands.put("define", new Define());
		commands.put("img", new ImageSearch());
		commands.put("g", new WebSearch());
		commands.put("video", new VideoSearch());
		commands.put("v", new VideoSearch());
		commands.put("vid", new VideoSearch());
		commands.put("youtube", new VideoSearch());
		commands.put("yt", new VideoSearch());
		commands.put("leave", new Leave());
		commands.put("java", new JavaCommand());
		commands.put("is", new Is());
		commands.put("argtest", new ArgTest());
		commands.put("uuid", new UUIDCommand());
		commands.put("weather", new Weather());
		commands.put("w", new Weather());
		commands.put("version", new Version());
		commands.put("c4", new Connect4());


		Skype.addChatMessageListener(new ChatMessageAdapter() {
			public void chatMessageReceived(ChatMessage received) throws SkypeException {

				//incMessages(received);

				if(received.getContent().startsWith(".") && received.getTime().getTime() > System.currentTimeMillis() - 300000){
					String split[] = received.getContent().split(" ");
					String command = getCommand(split);
					String args[]  = getArgs(split);

					System.out.println("Received command: "+Arrays.toString(split)+" from "+received.getSender().getId());

					if(command.equalsIgnoreCase("help") || command.equalsIgnoreCase("halp")){
						StringBuilder sb = new StringBuilder();
						sb.append("--- [ MC-SG Bot Help ] ---");

						for(String com : commands.keySet()){
							String name = commands.get(com).getUsage();
							if(name != null){
								ChatManager.chat(received.getChat(), "\n"+commands.get(com).getUsage()+" - " + commands.get(com).getHelp());
							}
						}
						received.getChat().send(sb.toString());
					}

					SubCommand sub = commands.get(command);
					if(sub != null){
						executeAsync(sub, received.getChat(), received.getSender(),  args);
					}

				}
				//received.getChat().get
			}
		});



	}


	public void incMessages(ChatMessage received) throws SkypeException{

		int a = 0;
		if(messages.containsKey(received.getSenderId() + ":"+received.getChat().getId())){
			a = messages.get(received.getSenderId() + ":"+received.getChat().getId());
		}

		a++;
		if(a > 4){
			received.getChat().send("/kick "+received.getSenderId());
			received.getChat().send("User "+received.getSenderId()+" was kicked for spam ");
		}
		messages.put(received.getSenderId() + ":"+received.getChat().getId(), a);

	}

	public class SpamClean extends Thread{
		public void run(){
			while(true){
				messages.clear();
				try { sleep(10000); } catch (Exception e){}
			}
		}
	}

	public void executeAsync(final SubCommand sub, final Chat chat, final User sender, final String[] args){
		new Thread(){
			public void run(){
				try {
					sub.execute(chat, sender, args);
				} catch (Exception e) {
					ChatManager.printThrowable(chat, e);
				}
			}
		}.start();
	}



	public String getCommand(String[] split){
		return split[0].substring(1).toLowerCase();
	}

	public String[] getArgs(String[] split){
		ArrayList<String>t = new ArrayList<String>(Arrays.asList(split)); 
		t.remove(0);
		return t.toArray(new String[t.size()]);
	}
}

