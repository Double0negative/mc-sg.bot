package org.mcsg.bot.skype.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.mcsg.bot.skype.util.Arguments;
import org.mcsg.bot.skype.util.ChatManager;
import org.mcsg.bot.skype.util.MinecraftUUID;
import org.mcsg.bot.skype.util.MinecraftUUID.Profile;
import org.mcsg.bot.skype.util.UUIDAPI;

import com.skype.Chat;
import com.skype.User;

public class UUIDCommand implements SubCommand {

	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		Arguments arge = new Arguments(args, "daniel/denial/dd/d", "noformat/nofmt/no/n", "time/t");
		HashMap<String, String> swi = arge.getSwitches();
		args = arge.getArgs();
		
		long time = System.currentTimeMillis();
		
		if(swi.containsKey("daniel")){
			HashMap<String, String > uuids = UUIDAPI.getUUIDs(Arrays.asList(args));
			
			for(Entry<String, String> e : uuids.entrySet()){
				ChatManager.chat(chat,  sender,e.getKey()+"   "+e.getValue());
			}
		} else {
			boolean fmt = !swi.containsKey("noformat");
			Profile [] profiles = MinecraftUUID.getUUIDs(args);
			
			for(Profile profile : profiles){
				ChatManager.chat(chat, sender,profile.name + "   " + ((fmt) ? profile.getUuid() : profile.uuid));
			}
		}
		
		if(swi.containsKey("time")){
			ChatManager.chat(chat,  sender, "time: "+(System.currentTimeMillis() - time)+"ms");
		}
		
	}

	@Override
	public String getHelp() {
		return "Get the UUID of a player or players";
	}

	@Override
	public String getUsage() {
		return ".uuid [-daniel/d|-noformat/n] <uuid>";
	}

}
