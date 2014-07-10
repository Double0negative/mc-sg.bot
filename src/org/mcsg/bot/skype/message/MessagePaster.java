package org.mcsg.bot.skype.message;

import java.util.HashMap;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.web.GistPaster;
import com.skype.Chat;
import com.skype.User;

public class MessagePaster {

	private static HashMap<String, String> prefix = new HashMap<>();


	static {
		prefix.put("java", "java");
		prefix.put("ruby", "rb");
		prefix.put("txt", "txt");
		prefix.put("python", "py");
		prefix.put("html", "html");
		prefix.put("css", "css");
		prefix.put("c++", "cpp");
		prefix.put("c", "c");
		prefix.put("json", "json");
	}


	public static void message(User user, Chat chat, String message) throws Exception{
		boolean paste = false;
		String pre = "";
		String prepre = "";
		if(prefix.containsKey(prepre = message.trim().substring(0, message.trim().indexOf(" ")).toLowerCase())){
			paste = true;
			pre = prefix.get(prepre);
			message = message.replaceFirst(prepre, "");
			message = message.replace("\t", "  ").trim();
		}else if(message.length() > 250 || StringUtils.countOff(message, "\n") > 5 ||
				message.contains("{") && message.contains("}")){
			paste = true;
			if(message.contains("{") && message.contains("}")){
					pre = "java";
			} else {
				pre = "txt";
			}
		}
		if(paste){
			String url = GistPaster.paste("mcsg-bot."+pre, message);
			chat.send(user.getFullName() +", I automatically created a paste for your message: "+ url +". You can now remove your message to save space.");
		}
	}




}
