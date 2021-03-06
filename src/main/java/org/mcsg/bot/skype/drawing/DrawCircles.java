package org.mcsg.bot.skype.drawing;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.mcsg.bot.skype.util.MapWrapper;
import org.mcsg.bot.skype.util.Progress;

public class DrawCircles extends Drawer {

	public DrawCircles(int width, int height, BufferedImage img, Graphics2D g) {
		super(width, height, img, g);
	}

	@Override
	public void draw(Progress<Integer> prog, MapWrapper args) {


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
