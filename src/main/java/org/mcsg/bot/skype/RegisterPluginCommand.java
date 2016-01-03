package org.mcsg.bot.skype;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class RegisterPluginCommand implements SubCommand {

    @Override
    public void execute(Chat chat, User sender, String[] args) throws Exception {
        if (Permissions.hasPermission(sender, chat, "plugin")) {
            Arguments arge = new Arguments(args, "register", "update", "unregister");
            args = arge.getArgs();
            HashMap<String, String> swi = arge.getSwitches();

            if (swi.containsKey("unregister")) {
                String name = args[0];
                PluginManager.disablePlugin(name);
                PluginManager.unregisterPlugin(name);
                chat.sendMessage("Unregistered Plugin: " + name);
                return;
            }

            boolean register = swi.containsKey("register");
            boolean update = swi.containsKey("update");
            String url = args[0];

            if (url.contains("gist.github.com")) {
                loadFromGist(chat, url, register, update);
            }

        }

    }

    public void loadFromGist(Chat chat, String url, boolean register, boolean update) throws Exception {
        String id = url.substring(url.lastIndexOf("/") + 1);
        GistResponse gist = GistAPI.get(id);
        if (!gist.files.containsKey("manifest.json")) {
            ChatManager.chat(chat, "Failed to load plugin: Cannot find manifest file.");
            return;
        }
        PluginManifest mani = new Gson().fromJson(gist.files.get("manifest.json").content, PluginManifest.class);

        File dir = new File(PluginRegistry.DIR, mani.name);
        if (update) {
            ShellCommand.exec(chat, "cd " + PluginRegistry.DIR + "; rm -rf " + mani.name, 0, false);
            PluginManager.disablePlugin(mani.name);
            PluginManager.unregisterPlugin(mani.name);
        }
        dir.mkdirs();

        List<ClassFile> files = new ArrayList<>();
        for (GistFile contents : gist.files.values()) {
            if (contents.language.equalsIgnoreCase("Java")) {
                File file = new File(dir, contents.filename);
                FileUtils.writeFile(file, contents.content);

                int ploc = contents.content.indexOf("package");
                String packagename = (ploc > -1) ? contents.content
                        .substring(ploc, contents.content.indexOf(";", ploc)).replace("package ", "") + "." : "";

                String s = " class ";
                int cloc = contents.content.indexOf(s) + s.length();
                String classname = contents.content.substring(cloc, contents.content.indexOf("{", cloc)).trim();
                String name = packagename + classname;
                ClassFile cfile = new ClassFile();

                cfile.location = file.getAbsolutePath();
                cfile.name = name;
                files.add(cfile);
            }
        }
        ShellCommand.exec(chat, "javac -classpath \"/home/bot/java_libs/*:\" " + dir.getAbsolutePath() + "/*.java", 0,
                false, true);

        PluginData data = PluginManager.createPlugin(mani.name, mani.main, files.toArray(new ClassFile[0]));
        PluginManager.buildPlugin(data);
        PluginManager.registerPlugin(data, chat);

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
