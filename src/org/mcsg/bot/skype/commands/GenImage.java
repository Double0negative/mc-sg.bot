package org.mcsg.bot.skype.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.mcsg.bot.skype.drawing.PictureDraw;
import org.mcsg.bot.skype.util.Arguments;
import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.HttpHeader;
import org.mcsg.bot.skype.util.Settings;
import org.mcsg.bot.skype.util.WebClient;
import org.mcsg.bot.skype.web.ImgurUpload;
import org.mcsg.bot.skype.web.McsgUpload;

import com.skype.Chat;
import com.skype.User;

public class GenImage implements SubCommand{

	HashMap<String, Integer> gens = new HashMap<String, Integer>();
	HashMap<String, String> res = new HashMap<String, String>();
	public GenImage(){
		gens.put("shapes", 0);
		gens.put("lines", 1);
		gens.put("circles", 2);
		gens.put("circlelines", 3);
		gens.put("smoke", 4);


	}

	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		Arguments arge = new Arguments(args, "generator/gen args", "base/background arg", "resolution/res arg");
		args = arge.getArgs();
		HashMap<String, String> swi = arge.getSwitches();


		PictureDraw draw = null;
		if(swi.containsKey("resolution")){
			String res [] = swi.get("resolution").split("x");
			if(res.length == 2)
				draw = new PictureDraw(Integer.parseInt(res[0]), Integer.parseInt(res[1]));
			else 
				draw = new PictureDraw();
		} else 
			draw = new PictureDraw();

		new PictureDraw();
		draw.draw(swi.containsKey("generator") ? gens.getOrDefault(swi.get("generator"), -1) : -1);

		if(Settings.Root.Image.IMAGE_UPLOAD_METHOD.equals("mcsg")){
			ChatManager.chat(chat, McsgUpload.upload(draw.getBytes()));
		} else {
			ChatManager.chat(chat, ImgurUpload.upload(draw.getBytes()));
		}



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
