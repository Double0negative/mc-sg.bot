package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Permissions;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class HostCall implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws Exception {
		if(Permissions.hasPermission(sender, chat, "host")){
			chat.send("Hosting call.");
			chat.send("/golive");
		} else {
			chat.send("No Permission");
		}
	}

	@Override
	public String getHelp() {
		return "Host a Skype call";
	}

	@Override
	public String getUsage() {
		return ".host";
	}

  @Override
  public String getCommand() {
   return "host";
  }

  @Override
  public String[] getAliases() {
    // TODO Auto-generated method stub
    return null;
  }

}
