package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.xml.bind.DatatypeConverter;

import org.mcsg.bot.skype.Settings;
import org.mcsg.bot.skype.util.MapWrapper;
import org.mcsg.bot.skype.util.Progress;
import org.mcsg.bot.skype.util.ThreadUtil;
import org.mcsg.bot.skype.web.HttpHeader;
import org.mcsg.bot.skype.web.WebClient;

public class PictureDraw {

	private int WIDTH = 1920;
	private int HEIGHT = 1080;

	private Random rand = new Random();
	private BufferedImage img;
	private Graphics2D g;

	static JProgressBar bar = new JProgressBar(0, 100);

	public static void main(String args[]) throws MalformedURLException, IOException{
		System.out.println("Drawing...");
		//BufferedImage img = ImageIO.read(new URL("http://assets.worldwildlife.org/photos/2842/images/hero_small/shutterstock_12730534.jpg?1352150501"));

		//draw.save(new File("/home/drew/", "TestImage.png"));

		JFrame frame = new JFrame("img");

		frame.setSize(1920, 1080);
		frame.setLayout(null);
		frame.setVisible(true);
		final JLabel l = new JLabel();
		l.setBounds(0, 0, 1920, 1080);
		frame.add(l);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton b = new JButton("next");
		frame.add(b);
		b.setBounds(0, 0, 70, 30);
		bar.setBounds(300, 300, 800, 30);

		b.addActionListener((action) -> {
			PictureDraw draw = new PictureDraw(null);
			Progress<Integer> prog = draw.draw(8);
			bar.setMaximum((int) prog.getMax());
			prog.waitForFinish();
			l.setIcon(new ImageIcon(draw.getImage()));
			//frame.remove(bar);
		});
		/*try {
				//List<HttpHeader> headers = new ArrayList<HttpHeader>();
				//headers.add(new HttpHeader("Authorization", "Client-ID "+Settings.Root.Imgur.CLIENT_ID));
				/*System.out.println(Base64.getEncoder().encodeToString(draw.getBytes()));
				String json = WebClient.post("http://uploads.im/api.php",  new String("file="+draw.getBytes()), null);
				System.out.println(json);

				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

	}

	public PictureDraw(int width, int height, BufferedImage imgb){
		this.WIDTH = width;
		this.HEIGHT = height;
		setup(imgb);
	}

	public PictureDraw(BufferedImage imgb){
		if(imgb != null){
			WIDTH = imgb.getWidth();
			HEIGHT = imgb.getHeight();
		}
		setup(imgb);
	}

	private void setup(BufferedImage imgb){
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		g = (Graphics2D) img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Drawer.getRandomColor(false));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		if(imgb != null){
			g.drawImage(imgb, 0, 0, null);
		}
	}

	public Progress<Integer> draw(final int sell, String ... args){
		final Progress<Integer> prog = new Progress<>();
		final MapWrapper wrap = new MapWrapper();
		for(String arg : args){
			String [] split = arg.split(":");
			wrap.put(split[0], split[1]);
		}
		if(wrap.containsKey("bg") || wrap.containsKey("background")){
		  String bg = wrap.get("bg");
		  Color c = null;
		  if(bg.startsWith("#")){
		    c = Color.decode(bg);
		  } else {
		    c = Color.getColor(bg);
		  }
		  g.setColor(c);
      g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		ThreadUtil.run("Image Generator", new Thread(){
			public void run(){
				int sel = sell;
				if(sel == -1)
					sel = rand.nextInt(9);
				switch (sel) {
				case 0:
					new DrawShapes(WIDTH, HEIGHT, img, g).draw(prog, wrap);
					break;
				case 1:
					new DrawLines(WIDTH, HEIGHT, img, g).draw(prog, wrap);
					break;
				case 2:
					new DrawCircles(WIDTH, HEIGHT, img, g).draw(prog, wrap);
					break;
				case 3:
					new DrawDotLines(WIDTH, HEIGHT, img, g).draw(prog, wrap);
					break;
				case 4:
					new DrawSmoke(WIDTH, HEIGHT, img, g).draw(prog, wrap);
					break;
				case 5:
					new DrawClusters(WIDTH, HEIGHT, img, g).draw(prog, wrap);
					break;
				case 6:
					new DrawDots(WIDTH, HEIGHT, img, g).draw(prog, wrap);
					break;
				case 7:
					new DrawPerlin(WIDTH, HEIGHT, img, g).draw(prog, wrap);
					break;
				case 8:
				  new AbstractShapes(WIDTH, WIDTH, img, g).draw(prog, wrap);
				  break;
				case 25: 
					new DrawPixelImg(WIDTH, HEIGHT, img, g).draw(prog, wrap);
				}
				prog.finish();
			}
		});
		return prog;

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

	public BufferedImage getImage(){
		return img;
	}

}
