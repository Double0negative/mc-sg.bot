package org.mcsg.bot.skype.commands;


import java.util.Arrays;

import org.mcsg.bot.skype.util.Arguments;
import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;

import com.skype.Chat;
import com.skype.User;

public class ArgTest implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		Arguments arge = new Arguments(args, "test arg", "hello arg", "rand", "template/temp arg");
		for(String key : arge.getSwitches().keySet()){
			ChatManager.chat(chat, key +" : "+arge.getSwitches().get(key));
		}
		System.out.println(Arrays.toString(arge.getArgs()));
		ChatManager.chat(chat, StringUtils.
				implode(arge.getArgs()));
		
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
