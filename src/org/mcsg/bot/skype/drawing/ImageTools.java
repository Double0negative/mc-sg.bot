package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.mcsg.bot.skype.util.Progress;

public class ImageTools {

	public static double[][] createNoise(BufferedImage img, int max, int width, int height, int boundxs, int boundys, int boundx, int boundy, int scalex, int scaley, double persist,  Progress<?> prog){
		SimplexNoise noise = new SimplexNoise(max, persist, new Random().nextInt());

		double[][] result=new double[width][height];

		double xStart=0;
		double yStart=0;

		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				int x=(int)(xStart+i*((scalex-xStart)/width));
				int y=(int)(yStart+j*((scaley-yStart)/height));
				result[i][j]=0.5*(1+noise.getNoise(x,y));
			}
		}
		return result;
	}

	public static void createBWNoise(BufferedImage img, int max, int width, int height, int boundxs, int boundys, int boundx, int boundy, int scalex, int scaley, double persist,  Progress<?> prog){
		double[][] result=  createNoise(img, max , width, height, boundxs, boundys, boundx, boundy, scalex, scaley, persist, prog);
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				prog.incProgress(1);
				if (result[x][y]>1){
					result[x][y]=1;
				}
				if (result[x][y]<0){
					result[x][y]=0;
				}
				Color col=new Color((float)result[x][y],(float)result[x][y],(float)result[x][y]); 
				img.setRGB(x, y, col.getRGB());
			}
		}
	}

	public static void createColorNoise(BufferedImage img, int max, int width, int height, int boundxs, int boundys, int boundx, int boundy, int scalex, int scaley, double persist,  Progress<?> prog){
		double[][] r=  createNoise(img, max, width, height, boundxs, boundys, boundx, boundy, scalex, scaley, persist, prog);
		double[][] g=  createNoise(img, max, width, height, boundxs, boundys, boundx, boundy, scalex, scaley, persist, prog);
		double[][] b=  createNoise(img, max, width, height, boundxs, boundys, boundx, boundy, scalex, scaley, persist, prog);

		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				prog.incProgress(1);
				r[x][y] = limit(r[x][y], 1, 0);
				g[x][y] = limit(g[x][y], 1, 0);
				b[x][y] = limit(b[x][y], 1, 0);

				Color col=new Color((float)r[x][y],(float)g[x][y],(float)b[x][y]); 
				img.setRGB(x, y, col.getRGB());
			}
		}
	}


	public static double limit (double val, double max, double min){
		return (val > max) ? max : val < min ? min : val;
	}
	
	public static int limit (int val, int max, int min){
		return (val > max) ? max : val < min ? min : val;
	}

}
