package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.mcsg.bot.skype.util.MapWrapper;
import org.mcsg.bot.skype.util.Progress;

public class DrawSunset extends Drawer{

	public DrawSunset(int width, int height, BufferedImage img, Graphics2D g) {
		super(width, height, img, g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Progress<Integer> prog, MapWrapper args) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		int horiz = rand.nextInt(HEIGHT / 2) + HEIGHT / 2;
		int amt = rand.nextInt(600);

		g.setColor(new Color(255,255,255,10));
		for(int b = 0; b < amt; b++){

			int y = rand.nextInt(horiz);
			int x = rand.nextInt(WIDTH);
			drawPoints(x, y);

		}
	}



	private void drawPoints(int x, int y){
		int amt = rand.nextInt(50);
		int h = rand.nextInt(250);
		int w = rand.nextInt(400);

		for(int b = 0; b < amt; b++){
			int yy = y + rand.nextInt(h + y) - h /2 ;
			int xx = x + rand.nextInt(w + x) - w /2;
			drawCircle(xx, yy, 30);
		
		}


	}

}
