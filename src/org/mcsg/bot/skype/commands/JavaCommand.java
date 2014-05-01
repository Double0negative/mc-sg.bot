package org.mcsg.bot.skype.commands;

import java.io.File;

import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.FileUtils;
import org.mcsg.bot.skype.util.Permissions;
import org.mcsg.bot.skype.util.ShellCommand;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.util.WebClient;

import com.skype.Chat;
import com.skype.User;

public class JavaCommand implements SubCommand {

	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		if(Permissions.hasPermission(sender, chat, "java")){
			String input = StringUtils.implode(args);
			boolean parse = false;
			if(input.contains("pastebin.com")){
				input = input.substring(input.indexOf("pastebin.com/")+"pastebin.com/".length());	
			} else {
				input = "fES8cVZX";
				parse = true;
			}
			
			String code = WebClient.request("http://pastebin.com/raw.php?i="+input);
			if(parse){
				code = code.replace("$code", StringUtils.implode(args));
			}
			int cindex = code.indexOf("class") + "class ".length();
			String name = code.substring(cindex, code.indexOf(" ", cindex)).trim()+"";

			File javaf = new File(name+".java");
			File javac = new File(name+".class");

			FileUtils.writeFile(javaf, code);
			int id = ShellCommand.exec(chat, "javac "+javaf.getName()+"; java "+name, 20000, false);

			ChatManager.chat(chat, "Running java code. ID "+id+". Code: "+ ChatManager.createPaste(code));
			
			javaf.deleteOnExit();
			javac.deleteOnExit();
		}
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return null;
		
	}

	
}
