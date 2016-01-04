package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Permissions;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class LeaveCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        if (Permissions.hasPermission(sender, chat, "leave")) {
            chat.sendMessage("/leave");
        } else {
            chat.sendMessage("NO YOU LEAVE");
            chat.sendMessage("/kick " + sender.getUsername());
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
        return "leave";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
