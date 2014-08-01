package org.mcsg.bot.skype.commands;

import com.skype.Chat;
import com.skype.User;

public abstract class SubCommandHelper implements SubCommand {

    public final void execute(Chat chat, User sender, String[] args) throws Exception {
        if (!executeCommand(chat, sender, args)) chat.send("Usage: " + (this.getUsage() != null ? this.getUsage() : "Null"));
    }

    public abstract boolean executeCommand(Chat chat, User sender, String args[]) throws Exception;

    public abstract String getCommand();

    public abstract String[] getAliases();

    public abstract String getHelp();

    public abstract String getUsage();


}
