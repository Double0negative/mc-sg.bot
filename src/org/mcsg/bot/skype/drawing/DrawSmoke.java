package org.mcsg.bot.skype.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class DrawSmoke extends Drawer{
	int r=rand.nextInt(255);
	int gc=rand.nextInt(255);
	int b=rand.nextInt(255);
	int a=rand.nextInt(255);

	int x = rand.nextInt(WIDTH);
	int y = rand.nextInt(HEIGHT);

	int size = rand.nextInt(45);


	public DrawSmoke(int width, int height, Graphics2D g) {
		super(width, height, g);
	}


	@Override
	public void draw() {
		
		g.setColor(Color.WHITE);
		setRandomColor(false);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setStroke(new BasicStroke(rand.nextInt(7)));
		
		int stop = rand.nextInt(90000);
		
		int minx = rand.nextInt(WIDTH - 750);
		int maxx = rand.nextInt(600) + minx + 150;
		
		int miny = rand.nextInt(HEIGHT - 750);
		int maxy = rand.nextInt(600) + miny + 150;
		
		for(int aa = 0; aa < stop; aa++){
			r += rand.nextInt(7) - 3;
			b += rand.nextInt(7) - 3;
			gc += rand.nextInt(7) - 3;
			a += rand.nextInt(7) - 3;

			r = r < 0 ? 0 : r > 255 ? 255 : r; 
			b = b < 0 ? 0 : b > 255 ? 255 : b; 
			gc = gc < 0 ? 0 : gc > 255 ? 255 : gc; 
			a = a < 0 ? 0 : a > 25 ? 25 : a; 

			g.setColor(new Color(r, b, gc, a));

			x += rand.nextInt(size) - size / 2;
			y += rand.nextInt(size) - size / 2;
			size += rand.nextInt(3) - 1;

			size = size < 10 ? 10 : size  > 50 ? 50 : size;
			/*x = x > maxx ? maxx : x < minx ? minx : x;
			y = y > maxy ? maxy : y < miny ? miny : y;*/

			x = x > WIDTH ? WIDTH : x < 0 ? 0 : x;
			y = y > HEIGHT ? HEIGHT : y < 0 ? 0 : y;
			
			g.fillOval(x, y, size, size);
			g.setColor(g.getColor().brighter());
			//g.drawOval(x, y, size, size);
		}
	}
}
