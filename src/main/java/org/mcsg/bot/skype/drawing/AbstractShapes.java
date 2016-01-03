package org.mcsg.bot.skype.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.mcsg.bot.skype.util.MapWrapper;
import org.mcsg.bot.skype.util.Point;
import org.mcsg.bot.skype.util.Progress;

public class AbstractShapes extends Drawer{

  public AbstractShapes(int width, int height, BufferedImage img, Graphics2D g) {
    super(width, height, img, g);
  }

  @Override
  public void draw(Progress<Integer> prog, MapWrapper args) {
    int stop = args.getInt("amount", rand.nextInt(5000));
    prog.setMax(stop);
    prog.setMessage("Abstract Shapes");
    Point point = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
    setRandomColor(true);
    g.setColor(new Color(255,170,0,50));
    for(int a = 0; a < stop; a++){
      prog.incProgress(1);
      int r = rand.nextInt(500);
      if( r > 450){
        if(rand.nextBoolean())
          g.setColor(brigher(g.getColor(), 3));
        else 
          g.setColor(darker(g.getColor(), 3));
      } else if(r < 100){
        g.setColor(incColor(g.getColor(), 10));
      }
      point = drawRandomShape(point);
    }
  }

  
  
  private Point drawRandomShape(Point point){
    int size = 3;
    while(rand.nextInt(10) > 2)
      size++;
    int xcor [] = new int[size];
    int ycor [] = new int[size];
    
    int lx = point.getX();
    int ly = point.getY();
    System.out.println("Starting at "+lx+" "+ly);
    for(int a = 0; a < size; a++){
      lx = lx + (rand.nextInt(300) - 150);
      xcor[a] = lx = ImageTools.limit(lx, WIDTH, 0);
      ly = ly + (rand.nextInt(300) - 150);
      ycor[a] = ly = ImageTools.limit(ly, HEIGHT, 0);
    }

    g.fillPolygon(xcor, ycor, size);
    System.out.println("Ending at "+lx+" "+ly);

    return new Point(lx, ly);
    
  }
}
