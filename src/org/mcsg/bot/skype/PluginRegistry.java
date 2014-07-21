package org.mcsg.bot.skype;

import java.io.File;
import java.util.ArrayList;

public class PluginRegistry {

  public static transient final File DIR = new File("plugins/");
  
  public ArrayList<PluginData> data = new ArrayList<>();
  
  
  public static class PluginData {
    String name;
    String mainclass;
    String type;
    
    ClassFile[] files;
    
    public static class ClassFile{
      String name;
      String location;
    }
    
  }
  
  
}
