package org.mcsg.bot.skype.events;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.User;

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
  
  public MessageReceivedEvent(ChatMessage message, Chat chat, User user, String content){
    this.chat = chat;
    this.user = user;
    this.message = message;
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
  
  public String getContent(){
    return content;
  }

  
}
