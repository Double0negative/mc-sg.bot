package org.mcsg.bot.skype.util;

import com.samczsun.skype4j.chat.Chat;

public abstract class ShellStreamReader {

    public enum StreamType {
        INPUT(""), ERROR("ERR: ");

        private String prefix;

        private StreamType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    private Chat chat;

    public abstract void read(StreamType inputType, String msg);

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

}
