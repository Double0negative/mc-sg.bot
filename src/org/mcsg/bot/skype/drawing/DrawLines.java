package org.mcsg.bot.skype.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.mcsg.bot.skype.util.Fader;
import org.mcsg.bot.skype.util.MapWrapper;
import org.mcsg.bot.skype.util.Progress;

class DrawLines extends Drawer{

	int r=rand.nextInt(255);
	int gc=rand.nextInt(255);
	int b=rand.nextInt(255);
	int a=rand.nextInt(255);

	int x1 = rand.nextInt(WIDTH);
	int x2 = rand.nextInt(WIDTH);

	int y1 = rand.nextInt(HEIGHT);
	int y2 = rand.nextInt(HEIGHT);


	public DrawLines(int width, int height, BufferedImage img, Graphics2D g) {
		super(width, height, img, g);
	}


	public void draw(Progress<Integer> prog, MapWrapper args) {
		g.setStroke(new BasicStroke(args.getInt("stroke-size", 2)));

		boolean limit = args.getBoolean("limit", rand.nextBoolean());
		int stop = args.getInt("amount", rand.nextInt(40000)) + args.getInt("amount-base", 10000);
		prog.setMax(stop);
		prog.setMessage("lines");
		for(int aa = 0; aa < stop; aa++){


			g.setColor(incColor(g.getColor(), 7));

			x1 += rand.nextInt(13) - 6;
			x2 += rand.nextInt(13) - 6;
			y1 += rand.nextInt(13) - 6;
			y2 += rand.nextInt(13) - 6;

			if(limit){
				x1 = ImageTools.limit(x1, WIDTH, 0);
				x2 = ImageTools.limit(x2, WIDTH, 0);
				y1 = ImageTools.limit(y1, HEIGHT, 0);
				y2 = ImageTools.limit(y2, HEIGHT, 0);
			}
			g.drawLine(x1, y1, x2, y2);
			prog.setProgress(aa);
		}

	}

	@Override
	public void drawCircle(int x, int y, int size) {
		// TODO Auto-generated method stub
		super.drawCircle(x, y, size);
	}
}