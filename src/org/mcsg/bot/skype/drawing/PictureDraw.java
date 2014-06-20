package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.mcsg.bot.skype.util.HttpHeader;
import org.mcsg.bot.skype.util.Settings;
import org.mcsg.bot.skype.util.WebClient;

public class PictureDraw {

	private int WIDTH = 1200;
	private int HEIGHT = 720;

	private Random rand = new Random();
	private BufferedImage img;
	private Graphics2D g;



	public static void main(String args[]){
			PictureDraw draw = new PictureDraw();
			draw.draw();
			draw.save(new File("/home/drew/", "TestImage.png"));
		
			/*try {
				List<HttpHeader> headers = new ArrayList<HttpHeader>();
				headers.add(new HttpHeader("Authorization", "Client-ID "+Settings.Root.Imgur.CLIENT_ID));
				String json = WebClient.postArgs("https://api.imgur.com/3/image", headers, "image", Base64.getEncoder().encodeToString(draw.getBytes()));
				System.out.println(json);
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
	}

	public PictureDraw(){
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		g = (Graphics2D) img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void draw(){
		if(rand.nextBoolean()){
			new DrawShapes(WIDTH, HEIGHT, g).draw();
		} else {
			new DrawLines(WIDTH, HEIGHT, g).draw();
		}
	}

	public byte[] getBytes() {
	    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	    try {
			ImageIO.write(img, "png", byteArray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return byteArray.toByteArray();
	}
	
	public void save(File file){
		try {
			ImageIO.write(img, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
