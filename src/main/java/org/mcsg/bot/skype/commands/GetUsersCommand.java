package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.Permissions;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class GetUsersCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        if (Permissions.hasPermission(sender, chat, "getusers")) {
            StringBuilder sb = new StringBuilder();
            for (User user : chat.getAllUsers()) {
                if (args.length == 0) {
                    sb.append(user.getUsername() + ":" + user.getDisplayName() + " ");
                } else {
                    sb.append(user.getUsername() + " ");

                }
            }
            ChatManager.chat(chat, sb.toString());
        }
    }

    @Override
    public String getHelp() {
        return "Get all users in a chat";
    }

    @Override
    public String getUsage() {
        return ".getusers [-noid]";
    }

    @Override
    public String getCommand() {
        return "getusers";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
