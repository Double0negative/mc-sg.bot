package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Permissions;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class Permission implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        if (sender.getUsername().equals("drew.foland")) {
            if (args[0].equals("add")) {
                Permissions.addPerm(args[1], chat.getIdentity(), args[2]);
                chat.sendMessage("Adding permission");
            } else if (args[0].equals("all")) {
                for (User user : chat.getAllUsers()) {
                    Permissions.addPerm(user, chat, args[1]);
                }
                chat.sendMessage("Adding permission");
            } else {
                Permissions.removePerms(args[1], chat.getIdentity(), args[2]);
                chat.sendMessage("Removing permission");
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
        return "perm";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
