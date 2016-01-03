package org.mcsg.bot.skype.commands;

import java.util.HashMap;

import org.mcsg.bot.skype.Permissions;
import org.mcsg.bot.skype.Settings;
import org.mcsg.bot.skype.util.Arguments;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class ManageChat implements SubCommand {

    @Override
    public void execute(Chat chat, User sender, String[] args) throws Exception {
        Arguments arge = new Arguments(args, "seconds/sec/time/t args", "paste/amount/a args",
                "pastemethod/method/m args");
        HashMap<String, String> swi = arge.getSwitches();
        args = arge.getArgs();

        if (Permissions.hasPermission(sender, chat, "chatmanage")) {
            if (swi.containsKey("seconds")) {
                int time = Integer.parseInt(swi.get("seconds"));
                Settings.Root.Bot.chat.time = time;
            }
            if (swi.containsKey("paste")) {
                int paste = Integer.parseInt(swi.get("paste"));
                Settings.Root.Bot.chat.paste = paste;
            }
            if (swi.containsKey("pastemethod")) {
                Settings.Root.Bot.chat.pastemethod = swi.get("pastemethod");
            }
            Settings.save();
            chat.sendMessage("Set settings");
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
        return "setchat";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
