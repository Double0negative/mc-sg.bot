package org.mcsg.bot.skype.util;

import java.util.concurrent.atomic.AtomicLong;

public class Progress<T> {

	private Object wait = new Object();

	private String message;
	
	private AtomicLong max = new AtomicLong(100);
	private AtomicLong prog = new AtomicLong(0); 
	private volatile boolean finished;
	T result;


	public void setMax(long max){
		this.max.set(max);;
	}
	
	public double getMax(){
		return max.get();
	}
	
	public void setProgress(long prog){
		this.prog.set(prog);
	}
	
	public void incProgress(long inc){
		this.prog.addAndGet(inc);
	}
	
	public long getProgress(){
		return this.prog.get();
	}

	public  double getPercent(){
		return (prog.get() + 0.0) / (max.get() + 0.0);
	}
	
	public void waitForFinish(){
		synchronized (wait) {
			try {
				wait.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}


	public void finish(T result){
		this.result = result;
		finish();
	}

	public void finish(){
		this.finished = true;
		synchronized (wait) {
			wait.notify();
		}
	}

	public boolean isFinished(){
		return finished;
	}

	public T getResult(){
		return result;
	}


}
