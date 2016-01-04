package org.mcsg.bot.skype.commands;

import java.util.Random;

import org.mcsg.bot.skype.util.StringUtils;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class IsCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        if (new Random(StringUtils.implode(args).hashCode()).nextBoolean()) {
            chat.sendMessage("Yes");
        } else {
            chat.sendMessage("No");
        }

    }

    @Override
    public String getHelp() {
        return "Is it?";
    }

    @Override
    public String getUsage() {
        return ".is <query>";
    }

    @Override
    public String getCommand() {
        return "is";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
