package org.mcsg.bot.skype.util;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ThreadPool {

	private static ExecutorService pool;
	
	/**
	 * Execute a async task at some point in the future
	 * from a dedicated thread pool
	 * @param <T>
	 * @return 
	 * 
	 */
	public static Future<?> submit(Runnable r){
		if(pool == null){
			pool = Executors.newFixedThreadPool(16);
		}
		pool.execute(r);
		return null;
	}
}