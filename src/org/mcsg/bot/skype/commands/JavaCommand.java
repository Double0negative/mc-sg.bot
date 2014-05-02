package org.mcsg.bot.skype.commands;

import java.io.File;

import org.mcsg.bot.skype.util.Arguments;
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
			String template = "fES8cVZX";
			
			Arguments arge = new Arguments(args, "template/temp/t args", "paste/p args", "nolimit", "limit/l args");
			args = arge.getArgs();
			
			
			
			String input = StringUtils.implode(args);
			if(args[0].equals("-template")){
				template = args[1];
				input = StringUtils.implode(2, args);
			}
			boolean parse = false;
			String urlid = "";
			if(input.contains("pastebin.com")){
				urlid = input.substring(input.indexOf("pastebin.com/")+"pastebin.com/".length());	
			} else {
				urlid = template;
				parse = true;
			}

			String code = WebClient.request("http://pastebin.com/raw.php?i="+urlid);
			if(parse){
				code = code.replace("$code", input);
			}
			int cindex = code.indexOf("class") + "class ".length();
			String name = code.substring(cindex, code.indexOf(" ", cindex)).trim()+"";

			File javaf = new File("files/",name+".java");
			File javac = new File("files/",name+".class");

			FileUtils.writeFile(javaf, code);
			int id = ShellCommand.exec(chat, "cd files; javac -classpath \"../java_libs/*:\" "+name+".java; java -classpath \"../java_libs/*:\" "+name, 20000, false);

			ChatManager.chat(chat, "Running java code. ID "+id+". Code: "+ ChatManager.createPaste(code));

			javaf.deleteOnExit();
			javac.deleteOnExit();
		} else {
			chat.send("No permission to execute command");
		}
	}


	public String getPasteLink(String url){
		String gist = "http://gist.githubusercontent.com/$id/raw";
		String paste = "http://pastebin.com/raw.php?i=$id";

		String id = "";

		if(url.contains("pastebin")){
			id = url.substring(url.lastIndexOf("/"));
			return paste.replace("$id", id);
		} else if(url.contains("github")){
			id = url.substring(url.indexOf("/", 7));
			return gist.replace("$id", id);
		}
		return "";
	}

	@Override
	public String getHelp() {
		return "Run a java command";
	}

	@Override
	public String getUsage() {
		return ".java [-template] <code|pastebin>";
	}


}
