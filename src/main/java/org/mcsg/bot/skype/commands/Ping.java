package org.mcsg.bot.skype.commands;

import java.net.InetAddress;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.util.ShellCommand;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.user.User;

public class Ping implements SubCommand {

    @Override
    public void execute(Chat chat, User sender, String[] args) throws SkypeException {
        if (args.length == 0) {
            chat.sendMessage("Pong!");
        } else if (args.length == 1) {
            try {
                InetAddress addr = InetAddress.getByName(args[0]);
                ShellCommand.exec(chat, "printf 'Ping " + args[0] + " (" + addr.getHostAddress()
                        + "). Average time ';ping -c 5 " + addr.getHostAddress()
                        + "| tail -1| awk '{print $4}' | cut -d '/' -f 2| awk '{printf \"%sms\", $1}'", 0, false);

            } catch (Exception e) {
                ChatManager.chat(chat, sender, "Failed to resolve " + args[0]
                        + ". Host does not exist or is not reachable");
            }
        }
    }

    @Override
    public String getHelp() {
        return "PONG!";
    }

    @Override
    public String getUsage() {
        return ".ping [server]";
    }

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
