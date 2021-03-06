package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.web.DuckDuckGo;
import org.mcsg.bot.skype.web.DuckDuckGo.DuckDuckResult;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class WebAbstractCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        DuckDuckResult result = DuckDuckGo.search(StringUtils.implode(args));
        String abstractt = result.AbstractText;
        if (abstractt != null && !abstractt.equals("")) {
            ChatManager.chat(chat, sender, abstractt);
            ChatManager.chat(chat, sender, " -" + result.AbstractSource);
        } else {
            ChatManager.chat(chat, sender, "No abstract for " + StringUtils.implode(args));
        }
    }

    @Override
    public String getHelp() {
        return "Get an abstract";
    }

    @Override
    public String getUsage() {
        return ".abstract <search>";
    }

    @Override
    public String getCommand() {
        return "abstract";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
