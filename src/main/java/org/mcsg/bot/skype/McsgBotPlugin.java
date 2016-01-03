package org.mcsg.bot.skype;

import com.samczsun.skype4j.chat.Chat;

public interface McsgBotPlugin {
  
  public String getName();
  
  public void onEnable(Chat chat) throws Exception;
  
  public void onDisable() throws Exception;
  
}
