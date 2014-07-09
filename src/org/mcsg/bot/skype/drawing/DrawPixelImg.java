package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.mcsg.bot.skype.drawing.PixelImage.PixelData;
import org.mcsg.bot.skype.util.Progress;

public class DrawPixelImg extends Drawer {

	public DrawPixelImg(int width, int height, BufferedImage img, Graphics2D g) {
		super(width, height, img, g);
	}

	@Override
<<<<<<< HEAD
	public void draw(Progress<Integer> prog, String ... args) {
=======
	public void draw(String ... args) {
>>>>>>> 7c41bd3b9dd888e04b098fee745757b8f6819725
		PixelImage pixelimg = new PixelImage(img, 7);
		
		ArrayList<PixelData> points = pixelimg.getPoints();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	/*	for(int a = 0; a < 5; a++){
			g.setColor(new Color(0,0,0,100));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			physics(points);
			for(PixelData point : points){
				g.setColor(new Color(point.r,point.g,point.b));
				g.fillOval(point.x, point.y, 5, 5);
			}
		}*/
		
		prog.setMax(points.size());
		for(PixelData point : points){
			g.setColor(new Color(point.r,point.g,point.b));
			g.fillOval(point.x, point.y, 5, 5);
			prog.incProgress(1);
		}
		
		
	}

	
	public void physics(ArrayList<PixelData> points){
		for(PixelData d : points.toArray(new PixelData[points.size()])){
			d.x += d.vx;
			d.y+= d.vy;
			//d.vy += 0.05;

			
			if(d.y < 0 ||d.y > HEIGHT){
				
					points.remove(d);
				
			}
			if(d.x < 0 || d.x > WIDTH){
				points.remove(d);
			}
		}
	}
	
}
