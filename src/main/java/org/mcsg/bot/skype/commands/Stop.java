package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Permissions;
import org.mcsg.bot.skype.Settings;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class Stop implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        if (Permissions.hasPermission(sender, chat, "stop")) {
            chat.sendMessage("Stopping...");
            Settings.Root.Bot.lastchat = chat.getIdentity();
            Settings.save();
            System.exit(0);
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
        return "stop";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
