package org.mcsg.bot.skype.util;


import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class ProgressBar {

	private static final int LENGTH = 20;
	
	private String title;
	private Chat chat;
	private ChatMessage msg;
	private int percent;
	
	public ProgressBar(String title, Chat chat) throws SkypeException{
		this.title = title;
		this.chat = chat;
		setProgress(0);
	}
	
	public void setProgress(int percent) throws SkypeException{
		this.percent = percent;
		
			updateMessage();
	}
	
	public void finish(String message){
		try{
			msg.setContent(message);
		} catch (Exception e){}
	}
	
	private void updateMessage() throws SkypeException{
		if(msg == null){
			msg = chat.send(title + " ["+getProgressString()+"] "+ percent +"%");
		} else {
			msg.setContent(title + " ["+getProgressString()+"] "+ percent +"%");
		}
	}
	
	private String getProgressString(){
		StringBuilder sb = new StringBuilder();
		int per = percent / (100 / LENGTH);
		for(int a = 0; a < LENGTH; a++){
			sb.append(a < per ? "=" : "_");
		}
		return sb.toString();
	}

	public void setProgress(double percent) throws SkypeException {
		this.percent = (int) (percent * 100);
		updateMessage();
	}

	public void setTitle(String string) {
		this.title = string;
	}
}
