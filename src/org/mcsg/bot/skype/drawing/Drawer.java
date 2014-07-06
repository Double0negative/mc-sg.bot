package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

abstract class Drawer {
	
	protected BufferedImage img;
	protected Graphics2D g;
	protected static Random rand = new Random();
	
	protected Color color;

	protected int WIDTH;
	protected int HEIGHT;
	
	public Drawer(int width, int height, BufferedImage img,  Graphics2D g){
		this.HEIGHT = height;
		this.WIDTH = width;
		this.img = img;
		this.g = g;
	}
	
	public abstract void draw(String ... args);
	
	
	public static Color getRandomColor(boolean a){
		return new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255),!a ? 255 : rand.nextInt(255));
	}
	
	public void setRandomColor(boolean a){
		g.setColor(getRandomColor(a));
	}

	
}