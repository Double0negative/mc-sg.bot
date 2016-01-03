package org.mcsg.bot.skype.util;

public class Point {

	public Point(Point point){
		this(point.getX(), point.getY());
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void incX(int x){
		this.x += x;
	}
	
	public void incY(int y){
		this.y += y;
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
	
	public int distance(Point pnt){
		return distance(pnt.getX(), pnt.getY());
	}
	
	
}
