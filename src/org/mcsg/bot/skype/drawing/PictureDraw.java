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

	private int WIDTH = 1920;
	private int HEIGHT = 1080;

	private Random rand = new Random();
	private BufferedImage img;
	private Graphics2D g;


	public static void main(String args[]){
		PictureDraw draw = new PictureDraw();
		draw.draw(-1);
		draw.save(new File("/home/drew/", "TestImage.png"));

		/*try {
				//List<HttpHeader> headers = new ArrayList<HttpHeader>();
				//headers.add(new HttpHeader("Authorization", "Client-ID "+Settings.Root.Imgur.CLIENT_ID));
				/*System.out.println(Base64.getEncoder().encodeToString(draw.getBytes()));
				String json = WebClient.post("http://uploads.im/api.php",  new String("file="+draw.getBytes()), null);
				System.out.println(json);

				String json = WebClient.postArgs("http://mc-sg.org/bot/upload.php",  null,"img", Base64.getEncoder().encodeToString(draw.getBytes()), "key", "2gsd@#YdfG#$" );

				System.out.println(json);
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

	}

	public PictureDraw(int width, int height){
		this.WIDTH = width;
		this.HEIGHT = height;
		setup();
	}

	public PictureDraw(){
		setup();
	}

	private void setup(){
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		g = (Graphics2D) img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	public void draw(int sel){
		if(sel == -1)
			sel = rand.nextInt(5);
		switch (sel) {
		case 0:
			new DrawShapes(WIDTH, HEIGHT, g).draw();
			break;
		case 1:
			new DrawLines(WIDTH, HEIGHT, g).draw();
			break;
		case 2:
			new DrawCircles(WIDTH, HEIGHT, g).draw();
			break;
		case 3:
			new DrawDotLines(WIDTH, HEIGHT, g).draw();
			break;
		case 4:
			new DrawSmoke(WIDTH, HEIGHT, g).draw();
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
