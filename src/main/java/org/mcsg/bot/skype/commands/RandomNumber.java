package org.mcsg.bot.skype.commands;

import java.util.Random;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class RandomNumber implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        if (args.length == 0) {
            chat.sendMessage(new Random().nextInt() + "");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("double")) {
                chat.sendMessage(new Random().nextDouble() + "");
            } else if (args[0].equalsIgnoreCase("boolean")) {
                chat.sendMessage(new Random().nextBoolean() + "");
            } else if (args[0].equalsIgnoreCase("int")) {
                chat.sendMessage(new Random().nextInt() + "");
            } else if (args[0].equalsIgnoreCase("long")) {
                chat.sendMessage(new Random().nextLong() + "");
            }
        } else if (args.length == 2) {
            chat.sendMessage(new Random().nextInt(Integer.parseInt(args[1])) + "");
        } else if (args.length == 3) {
            chat.sendMessage(((int) new Random().nextDouble() * Integer.parseInt(args[1]) - Integer.parseInt(args[2]))
                    + "");
        }

    }

    @Override
    public String getHelp() {
        return "Get a random";
    }

    @Override
    public String getUsage() {
        return ".rand [type]";
    }

    @Override
    public String getCommand() {
        return "rand";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
