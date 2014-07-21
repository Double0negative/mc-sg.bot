package org.mcsg.bot.skype;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.mcsg.bot.skype.commands.Connect4;
import org.mcsg.bot.skype.commands.Define;
import org.mcsg.bot.skype.commands.GameStatsCommand;
import org.mcsg.bot.skype.commands.GenImage;
import org.mcsg.bot.skype.commands.GetUsers;
import org.mcsg.bot.skype.commands.GitHubListener;
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
import org.mcsg.bot.skype.commands.TicTacToe;
import org.mcsg.bot.skype.commands.UUIDCommand;
import org.mcsg.bot.skype.commands.Version;
import org.mcsg.bot.skype.commands.VideoSearch;
import org.mcsg.bot.skype.commands.Weather;
import org.mcsg.bot.skype.commands.WebAbstract;
import org.mcsg.bot.skype.commands.WebSearch;
import org.mcsg.bot.skype.commands.WikipediaSearchCommand;
import org.mcsg.bot.skype.message.MessagePaster;
import org.mcsg.bot.skype.web.GithubListener;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Friend;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class Bot {

	public static final String version ="1.46 Drawing options";

	private static HashMap<String, SubCommand> commands = 
			new HashMap<String, SubCommand>();
	 private static HashMap<String, SubCommand> aliases = 
	      new HashMap<String, SubCommand>();

	public static boolean killnuke = false;

	private static Chat defaultChat;
	public static final String LAST_FILE  = "lastchat";
	public static final HashMap<Chat, Integer> messageCount = new HashMap<Chat, Integer>();

	private ConcurrentHashMap<String, Integer> messages = new ConcurrentHashMap<>();
	

	public static void main(String[] args) {


		System.out.println("Starting MC-SG.BOT version "+version);
		System.out.println("Settings File: "+Settings.file.getAbsolutePath());
		System.out.println("Permissions Files: "+Permissions.pfile.getAbsolutePath());


		Skype.setDaemon(false); // to prevent exiting from this program
		try {

			File f = new File(LAST_FILE);
			if(f.exists()){
				Scanner scanner = new Scanner(f);
				String chatid = scanner.nextLine();
				for(Chat chat : Skype.getAllChats()){
					if(chat.getId().equals(chatid)){
					  defaultChat = chat;
						chat.send("Starting MC-SG.BOT version "+version);
						chat.send("/topic MC-SG BOT  v"+version);
						
						int a = 0;
						for(Friend t : Skype.getContactList().getAllUserWaitingForAuthorization()){
							t.setAuthorized(true);
							a++;
						}
						chat.send("Accepted "+a+" new contacts.");
					}
					scanner.close();
					f.delete();
				}
			}
			new Bot().start();



		} catch (Exception e) {
			System.exit(1);
		}
	}

	public void start() throws Exception{

		//new SpamClean().start();
		ChatManager.start();
		Settings.load();
		GithubListener.listen();


		registerCommand(new Ping());
		registerCommand(new MinecraftPingCommand());
		registerCommand(new Nuke());
		registerCommand(new StopNuke());
		registerCommand(new HostCall());
		registerCommand(new Hi());
		registerCommand(new RandomNumber());
		registerCommand(new Heart());
		registerCommand(new Kick());
		registerCommand(new GetUsers());
		registerCommand(new Shell());
		registerCommand(new Perm());
		registerCommand(new ShellWrite());
		registerCommand(new KillProc());
		registerCommand(new ProcIn());
		registerCommand(new Stop());
		registerCommand(new Source());
		registerCommand(new ManageChat());
		registerCommand(new WikipediaSearchCommand());
		registerCommand(new WebSearch());
		registerCommand(new WebAbstract());
		registerCommand(new Define());
		registerCommand(new ImageSearch());
		registerCommand(new VideoSearch());
		registerCommand(new Leave());
		registerCommand(new JavaCommand());
		registerCommand(new Is());
		registerCommand(new UUIDCommand());
		registerCommand(new Weather());
		registerCommand(new Version());
		registerCommand(new Connect4());
		registerCommand(new TicTacToe());
		registerCommand(new GameStatsCommand());
		registerCommand(new GenImage());
		registerCommand(new GitHubListener());


		Skype.addChatMessageListener(new ChatMessageAdapter() {
			public void chatMessageReceived(ChatMessage received) throws SkypeException {
			//	int count = (messageCount.containsKey(received.getChat()) ? messageCount.get(received.getChat()) : 0);
			//	messageCount.put(received.getChat(), count+1);

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

					SubCommand sub = getCommand(command);
					if(sub != null){
						executeAsync(sub, received.getChat(), received.getSender(),  args);
					}

				} else {
					try {
						MessagePaster.message(received.getSender(), received.getChat(), received.getContent());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public static SubCommand getCommand(String command){
	  if(commands.containsKey(command)){
	    return commands.get(command);
	  } else if(aliases.containsKey(command)){
	    return aliases.get(command);
	  } else return null;
	}
	
	public static void registerCommand(SubCommand command){
	  if(commands.containsKey(command.getCommand())){
	    ChatManager.printThrowable(getDefaultChat(), new RuntimeException("Cannot register command: "+command.getCommand()+". Command already exist"));
	  } else {
	    commands.put(command.getCommand(), command);
	  }
	  if(command.getAliases() != null){
	    for(String alias : command.getAliases()){
	      if(aliases.containsKey(alias)){
	        ChatManager.printThrowable(getDefaultChat(), new RuntimeException("Cannot register alias: "+ alias+". Alias already registered by "+aliases.get(alias).getCommand()));
	      } else {
	        aliases.put(alias, command);
	      }
	    }
	  }
	}
	
	public static Chat getDefaultChat(){
	  return defaultChat;
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



	private String getCommand(String[] split){
		return split[0].substring(1).toLowerCase();
	}

	public String[] getArgs(String[] split){
		ArrayList<String>t = new ArrayList<String>(Arrays.asList(split)); 
		t.remove(0);
		return t.toArray(new String[t.size()]);
	}

	public static Chat getChat(String id){
		try {
			Chat[] chats = Skype.getAllChats();
			for(Chat chat : chats){
				if(chat.getId().equals(id)){
					return chat;
				}
			}
			return null;
		} catch (SkypeException e) {
			return null;
		}
	}
}


