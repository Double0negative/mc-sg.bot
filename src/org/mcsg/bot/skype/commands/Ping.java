package org.mcsg.bot.skype.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.mcsg.bot.skype.util.MinecraftPing;
import org.mcsg.bot.skype.util.ShellCommand;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class Ping implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args) throws SkypeException {
		if(args.length == 0){
			chat.send("Pong!");
		} else if (args.length == 1){
			ShellCommand.exec(chat, "ping -c 5 -q "+args[0]);
		}
	}

	@Override
	public String getHelp() {
		return "PONG!";
	}

	@Override
	public String getUsage() {
		return ".ping [server]";
	}

}
