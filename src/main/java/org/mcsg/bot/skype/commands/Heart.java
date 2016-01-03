package org.mcsg.bot.skype.commands;

import java.util.Random;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.web.Google;
import org.mcsg.bot.skype.web.Google.GoogleResult;
import org.mcsg.bot.skype.web.Google.Results;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class Heart implements SubCommand {

    @Override
    public void execute(Chat chat, User sender, String[] args) throws Exception {
        GoogleResult result = Google.search("images", "heart love", new Random().nextInt(60));
        Results[] results = result.responseData.results;
        if (results != null && results.length > 0) {
            ChatManager.chat(chat, sender, results[0].url);
        }
    }

    @Override
    public String getHelp() {
        return "I love you";
    }

    @Override
    public String getUsage() {
        return ".<3";
    }

    @Override
    public String getCommand() {
        return "<3";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
