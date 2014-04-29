package org.mcsg.bot.skype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.ReverbType;

import org.mcsg.bot.skype.commands.GetUsers;
import org.mcsg.bot.skype.commands.Heart;
import org.mcsg.bot.skype.commands.Hi;
import org.mcsg.bot.skype.commands.HostCall;
import org.mcsg.bot.skype.commands.Kick;
import org.mcsg.bot.skype.commands.KillProc;
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

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.ChatMessageEditListener;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class Bot {

	public static final String version ="1.10";

	private HashMap<String, SubCommand> commands = 
			new HashMap<String, SubCommand>();

	public static boolean killnuke = false;



	public static void main(String[] args) {
		System.out.println("Starting MC-SG.BOT version "+version);
		Skype.setDaemon(false); // to prevent exiting from this program
		try {
			new Bot().start();
		} catch (SkypeException e) {
			System.exit(1);
		}
	}

	public void start() throws SkypeException{

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



		Skype.addChatMessageEditListener(new ChatMessageEditListener() {

			@Override
			public void chatMessageEdited(ChatMessage arg0, Date arg1, User arg2) {
				try {
					System.out.println(arg2.getDisplayName()+" edited a message " + arg0.getContent());
				} catch (SkypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	

			}
		});

		Skype.addChatMessageListener(new ChatMessageAdapter() {
			public void chatMessageReceived(ChatMessage received) throws SkypeException {
				if(received.getContent().startsWith(".")){
					String split[] = received.getContent().split(" ");
					String command = getCommand(split);
					String args[]  = getArgs(split);

					System.out.println("Received command: "+Arrays.toString(split)+" from "+received.getSender().getId());

					if(command.equalsIgnoreCase("help") || command.equalsIgnoreCase("halp")){
						StringBuilder sb = new StringBuilder();
						sb.append("--- [ MC-SG Bot Help ] ---");

						for(String com : commands.keySet()){
							String name = commands.get(com).getName();
							if(name != null){
								sb.append("\n."+com+" - " + commands.get(com).getHelp());
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


	public void executeAsync(final SubCommand sub, final Chat chat, final User sender, final String[] args){
		new Thread(){
			public void run(){
				try {
					sub.execute(chat, sender, args);
				} catch (SkypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

