package org.mcsg.bot.skype.drawing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class PixelImage {

	private BufferedImage img;
	private ArrayList<PixelData> points;
	private int size;
	
	private int ex, ey;
	
	public PixelImage(BufferedImage img, int size){
		this.img = img;
		this.size = size;
		
		ex = new Random().nextInt(img.getWidth());
		ey = new Random().nextInt(img.getHeight());
		
		load();
	}
	
	private void load(){
		points = new ArrayList<>();
		
		for(int x = 0; x < img.getWidth(); x+=size){
			for(int y = 0; y < img.getHeight(); y+=size){
				int rgb = img.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);

				//if(r >  150 || g > 150  || b > 150){
					PixelData d =  new PixelData();
					d.x = x;
					d.y = y;
					d.r = r ;
					d.g = g;
					d.b = b ;
					d.a = 255;
					
				/*	int dx = x - ex;
					int dy = y - ey;
					if(dx * dx + dy *dy < 100 * 100){
						double angle = Math.atan2(dx, dy);
						double dis =  Math.sqrt(dx * dx + dy *dy);
						d.vx =  Math.sin(angle)* (100 - dis);
						d.vy =  Math.cos(angle)* (100 - dis);
					} trying to do math at 4am no good*/
				/*	
					if(new Random().nextInt(10) < 1){
						d.vx = new Random().nextInt(6);
						d.vy = new Random().nextInt(6);
					}
				*/
					points.add(d);
				//}
			}
		}

	}
	
	public ArrayList<PixelData> getPoints(){
		return points;
	}
	
	public class PixelData{
		int x,y;
		int r,g,b,a;
		double vy;
		double vx;
	}
	
}
