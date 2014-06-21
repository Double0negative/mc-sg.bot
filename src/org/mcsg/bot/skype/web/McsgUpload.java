package org.mcsg.bot.skype.web;

import java.util.Base64;

import org.mcsg.bot.skype.util.Settings;
import org.mcsg.bot.skype.util.WebClient;

public class McsgUpload {
	
	public static String upload(byte [] img){
		try {

			return WebClient.postArgs("http://mc-sg.org/bot/upload.php",  null,"img", Base64.getEncoder().encodeToString(img), "key", Settings.Root.Image.MCSG_UPLOAD_KEY );
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}

