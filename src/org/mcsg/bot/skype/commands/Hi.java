package org.mcsg.bot.skype.commands;

import java.util.Random;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Hi implements SubCommand{

	String [] msg = {"Hi", "Hello", "How are you", "Greetings", "Pickles", "Whats up?", "Hi person" };
	
	
	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		chat.send(msg[new Random().nextInt(msg.length)]);
	}

	@Override
	public String getHelp() {
		return "Greetings";
	}

	@Override
	public String getUsage() {
		return ".hi";
	}

}
