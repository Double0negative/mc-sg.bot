package org.mcsg.bot.skype.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.mcsg.bot.skype.drawing.PictureDraw;
import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.HttpHeader;
import org.mcsg.bot.skype.util.WebClient;
import org.mcsg.bot.skype.web.ImgurUpload;

import com.skype.Chat;
import com.skype.User;

public class GenImage implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		PictureDraw draw = new PictureDraw();
		draw.draw();

		ChatManager.chat(chat, ImgurUpload.upload(draw.getBytes()));
		
		
		
	}

	
	@Override
	public String getHelp() {
		return "Generate a random image";
	}

	@Override
	public String getUsage() {
		return ".genimage";
	}

}
