package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.mcsg.bot.skype.util.Point;
import org.mcsg.bot.skype.util.Progress;

public class DrawDots extends Drawer{

	public DrawDots(int width, int height, BufferedImage img, Graphics2D g) {
		super(width, height, img, g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Progress<Integer> prog, String... args) {
		int x = rand.nextInt(WIDTH);
		int y = rand.nextInt(HEIGHT);
		int amt = rand.nextInt(50000);
		int size = 12;
		Point lpoint = new Point(x, y);

		int r=rand.nextInt(255);
		int gc=rand.nextInt(255);
		int b=rand.nextInt(255);
		int a=rand.nextInt(255);

		prog.setMax(amt);

		for(int d = 0; d < amt; d++){
			prog.setProgress(d);
			g.setColor(new Color(r, b, gc, a));

			Point p = new Point(lpoint);
			while(p.distance(lpoint) < size + 1){
				p.incX(rand.nextInt(3) - 1);
				p.incY(rand.nextInt(3) - 1);
			}

			/*p.incX(rand.nextInt(11) - 6);
			p.incY(rand.nextInt(11) - 6);*/

			r += rand.nextInt(7) - 3;
			b += rand.nextInt(7) - 3;
			gc += rand.nextInt(7) - 3;
			a += rand.nextInt(7) - 3;

			r = r < 0 ? 0 : r > 255 ? 255 : r; 
			b = b < 0 ? 0 : b > 255 ? 255 : b; 
			gc = gc < 0 ? 0 : gc > 255 ? 255 : gc; 
			a = a < 0 ? 0 : a > 255 ? 255 : a; 

			p.setX(p.getX() > WIDTH ? WIDTH : p.getX()< 0 ? 0 : p.getX());
			p.setY(p.getY() > HEIGHT ? HEIGHT : p.getY() < 0 ? 0 : p.getY());

			drawCircle(p.getX(), p.getY(), size);
			g.setColor(g.getColor().brighter());
			drawCircle(p.getX(), p.getY(), size - 2);

			lpoint = p;

		}



	}








}
