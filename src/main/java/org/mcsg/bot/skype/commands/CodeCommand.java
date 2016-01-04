package org.mcsg.bot.skype.commands;

import java.io.File;
import java.util.HashMap;

import org.mcsg.bot.skype.Settings;
import org.mcsg.bot.skype.util.FileUtils;
import org.mcsg.bot.skype.util.ShellCommand;
import org.mcsg.bot.skype.util.StringUtils;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.user.User;

public class CodeCommand implements SubCommand {

    private HashMap<String, CodeTemplate> templates = new HashMap<>();

    public CodeCommand() {
        templates.put("js", new CodeTemplate(null, "node {file}", null, ".js"));
        templates.put("groovy", new CodeTemplate("cd {cfiledir}; groovyc {cfile}", "groovy {file}", ".groovy", ""));
        templates.put("py", new CodeTemplate(null, "python {file}", null, ".py"));
        templates.put("rb", new CodeTemplate(null, "ruby {file}", null, ".rb"));
        templates.put("pl", new CodeTemplate(null, "perl {file}", null, ".pl"));

    }

    @Override
    public void execute(String cmd, Chat chat, User sender, String[] args) throws Exception {
        CodeTemplate temp = templates.get(cmd);
        if (temp != null) {
            long time = System.currentTimeMillis();
            File runFile = FileUtils.createTempFile(time + temp.getRunExt());
            if (temp.getCompileLine() != null) {
                File compileFile = FileUtils.createTempFile(time + temp.getCompileExt());
                FileUtils.writeFile(compileFile, StringUtils.implode(args));
                ShellCommand.exec(chat,
                        temp.getCompileLine().replace("{cfiledir}", compileFile.getParentFile().getAbsolutePath())
                                .replace("{cfile}", compileFile.getAbsolutePath()), 0, false, true);
            } else {
                FileUtils.writeFile(runFile, StringUtils.implode(args));
            }

            ShellCommand.exec(chat, temp.getRunLine().replace("{file}", runFile.getAbsolutePath()),
                    Settings.Root.Bot.chat.time, false, true);
        }
    }

    @Override
    public String getCommand() {
        return "code";
    }

    @Override
    public String[] getAliases() {
        return a("c", "js", "groovy", "py", "rb", "pl");
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

    private class CodeTemplate {
        private String compileLine;
        private String runLine;
        private String compileExt;
        private String runExt;

        public String getCompileLine() {
            return compileLine;
        }

        public String getRunLine() {
            return runLine;
        }

        public String getCompileExt() {
            return compileExt;
        }

        public String getRunExt() {
            return runExt;
        }

        public CodeTemplate(String compileLine, String runLine, String compileExt, String runExt) {
            this.compileLine = compileLine;
            this.runLine = runLine;
            this.compileExt = compileExt;
            this.runExt = runExt;
        }

    }

}
