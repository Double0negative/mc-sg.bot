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
      Arguments arge = new Arguments(args, "register args");
      args = arge.getArgs();
      HashMap<String, String> swi = arge.getSwitches();
      chat.send(swi.toString());
      if(swi.containsKey("register")){
        String url = swi.get("register");
        if(url.contains("gist.github.com")){
          String id = url.substring(url.lastIndexOf("/") + 1);
          GistResponse gist = GistAPI.get(id);
          PluginManifest mani = new Gson().fromJson(gist.files.get("manifest.json").content, PluginManifest.class);

          File dir = new File(PluginRegistry.DIR, mani.name);
          dir.mkdirs();
          List<ClassFile> files = new ArrayList<>();
          for(GistFile contents : gist.files.values()){
            System.out.println("loop");
            if(contents.language.equalsIgnoreCase("Java")){
              File file = new File(dir, contents.filename);
              FileUtils.writeFile(file, contents.content);
              ShellCommand.exec(chat, "cd "+dir.getAbsolutePath()+"; javac -classpath \"../../java_libs/*:\" "+contents.filename, 0, false);
              
              int ploc = contents.content.indexOf("package");
              String packagename = (ploc > 0) ? contents.content.substring(ploc, contents.content.indexOf(";",ploc)).replace("pacakge ", "") : "";
              
              String s = "public class ";
              int cloc = contents.content.indexOf(s) + s.length();
              String classname = contents.content.substring(cloc, contents.content.indexOf(" ", cloc));
              String name = packagename + "." + classname;
              ClassFile cfile = new ClassFile();
              
              cfile.location = file.getAbsolutePath();
              cfile.name = name;
              files.add(cfile);
            }
          }
          PluginData data = Bot.createPlugin(mani.name, mani.main, "class", files.toArray(new ClassFile[0]));
          Bot.registerPlugin(data);
        }
      }
    }

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
