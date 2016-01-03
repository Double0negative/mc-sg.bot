package org.mcsg.bot.skype;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;

import org.mcsg.bot.skype.PluginRegistry.PluginData;
import org.mcsg.bot.skype.PluginRegistry.PluginData.ClassFile;
import org.mcsg.bot.skype.util.FileUtils;
import org.mcsg.bot.skype.util.JarCreator;

import com.google.gson.Gson;
import com.samczsun.skype4j.chat.Chat;

public class PluginManager {

  private static HashMap<String, McsgBotPlugin> plugins = 
      new HashMap<>();

  private static final Gson gson = new Gson();
  private static PluginRegistry pluginRegistry;
  private static File pluginFile = new File("plugins.json");
  private static File pluginDir = new File("plugins/");

  public static void disablePlugin(String name) throws Exception{
    McsgBotPlugin plugin = getPlugin(name);
    if(plugin != null){
      plugin.onDisable();
      plugins.remove(name);
      Bot.unregisterCommands(plugin);
      EventHandler.unregisterListeners(plugin);

    } 

  }

  protected static void unregisterPlugin(String name) throws Exception {
    PluginData data = null;
    for(PluginData pdata : pluginRegistry.data)
      if(pdata.name.equals(name))
        data = pdata;

    plugins.remove(name);
    pluginRegistry.data.remove(data);
    savePluginData();
  }

  public static McsgBotPlugin getPlugin(String name){
    return plugins.get(name);
  }

  protected static PluginData createPlugin(String name,  String mainClass,  ClassFile ... files){
    PluginData data = new PluginData();
    data.mainclass = mainClass;
    data.name = name;
    data.jarLocation = pluginDir.getAbsolutePath()+"/"+name+".jar";
    data.files = files;
    return data;
  }

  protected static void buildPlugin(PluginData data) throws Exception{
    JarCreator creator = new JarCreator(new File(pluginDir, data.name+".jar"));
    File dir = new File(pluginDir, data.name+"/");
    System.out.println("Checking "+dir + "  "+ Arrays.toString(dir.list()));
    for(File file : dir.listFiles())
      if(file.getName().endsWith(".class")) {
        creator.add(pluginDir.toString()+"/"+data.name+"/", file);
        System.out.println(" adding file " + file);
      }
    creator.close();
  }

  protected static void savePluginData() throws IOException{
    String json = gson.toJson(pluginRegistry);
    FileUtils.writeFile(pluginFile, json);
  }

  protected static void registerPlugin(PluginData data, Chat chat) throws Exception{
    pluginRegistry.data.add(data);
    savePluginData();
    loadPlugin(data, chat);
  }


  protected static void loadPlugins(Chat chat) throws Exception{
    String regs = FileUtils.readFile(pluginFile);
    pluginRegistry = gson.fromJson(regs, PluginRegistry.class);

    if(pluginRegistry != null)
      for(PluginData data : pluginRegistry.data){
        try {
          loadPlugin(data, chat);}
        catch (Exception e){}
      }
    else 
      pluginRegistry = new PluginRegistry();
  }



  public static void loadPlugin(PluginData data, Chat chat) throws Exception {
    if(plugins.containsKey(data.name)) {
      if(chat != null)
        ChatManager.chat(chat, "Failed to load plugin \""+data.name+"\": Plugin is already loaded!");
      return;
    }
    System.out.println("Loading Plugin ");

    URLClassLoader loader = URLClassLoader.newInstance(new URL[]{new File(data.jarLocation).toURI().toURL()});
    for(ClassFile main : data.files){
      Class.forName(main.name, true, loader);
    }
    Class<?> clazz = Class.forName(data.mainclass, true, loader);
    McsgBotPlugin plugin  = clazz.asSubclass(McsgBotPlugin.class).newInstance();

    plugins.put(data.name, plugin);

    try { 
      plugin.onEnable(chat);
    } catch (Exception e){
      if(chat != null)
        ChatManager.printThrowable(chat, e);
    }
    if(chat != null)
      ChatManager.chat(chat,"Loaded plugin: "+data.name);
    loader.close();
  }
}


/* public static void loadPlugin(PluginData data) throws Exception{
    System.out.println("Loading plugin "+data.name +" of type "+data.type);
    if(data.type.equals("class")){
      URL []urls = new URL[data.files.length]; 
      int a = 0;
      for(ClassFile cfile : data.files){
        File file = new File(cfile.location);
        urls[a] = file.getParentFile().toURI().toURL(); 
        a++;
      }
      Chat chat = Bot.getDefaultChat();
      if(chat != null)
        ChatManager.chat(chat,"Loading plugin: "+data.name);

      URLClassLoader loader = new URLClassLoader(urls);
      for(ClassFile cfile :data.files){
        if(!cfile.name.equals(data.mainclass)){
          loader.loadClass(cfile.name);
        }
      }
      McsgBotPlugin plugin = (McsgBotPlugin) loader.loadClass(data.mainclass).newInstance();

      plugins.put(plugin.getName(), plugin);



      try { 
        plugin.onEnable();
      } catch (Exception e){
        if(chat != null)
          ChatManager.printThrowable(chat, e);
      }
      if(chat != null)
        ChatManager.chat(chat,"Loaded plugin: "+plugin.getName());
      loader.close();
    }

  }*/

