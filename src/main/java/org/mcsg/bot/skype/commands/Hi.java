package org.mcsg.bot.skype.commands;

import java.util.Random;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.user.User;

public class Hi implements SubCommand {

    String[] msg = { "Hi", "Hello", "How are you", "Greetings", "Pickles", "Whats up?", "Hi person" };

    @Override
    public void execute(Chat chat, User sender, String[] args) throws SkypeException {
        chat.sendMessage(msg[new Random().nextInt(msg.length)]);
    }

    @Override
    public String getHelp() {
        return "Greetings";
    }

    @Override
    public String getUsage() {
        return ".hi";
    }

    @Override
    public String getCommand() {
        return "hi";
    }

    @Override
    public String[] getAliases() {
        return a("hello");
    }

}
