package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Permissions;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class Kick implements SubCommand {

    @Override
    public void execute(Chat chat, User sender, String[] args) throws Exception {
        if (Permissions.hasPermission(sender, chat, "kick")) {
            for (String arg : args) {
                chat.sendMessage("/kick " + arg);
            }
        }
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

    @Override
    public String getCommand() {
        return "kick";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
