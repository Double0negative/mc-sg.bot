package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Bot;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class VersionCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        chat.sendMessage("MC-SG.BOT Version " + Bot.version);
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
        return "version";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

}
