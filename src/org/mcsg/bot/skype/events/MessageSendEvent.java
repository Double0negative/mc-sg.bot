package org.mcsg.bot.skype.events;


/**
 * Called when a message is about to be sent by the chat manager before it
 * is actually sent to chat. 
 * @author drew
 *
 */
public  class MessageSendEvent implements Event{

  private String message;
  private boolean cancel;
  
  public MessageSendEvent(String message){
    this.setMessage(message);
  }
  
  public String getMessage(){
    return message;
  }
  
  public void setMessage(String message){
    this.message = message;
  }
  
  public void setCancelled(boolean bol){
    this.cancel = bol;
  }
  
  public boolean isCancelled(){
    return cancel;
  }
  
}
