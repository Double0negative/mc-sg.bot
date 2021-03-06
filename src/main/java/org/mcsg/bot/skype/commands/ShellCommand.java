package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Permissions;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class ShellCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        if (Permissions.hasPermission(sender, chat, "sh")) {
            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                sb.append(arg).append(" ");
            }
            org.mcsg.bot.skype.util.ShellCommand.exec(chat, sb.toString());
            // ShellCommand.exec(chat, args);
        } else {
            chat.sendMessage("No permissions to execute command");
        }
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public String getCommand() {
        return "sh";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
