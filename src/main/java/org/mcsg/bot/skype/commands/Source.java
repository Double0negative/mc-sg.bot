package org.mcsg.bot.skype.commands;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.user.User;

public class Source implements SubCommand {

    @Override
    public void execute(Chat chat, User sender, String[] args) throws SkypeException {
        // chat.send("https://bitbucket.org/mcsg/mc-sg.bot");
        chat.sendMessage("https://github.com/Double0negative/mc-sg.bot");
    }

    @Override
    public String getHelp() {
        return "Get the source";
    }

    @Override
    public String getUsage() {
        return ".src";
    }

    @Override
    public String getCommand() {
        return "src";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
