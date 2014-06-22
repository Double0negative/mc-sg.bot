package org.mcsg.bot.skype.util;

public class Point {

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	int x,y;
	
	public int distance(int x, int y){
		return (int) Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
	}
	
	
}
