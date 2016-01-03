package org.mcsg.bot.skype.events;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;

/**
 * Called after a message is sent by the bot
 * @author drew
 *
 */
public class MessageSentEvent implements Event {

    private Chat chat;
    private ChatMessage message;
    private String content;

    public MessageSentEvent(ChatMessage message, Chat chat, String content) {
        this.chat = chat;
        this.message = message;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

}
