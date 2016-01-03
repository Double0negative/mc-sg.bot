package org.mcsg.bot.skype.events;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import com.samczsun.skype4j.user.User;

/**
 * Called when a message is received.
 * @author drew
 *
 */
public class MessageReceivedEvent implements Event {

    private Chat chat;
    private User user;
    private String content;
    private ChatMessage message;

    public MessageReceivedEvent(ChatMessage message) {
        this.chat = message.getChat();
        this.user = message.getSender();
        this.message = message;
        this.content = message.getContent().asPlaintext();
    }

    public Chat getChat() {
        return chat;
    }

    public User getUser() {
        return user;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public String getContent() {
        return content;
    }

}
