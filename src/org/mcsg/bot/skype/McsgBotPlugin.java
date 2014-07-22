package org.mcsg.bot.skype;

import com.skype.Chat;

public interface McsgBotPlugin {
  
  public String getName();
  
  public void onEnable(Chat chat) throws Exception;
  
  public void onDisable() throws Exception;
  
}
