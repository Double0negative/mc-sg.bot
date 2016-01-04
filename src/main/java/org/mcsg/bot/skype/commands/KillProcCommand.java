package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.Permissions;
import org.mcsg.bot.skype.util.ShellCommand;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class KillProcCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        if (Permissions.hasPermission(sender, chat, "killproc")) {
            if (args.length == 2) {
                ShellCommand.forceKill(Integer.parseInt(args[0]));
            } else {
                ShellCommand.kill(Integer.parseInt(args[0]));
            }
            chat.sendMessage("Killed proccess " + args[0]);
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
        return "killproc";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
