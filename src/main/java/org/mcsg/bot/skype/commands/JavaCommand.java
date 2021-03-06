package org.mcsg.bot.skype.commands;

import java.io.File;
import java.util.HashMap;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.Permissions;
import org.mcsg.bot.skype.util.Arguments;
import org.mcsg.bot.skype.util.FileUtils;
import org.mcsg.bot.skype.util.ShellCommand;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.web.WebClient;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class JavaCommand implements SubCommand {

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        if (Permissions.hasPermission(sender, chat, "java")) {
            String templatelink = "http://pastebin.com/raw.php?i=fES8cVZX";
            String template = "";
            String code = "";
            long execap = 20000;

            Arguments arge = new Arguments(args, "template/temp/t args", "paste/p args", "nolimit", "limit/l args",
                    "code/c", "imports/import/i args");
            HashMap<String, String> swi = arge.getSwitches();
            args = arge.getArgs();
            String input = StringUtils.implode(args);

            if (swi.containsKey("paste")) {
                String url = swi.get("paste");
                String link = getPasteLink(url);
                // System.out.println(link);
                code = WebClient.request(link);
                code = code.replace("$code", "");
            } else {
                code = StringUtils.implode(args);

                if (swi.containsKey("template")) {
                    String link = getPasteLink(swi.get("template"));
                    // System.out.println(link);
                    templatelink = link;
                }

                template = WebClient.request(templatelink);
                code = template.replace("$code", input);

            }

            if (swi.containsKey("imports")) {
                String[] split = swi.get("imports").split(":");
                StringBuilder sb = new StringBuilder();
                for (String imp : split) {
                    if (imp.startsWith("s/")) {
                        sb.append("import static " + imp.substring(2) + ";");
                    } else {
                        sb.append("import " + imp + ";");
                    }
                    sb.append("\n");
                }
                code = code.replace("$imports", sb.toString());
            } else {
                code = code.replace("$imports", "");
            }

            if (Permissions.hasPermission(sender, chat, "java.cap")) {
                if (swi.containsKey("nolimit")) {
                    execap = 0;
                } else if (swi.containsKey("limit")) {
                    execap = Long.parseLong(swi.get("limit"));
                }
            }

            runCode(chat, sender, code, execap, swi.containsKey("code"));

        } else {
            chat.sendMessage("No permission to execute command");
        }
    }

    public void runCode(Chat chat, User sender, String code, long cap, boolean b) throws Exception {
        int cindex = code.indexOf("class") + "class ".length();
        // chat.send(ChatManager.createPaste(cindex+" "+code.indexOf(" ",
        // cindex)+"\n"+code));
        String name = code.substring(cindex, code.indexOf(" ", cindex)).trim();

        File javaf = new File("files/", name + ".java");
        File javac = new File("files/", name + ".class");

        FileUtils.writeFile(javaf, code);
        int id = ShellCommand.exec(chat, "cd files; javac -classpath \"../java_libs/*:\" " + name
                + ".java && java -classpath \"../java_libs/*:\" " + name, cap, false);

        if (b)
            ChatManager.chat(chat, sender, "Running java code. ID " + id + ". Code: " + ChatManager.createPaste(code));

        javaf.deleteOnExit();
        javac.deleteOnExit();
    }

    public String getPasteLink(String url) {
        String gist = "https://gist.githubusercontent.com/$id/raw";
        String paste = "http://pastebin.com/raw.php?i=$id";

        String id = "";

        if (url.contains("pastebin")) {
            id = url.substring(url.lastIndexOf("/") + 1);
            return paste.replace("$id", id);
        } else if (url.contains("github")) {
            id = url.substring(url.indexOf("/", 15) + 1);
            return gist.replace("$id", id);
        }
        return "";
    }

    @Override
    public String getUsage() {
        return ".java [code] [-template paste|gist] [-paste paste|gist] [-code/c] [-limit] [-nolimit]";
    }

    @Override
    public String getCommand() {
        return "java";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getHelp() {
        return "Runs Java code";
    }

}
