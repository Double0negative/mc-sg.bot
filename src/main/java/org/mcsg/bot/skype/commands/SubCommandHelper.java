package org.mcsg.bot.skype.commands;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public abstract class SubCommandHelper implements SubCommand {

    public final void execute(Chat chat, User sender, String[] args) throws Exception {
        if (!executeCommand(chat, sender, args)) {
            chat.sendMessage(getCommand() + " - " + getUsage());
        }

    }

    public abstract boolean executeCommand(Chat chat, User sender, String args[]) throws Exception;

    public abstract String getCommand();

    public abstract String[] getAliases();

    public abstract String getHelp();

    public abstract String getUsage();

}
