package org.mcsg.bot.skype.util;

public class ThreadUtil {

	public static void run(final String name, final Runnable r){
		Thread t = new Thread(name){
			public void run(){
				r.run();
			}
		};
		t.start();
	}
}