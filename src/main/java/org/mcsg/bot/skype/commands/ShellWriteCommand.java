package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Permissions;
import org.mcsg.bot.skype.util.ShellCommand;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class ShellWriteCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        if (Permissions.hasPermission(sender, chat, "out")) {
            StringBuilder sb = new StringBuilder();
            for (int a = 1; a < args.length; a++) {
                sb.append(args[a]).append(" ");
            }
            ShellCommand.writeStream(Integer.parseInt(args[0]), sb.toString());
        } else {
            chat.sendMessage("No permission");
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
        return "out";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
