package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.mcsg.bot.skype.util.MapWrapper;
import org.mcsg.bot.skype.util.Progress;


public class DrawPerlin extends Drawer{

	public DrawPerlin(int width, int height, BufferedImage img, Graphics2D g) {
		super(width, height, img, g);
	}

	@Override
	public void draw(Progress<Integer> prog, MapWrapper args) {
		prog.setMax(HEIGHT * WIDTH);

		ImageTools.createBWNoise(img, 200, WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT, WIDTH /2 , HEIGHT/2, 0.3, prog);
		
	}





}
