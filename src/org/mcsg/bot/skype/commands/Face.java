package org.mcsg.bot.skype.commands;

import java.util.Random;

import org.mcsg.bot.skype.util.ChatManager;

import com.skype.Chat;
import com.skype.User;

public class Face implements SubCommand{

	public static final String faces[] = {".0.", ".O.", "._.", ".-.", ":o", "o;", "O:", "O_O", "o_o", "o.o", "O.O" };
	
	
	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		ChatManager.chat(chat, faces[new Random().nextInt(faces.length)]);
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}
}
