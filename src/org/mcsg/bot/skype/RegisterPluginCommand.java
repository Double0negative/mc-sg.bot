package org.mcsg.bot.skype;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.mcsg.bot.skype.PluginRegistry.PluginData;
import org.mcsg.bot.skype.PluginRegistry.PluginData.ClassFile;
import org.mcsg.bot.skype.commands.SubCommand;
import org.mcsg.bot.skype.util.Arguments;
import org.mcsg.bot.skype.util.FileUtils;
import org.mcsg.bot.skype.util.ShellCommand;
import org.mcsg.bot.skype.web.GistAPI;
import org.mcsg.bot.skype.web.GistAPI.GistResponse;
import org.mcsg.bot.skype.web.GistAPI.GistResponse.GistFile;

import com.google.gson.Gson;
import com.skype.Chat;
import com.skype.User;

public class RegisterPluginCommand implements SubCommand{

  @Override
  public void execute(Chat chat, User sender, String[] args) throws Exception {
    if(Permissions.hasPermission(sender, chat, "plugin")){
      Arguments arge = new Arguments(args, "register", "update", "unregister");
      args = arge.getArgs();
      HashMap<String, String> swi = arge.getSwitches();

      if(swi.containsKey("unregister")){
        String name = args[0];
        PluginManager.disablePlugin(name);
        PluginManager.unregisterPlugin(name);
        chat.send("Unregistered Plugin: "+name);
        return;
      }
      
      boolean register = swi.containsKey("register");
      boolean update = swi.containsKey("update");
      String url = args[0];

      if(url.contains("gist.github.com")){
        loadFromGist(chat, url, register, update);
      }

    }

  }

  public void loadFromGist(Chat chat, String url, boolean register, boolean update) throws Exception{
    String id = url.substring(url.lastIndexOf("/") + 1);
    GistResponse gist = GistAPI.get(id);
    PluginManifest mani = new Gson().fromJson(gist.files.get("manifest.json").content, PluginManifest.class);

    File dir = new File(PluginRegistry.DIR, mani.name);
    if(update) {
      ShellCommand.exec(chat, "cd "+PluginRegistry.DIR+"; rm -rf "+mani.name, 0, false);
      PluginManager.disablePlugin(mani.name);
      PluginManager.unregisterPlugin(mani.name);
    }
    dir.mkdirs();
    
    List<ClassFile> files = new ArrayList<>();
    for(GistFile contents : gist.files.values()){
      if(contents.language.equalsIgnoreCase("Java")){
        File file = new File(dir, contents.filename);
        FileUtils.writeFile(file, contents.content);
        ShellCommand.exec(chat, "cd "+dir.getAbsolutePath()+"; javac -classpath \"../../java_libs/*:\" "+contents.filename, 0, false, true);

        int ploc = contents.content.indexOf("package");
        String packagename = (ploc > -1) ? contents.content.substring(ploc, contents.content.indexOf(";",ploc)).replace("package ", "") +"." : "";

        String s = "public class ";
        int cloc = contents.content.indexOf(s) + s.length();
        String classname = contents.content.substring(cloc, contents.content.indexOf(" ", cloc));
        String name = packagename + classname;
        ClassFile cfile = new ClassFile();

        cfile.location = file.getAbsolutePath();
        cfile.name = name;
        files.add(cfile);
      }
    }
    PluginData data = PluginManager.createPlugin(mani.name, mani.main, files.toArray(new ClassFile[0]));
    PluginManager.buildPlugin(data);
    PluginManager.registerPlugin(data);
    
  }
  
  
  
  
  
  class PluginManifest {
    String main;
    String name;
  }


  @Override
  public String getCommand() {
    return "plugin";
  }

  @Override
  public String[] getAliases() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getHelp() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getUsage() {
    // TODO Auto-generated method stub
    return null;
  }



}
