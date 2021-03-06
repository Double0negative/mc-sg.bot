package org.mcsg.bot.skype.commands;

import java.util.Random;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.web.Google;
import org.mcsg.bot.skype.web.Google.GoogleResult;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class ImageSearchCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        String search = "";
        int start = 0;
        if (args[0].equals("-rand")) {
            search = StringUtils.implode(1, args);
            start = new Random().nextInt(50);
        } else if (args[0].equals("-result") || args[0].equals("-start") || args[0].equals("-num")) {
            search = StringUtils.implode(2, args);
            start = Integer.parseInt(args[1]) - 1;
        } else {
            search = StringUtils.implode(args);
        }
        GoogleResult result = Google.search("images", search, start);
        System.out.println(result.responseData);
        org.mcsg.bot.skype.web.Google.Results[] results = result.responseData.results;
        if (results != null && results.length > 0) {
            ChatManager.chat(chat, sender, start + 1 + ". " + results[0].unescapedUrl);
            ChatManager.chat(chat, sender, results[0].titleNoFormatting);
        } else {
            ChatManager.chat(chat, sender, "No results for " + StringUtils.implode(args));
        }

    }

    @Override
    public String getHelp() {
        return "Search google images";
    }

    @Override
    public String getUsage() {
        return ".img <search>";
    }

    @Override
    public String getCommand() {
        return "img";
    }

    @Override
    public String[] getAliases() {
        return a("image", "i");
    }

}
