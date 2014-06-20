package org.mcsg.bot.skype.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import org.mcsg.bot.skype.util.Fader;

class DrawLines extends Drawer{

	private	Fader fade = new Fader(0,0,0,0,0,0,1);
	int r=rand.nextInt(255);
	int gc=rand.nextInt(255);
	int b=rand.nextInt(255);
	int a=rand.nextInt(255);

	int x1 = rand.nextInt(WIDTH);
	int x2 = rand.nextInt(WIDTH);

	int y1 = rand.nextInt(HEIGHT);
	int y2 = rand.nextInt(HEIGHT);


	public DrawLines(int width, int height, Graphics2D g) {
		super(width, height, g);
	}


	@Override
	public void draw() {
		setRandomColor(false);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setStroke(new BasicStroke(2));

		boolean limit = rand.nextBoolean();

		for(int aa = 0; aa < 30000; aa++){

			r += rand.nextInt(7) - 3;
			b += rand.nextInt(7) - 3;
			gc += rand.nextInt(7) - 3;
			a += rand.nextInt(7) - 3;

			r = r < 0 ? 0 : r > 255 ? 255 : r; 
			b = b < 0 ? 0 : b > 255 ? 255 : b; 
			gc = gc < 0 ? 0 : gc > 255 ? 255 : gc; 
			a = a < 0 ? 0 : a > 255 ? 255 : a; 


			Color c = new Color(r, gc,b, a);
			g.setColor(c);

			x1 += rand.nextInt(13) - 6;
			x2 += rand.nextInt(13) - 6;
			y1 += rand.nextInt(13) - 6;
			y2 += rand.nextInt(13) - 6;

			if(limit){
				x1 = x1 > WIDTH ? WIDTH : x1 < 0 ? 0 : x1;
				x2 = x2 > WIDTH ? WIDTH : x2 < 0 ? 0 : x2;
				y1 = y1 > HEIGHT ? HEIGHT : y1 < 0 ? 0 : y1;
				y2 = y2 > HEIGHT ? HEIGHT : y2 < 0 ? 0 : y2;
			}
			g.drawLine(x1, y1, x2, y2);
		}

	}

}