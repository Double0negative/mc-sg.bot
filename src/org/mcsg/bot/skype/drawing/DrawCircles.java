package org.mcsg.bot.skype.drawing;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.mcsg.bot.skype.util.Progress;

public class DrawCircles extends Drawer {

	public DrawCircles(int width, int height, BufferedImage img, Graphics2D g) {
		super(width, height, img, g);
	}

	@Override
<<<<<<< HEAD
	public void draw(Progress<Integer> prog, String ... args) {
=======
	public void draw(String ... args) {
>>>>>>> 7c41bd3b9dd888e04b098fee745757b8f6819725

		g.setStroke(new BasicStroke(4));

		for(int a = 0; a < rand.nextInt(20) + 10; a++){ 
			setRandomColor(true);
			int size = rand.nextInt(450) + 100;
			int x = rand.nextInt(WIDTH) ;
			int y = rand.nextInt(HEIGHT);
			
			g.fillOval(x, y, size, size);
			g.setColor(g.getColor().brighter());
			g.drawOval(x, y, size, size);
		}
		
	}

}
