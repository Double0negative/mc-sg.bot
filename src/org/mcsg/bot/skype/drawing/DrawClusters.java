package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.mcsg.bot.skype.util.Point;
import org.mcsg.bot.skype.util.Progress;

public class DrawClusters extends Drawer {

	public DrawClusters(int width, int height, BufferedImage img, Graphics2D g) {
		super(width, height, img, g);
		
		for(int a = 0; a < rand.nextInt(5)  + 1; a++){
			points.add(new Point(rand.nextInt(WIDTH- 500 ) + 250, rand.nextInt(HEIGHT - 500) + 250));
		}
		
		baseColor = getRandomColor(true);
	}

	
	private ArrayList<Point> points = new ArrayList<>();
	private Color baseColor;
	
<<<<<<< HEAD
	public void draw(Progress<Integer> prog, String ... args) {
=======
	public void draw(String ... args) {
>>>>>>> 7c41bd3b9dd888e04b098fee745757b8f6819725
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		int stop = rand.nextInt(10000);
		prog.setMax(stop);
		for(int a = 0; a < stop; a++){
			int x = rand.nextInt(WIDTH);
			int y = rand.nextInt(HEIGHT);
			
			int dis = Integer.MAX_VALUE;
			for(Point point : points){
				dis = Math.min(dis, Math.abs(point.distance(x, y)));
			}
			
			int size = Math.max(0, 200 - dis) / 4;
			size += rand.nextInt(25);
			
			
			/*g.setColor(new Color(baseColor.getRed(), baseColor.getBlue(), baseColor.getGreen(), rand.nextInt(50)));
			g.fillOval(x - size /2 , y - size /2, size, size);*/
			
			g.setColor(new Color(baseColor.getRed(), baseColor.getBlue(), baseColor.getGreen(), size));
			size = Math.max(1, 200 - dis) / 3;
<<<<<<< HEAD
			drawCircle(x, y, size);
			prog.setProgress(a);
=======
			
>>>>>>> 7c41bd3b9dd888e04b098fee745757b8f6819725
		}
	}
	
	private void drawCircle(int x, int y, int size){

		if(size == 1)
			while(rand.nextBoolean())
				size=size * 3;
		size = rand.nextInt(size + 10);
		g.fillOval(x - size /2 , y - size /2, size, size);
	}

}
