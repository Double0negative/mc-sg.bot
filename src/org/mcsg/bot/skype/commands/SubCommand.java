package org.mcsg.bot.skype.commands;

import com.skype.Chat;
import com.skype.User;

public interface SubCommand {

  /**
   * Execute command
   * @param chat
   * @param sender
   * @param args
   * @throws Exception
   */
	public void execute(Chat chat, User sender, String [] args) throws Exception;
	
	/**
	 * Get the command name, this is what will be used as the command 
	 * @return The command
	 */
	public String getCommand();
	
	/**
	 * Alternate forms of this command
	 * @return Array of commands
	 */
	public String[] getAliases();
	
	
	/**
	 * Explain what this command does
	 * @return
	 */
	public String getHelp();
	
	/**
	 * Get command usage for this command in MD format
	 * @return
	 */
	public String getUsage();
	
	/**
	 * Helper method for creating array
	 * @param a
	 * @return
	 */
	public default String [] a(String ... a){
	  return a;
	}
}
