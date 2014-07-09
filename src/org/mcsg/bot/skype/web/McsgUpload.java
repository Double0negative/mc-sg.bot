package org.mcsg.bot.skype.web;

import java.util.Base64;

import org.mcsg.bot.skype.util.Progress;
import org.mcsg.bot.skype.util.Settings;

import com.skype.Chat;

public class McsgUpload {




	public static Progress<String> upload(Chat chat, byte [] img){

		return WebClient.postArgsProgress(chat, "http://mc-sg.org/bot/upload.php",  null,"img", Base64.getEncoder().encodeToString(img), "key", Settings.Root.Image.MCSG_UPLOAD_KEY );


	}
}

