package org.mcsg.bot.skype.commands;

import java.util.Random;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class RandomNumber implements SubCommand {

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		if(args.length == 0){
			chat.send(new Random().nextInt() + "");
		} else if(args.length == 1){
			if(args[0].equalsIgnoreCase("double")){
				chat.send(new Random().nextDouble()+"");
			} else if (args[0].equalsIgnoreCase("boolean")){
				chat.send(new Random().nextBoolean()+"");
			} else if (args[0].equalsIgnoreCase("int")){
				chat.send(new Random().nextInt() +"");
			} else if (args[0].equalsIgnoreCase("long")){
				chat.send(new Random().nextLong()+"");
			}
		} else if (args.length == 2){
			chat.send(new Random().nextInt(Integer.parseInt(args[1])) +"");
		} else if(args.length == 3){
			chat.send(((int) new Random().nextDouble() * Integer.parseInt(args[1]) - Integer.parseInt(args[2])) + "");
		}
		
	}

	@Override
	public String getHelp() {
		return "<type (int:double:long:boolean)> <max (int only)>Get a random number";
	}

	@Override
	public String getName() {
		return "Random Gen";
	}

}
