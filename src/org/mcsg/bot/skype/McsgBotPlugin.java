package org.mcsg.bot.skype;

public interface McsgBotPlugin {

  public String getName();
  
  public void onEnable() throws Exception;
  
  public void onDisable() throws Exception;
  
}
