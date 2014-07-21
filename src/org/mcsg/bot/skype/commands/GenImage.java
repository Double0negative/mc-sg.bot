package org.mcsg.bot.skype.commands;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.Settings;
import org.mcsg.bot.skype.drawing.PictureDraw;
import org.mcsg.bot.skype.util.Arguments;
import org.mcsg.bot.skype.util.Progress;
import org.mcsg.bot.skype.util.ProgressBar;
import org.mcsg.bot.skype.util.ProgressChatMessage;
import org.mcsg.bot.skype.web.HttpHeader;
import org.mcsg.bot.skype.web.ImgurUpload;
import org.mcsg.bot.skype.web.McsgUpload;
import org.mcsg.bot.skype.web.WebClient;

import com.skype.Chat;
import com.skype.User;

public class GenImage implements SubCommand{

	HashMap<String, Integer> gens = new HashMap<String, Integer>();
	HashMap<String, String> res = new HashMap<String, String>();
	public GenImage(){
		gens.put("shapes", 0);
		gens.put("lines", 1);
		gens.put("circles", 2);
		gens.put("smallcircles", 3);
		gens.put("smoke", 4);
		gens.put("cluster", 5);
		gens.put("dots", 6);
		gens.put("clouds", 7);
    gens.put("abstract", 8);

		gens.put("pixel", 25);

	}

	
	
	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		
		Arguments arge = new Arguments(args, "generator/gen args", "base/background arg", "resolution/res arg");
		args = arge.getArgs();
		HashMap<String, String> swi = arge.getSwitches();
		ProgressBar bar = new ProgressBar("@"+sender.getId() + " genimg - Downloading Base", chat);
		ProgressChatMessage progmsg = new ProgressChatMessage(bar);
		
		BufferedImage base = null;
		if(swi.containsKey("base")){
			Progress<byte[]> imgdl = WebClient.requestByteProgress(chat, swi.get("base"));
			progmsg.setProgress(imgdl).doWait();
			
			base = ImageIO.read(new ByteArrayInputStream(imgdl.getResult()));
		}
		
		bar.setTitle("@"+sender.getId() + " genimg - Generating");
		bar.setProgress(0); //dont have a good wait to determine this yet,,,
		
		PictureDraw draw = null;
		if(swi.containsKey("resolution")){
			String res [] = swi.get("resolution").split("x");
			if(res.length == 2)
				draw = new PictureDraw(Integer.parseInt(res[0]), Integer.parseInt(res[1]), base);
			else 
				draw = new PictureDraw(base);
		} else 
			draw = new PictureDraw(base);

		Progress<Integer> progi = draw.draw(swi.containsKey("generator") ? gens.getOrDefault(swi.get("generator"), -1) : -1, args);
		progmsg.setProgress(progi).doWait("@"+sender.getId() + " genimg - Generating");
		
		
		bar.setProgress(0);
		if(Settings.Root.Image.IMAGE_UPLOAD_METHOD.equals("mcsg")){
			Progress<String> prog = McsgUpload.upload(chat, draw.getBytes());
			progmsg.setProgress(prog).doWait("@"+sender.getId() + " genimg - ");
			bar.finish(prog.getResult());
		} else {
			ChatManager.chat(chat, ImgurUpload.upload(chat, draw.getBytes()));
		}



	}


	
	
	
	@Override
	public String getHelp() {
		return "Generate a random image";
	}

	@Override
	public String getUsage() {
		return ".genimage [-gen generator] [-res reseloution] [-base baseimg] [option:value]";
	}



  @Override
  public String getCommand() {
    return "genimg";
  }



  @Override
  public String[] getAliases() {
    return a("genimage", "background");
  }

}
