package org.mcsg.bot.skype.commands;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

public class GetUsers implements SubCommand{

	@Override
	public void execute(Chat chat, User sender, String[] args)
			throws SkypeException {
		StringBuilder sb = new StringBuilder();
		for(User user : chat.getAllMembers()){
			if(args.length == 0){
				sb.append(user.getId()+":"+user.getFullName()+" ");
			} else {
				sb.append(user.getId() + " ");

			}
		}
		chat.send(sb.toString());
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
