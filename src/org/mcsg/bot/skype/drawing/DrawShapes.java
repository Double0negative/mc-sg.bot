package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

class DrawShapes extends Drawer {
	
	public DrawShapes(int width, int height, BufferedImage img, Graphics2D g) {
		super(width, height, img, g);
	}

	public void draw(){

		for(int a = 0; a < rand.nextInt(25); a++){
			setRandomColor(true);
			if(rand.nextInt(10) > 1)
				drawRandomShape();
			else 
				drawPattern();

		}
	}
	
	private void drawPattern(){
		int csize = rand.nextInt(100) + 25;

		int xamount = rand.nextInt(10);
		int yamount = rand.nextInt(5);

		int xcent = rand.nextInt(WIDTH);
		int ycent = rand.nextInt(HEIGHT);

		int fade = rand.nextInt(25) + 5;

		setRandomColor(true);
		Color c = g.getColor();
		int originalAlpha = c.getAlpha();

		for(int x = -xamount; x < xamount; x++){
			for(int y = -yamount; y < yamount; y++){
				int alpha = originalAlpha - (Math.abs(x) * fade) - (Math.abs(y) * fade);
				g.setColor(new Color(c.getRed(), c.getBlue(), c.getGreen(), (alpha < 0) ? 0 : alpha));

				int xpos = xcent + ((csize + 3) * x);
				int ypos = ycent + ((csize + 3) * y);


				g.fillOval(xpos, ypos, csize, csize);
			}
		}

	}

	private void drawRandomShape(){
		int size = 2;
		while(rand.nextBoolean())
			size++;
		int xcor [] = new int[size];
		int ycor [] = new int[size];
		for(int a = 0; a < size; a++){
			xcor[a] = rand.nextInt(WIDTH);
			ycor[a] = rand.nextInt(HEIGHT);
		}

		//if(rand.nextInt(10) > 0)
		g.fillPolygon(xcor, ycor, size);
		//else
		//	g.drawPolygon(xcor, ycor, size);
	}
}