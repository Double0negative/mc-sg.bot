package org.mcsg.bot.skype.commands;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.web.DuckDuckGo;
import org.mcsg.bot.skype.web.DuckDuckGo.DuckDuckResult;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class DefineCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        DuckDuckResult result = DuckDuckGo.search(StringUtils.implode(args));
        String def = result.Definition;
        if (def != null && !def.equals("")) {
            ChatManager.chat(chat, sender, def);
            ChatManager.chat(chat, sender, result.DefinitionURL);
        } else {
            ChatManager.chat(chat, sender, "No definition for " + StringUtils.implode(args));
        }
    }

    @Override
    public String getHelp() {
        return "Define a word";
    }

    @Override
    public String getUsage() {
        return ".define <word>";
    }

    @Override
    public String getCommand() {
        return "define";
    }

    @Override
    public String[] getAliases() {
        // TODO Auto-generated method stub
        return null;
    }

}
