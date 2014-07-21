package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.mcsg.bot.skype.util.MapWrapper;
import org.mcsg.bot.skype.util.Progress;

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

	public abstract void draw(Progress<Integer> prog, MapWrapper args);

	
	
	public static Color getRandomColor(boolean a){
		return new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255),!a ? 255 : rand.nextInt(255));
	}
	
	public void setRandomColor(boolean a){
		g.setColor(getRandomColor(a));
	}

	public void drawCircle(int x, int y, int size){
		g.fillOval(x - size /2 , y - size /2, size, size);
	}
	
	public Color brigher(Color color, int inc){
	  return incColor(color, inc, inc, inc, 0);
	}
	
	public Color darker(Color color, int inc){
	  return incColor(color, -inc, -inc, -inc, 0);
	}
	
	private Color incColor(Color color, int rinc, int binc, int ginc, int ainc){
	   int r = color.getRed();
	    int g = color.getGreen();
	    int b = color.getBlue();
	    int a = color.getAlpha();
	    r += rinc;
	    b += binc;
	    g += ginc;
	    a += ainc;
	    r = ImageTools.limit(r, 255, 0); 
	    b = ImageTools.limit(b, 255, 0); 
	    g = ImageTools.limit(g, 255, 0); 
	    a = ImageTools.limit(a, 255, 0); 
	    return new Color(r, g, b, a);
	}
	
	public Color incColor(Color color, int inc){
    return incColor(color, (rand.nextInt(inc * 2) - inc), rand.nextInt(inc * 2) - inc, 
        rand.nextInt(inc * 2) - inc, rand.nextInt(inc * 2) - inc);
	}
	
}