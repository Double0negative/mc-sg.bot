package org.mcsg.bot.skype.drawing;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class DrawCircles extends Drawer {

	public DrawCircles(int width, int height, Graphics2D g) {
		super(width, height, g);
	}

	@Override
	public void draw() {
		setRandomColor(false);
		g.fillRect(0, 0, WIDTH, HEIGHT);
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
