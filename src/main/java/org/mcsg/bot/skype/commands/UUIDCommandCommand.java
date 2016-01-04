package org.mcsg.bot.skype.commands;

import java.util.HashMap;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.util.Arguments;
import org.mcsg.bot.skype.web.MinecraftUUID;
import org.mcsg.bot.skype.web.MinecraftUUID.Profile;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class UUIDCommandCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        Arguments arge = new Arguments(args, "daniel/denial/dd/d", "noformat/nofmt/no/n", "time/t");
        HashMap<String, String> swi = arge.getSwitches();
        args = arge.getArgs();

        long time = System.currentTimeMillis();

        boolean fmt = !swi.containsKey("noformat");
        Profile[] profiles = MinecraftUUID.getUUIDs(args);

        for (Profile profile : profiles) {
            ChatManager.chat(chat, sender, profile.name + "   " + ((fmt) ? profile.getUuid() : profile.uuid));
        }

        if (swi.containsKey("time")) {
            ChatManager.chat(chat, sender, "time: " + (System.currentTimeMillis() - time) + "ms");
        }

    }

    @Override
    public String getHelp() {
        return "Get the UUID of a player or players";
    }

    @Override
    public String getUsage() {
        return ".uuid [-daniel/d|-noformat/n] <uuid>";
    }

    @Override
    public String getCommand() {
        return "uuid";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
