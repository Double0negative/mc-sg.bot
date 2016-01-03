package org.mcsg.bot.skype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.mcsg.bot.skype.commands.Connect4;
import org.mcsg.bot.skype.commands.Define;
import org.mcsg.bot.skype.commands.GameStatsCommand;
import org.mcsg.bot.skype.commands.GenImage;
import org.mcsg.bot.skype.commands.GetUsers;
import org.mcsg.bot.skype.commands.GitHubListener;
import org.mcsg.bot.skype.commands.Heart;
import org.mcsg.bot.skype.commands.Hi;
import org.mcsg.bot.skype.commands.ImageSearch;
import org.mcsg.bot.skype.commands.Is;
import org.mcsg.bot.skype.commands.JavaCommand;
import org.mcsg.bot.skype.commands.Kick;
import org.mcsg.bot.skype.commands.KillProc;
import org.mcsg.bot.skype.commands.Leave;
import org.mcsg.bot.skype.commands.ManageChat;
import org.mcsg.bot.skype.commands.MinecraftPingCommand;
import org.mcsg.bot.skype.commands.Permission;
import org.mcsg.bot.skype.commands.Ping;
import org.mcsg.bot.skype.commands.ProcIn;
import org.mcsg.bot.skype.commands.RandomNumber;
import org.mcsg.bot.skype.commands.Shell;
import org.mcsg.bot.skype.commands.ShellWrite;
import org.mcsg.bot.skype.commands.Source;
import org.mcsg.bot.skype.commands.Stop;
import org.mcsg.bot.skype.commands.SubCommand;
import org.mcsg.bot.skype.commands.TicTacToe;
import org.mcsg.bot.skype.commands.UUIDCommand;
import org.mcsg.bot.skype.commands.Version;
import org.mcsg.bot.skype.commands.VideoSearch;
import org.mcsg.bot.skype.commands.Weather;
import org.mcsg.bot.skype.commands.WebAbstract;
import org.mcsg.bot.skype.commands.WebSearch;
import org.mcsg.bot.skype.commands.WikipediaSearchCommand;
import org.mcsg.bot.skype.message.MessagePaster;
import org.mcsg.bot.skype.web.GistAPI;
import org.mcsg.bot.skype.web.GithubListener;

import com.google.gson.Gson;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.user.User;

public class Bot {

    public static final String version = "2.03 Custom reading ";

    private static HashMap<String, SubCommand> commands = new HashMap<>();
    private static HashMap<String, SubCommand> aliases = new HashMap<>();
    private static HashMap<SubCommand, McsgBotPlugin> plug_comand = new HashMap<>();

    private static Skype skype;

    private static String defaultChat;
    public static final String LAST_FILE = "lastchat";

    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {

        new Bot().start();

    }

    public void start() throws Exception {
        Settings.load();

        skype = new SkypeBuilder(Settings.Root.Bot.username, Settings.Root.Bot.password).withAllResources().build();
        skype.login();

        System.out.println("Logged in");

        ChatManager.start();
        GithubListener.listen();

        registerCommand(new Ping());
        registerCommand(new MinecraftPingCommand());
        registerCommand(new Hi());
        registerCommand(new RandomNumber());
        registerCommand(new Heart());
        registerCommand(new Kick());
        registerCommand(new GetUsers());
        registerCommand(new Shell());
        registerCommand(new Permission());
        registerCommand(new ShellWrite());
        registerCommand(new KillProc());
        registerCommand(new ProcIn());
        registerCommand(new Stop());
        registerCommand(new Source());
        registerCommand(new ManageChat());
        registerCommand(new WikipediaSearchCommand());
        registerCommand(new WebSearch());
        registerCommand(new WebAbstract());
        registerCommand(new Define());
        registerCommand(new ImageSearch());
        registerCommand(new VideoSearch());
        registerCommand(new Leave());
        registerCommand(new JavaCommand());
        registerCommand(new Is());
        registerCommand(new UUIDCommand());
        registerCommand(new Weather());
        registerCommand(new Version());
        registerCommand(new Connect4());
        registerCommand(new TicTacToe());
        registerCommand(new GameStatsCommand());
        registerCommand(new GenImage());
        registerCommand(new GitHubListener());
        registerCommand(new RegisterPluginCommand());

        // PluginManager.loadPlugins(getDefaultChat());

        /*
         * skype.getEventDispatcher().registerListener(new Listener() {
         * @EventHandler public void onMessage(MessageReceivedEvent e) {
         * System.out.println("Got message: " + e.getMessage().getContent()); }
         * });
         */

        skype.getEventDispatcher().registerListener(new Listener() {
            @EventHandler
            public void onMessage(MessageReceivedEvent e) {
                System.out.println(e.getMessage().getContent().asPlaintext());

                ChatMessage recieved = e.getMessage();
                String text = recieved.getContent().asPlaintext();

                // org.mcsg.bot.skype.EventHandler.callEvent(new
                // org.mcsg.bot.skype.events.MessageReceivedEvent(recieved));

                if (text.startsWith(".")) {
                    String split[] = text.split(" ");
                    String command = getCommand(split);
                    String args[] = getArgs(split);
                    System.out.println("Received command: " + Arrays.toString(split) + " from "
                            + recieved.getSender().getDisplayName());
                    if (command.equalsIgnoreCase("help") || command.equalsIgnoreCase("halp")) {
                        StringBuilder sb = new StringBuilder();
                        try {
                            recieved.getChat().sendMessage("--- [ MC-SG Bot Help ] ---");
                        } catch (ConnectionException e1) {
                            e1.printStackTrace();
                        }
                        sb.append("#MC-SG.BOT Commands Reference \n");
                        for (String com : commands.keySet()) {
                            String name = commands.get(com).getUsage();
                            if (name != null) {
                                sb.append("- `" + commands.get(com).getUsage() + "` \n   "
                                        + commands.get(com).getHelp().replace("\n", "\n   ") + "\n\n");
                            }
                        }
                        try {
                            recieved.getChat().sendMessage("Help: " + GistAPI.paste("help.md", sb.toString()));
                        } catch (Exception ex) { // TODO Auto-generated catch
                                                 // block e.printStackTrace();
                        }
                    }
                    SubCommand sub = getCommand(command);
                    if (sub != null) {
                        executeAsync(sub, recieved.getChat(), recieved.getSender(), args);
                    }
                } else {
                    try {
                        MessagePaster.message(recieved);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

        skype.subscribe();
        System.out.println("subscripted");
    }

    public static SubCommand getCommand(String command) {
        if (commands.containsKey(command)) {
            return commands.get(command);
        } else if (aliases.containsKey(command)) {
            return aliases.get(command);
        } else
            return null;
    }

    public static void unregisterCommands(McsgBotPlugin plugin) {
        for (Entry<SubCommand, McsgBotPlugin> entry : plug_comand.entrySet()) {
            if (entry.getValue() == plugin) {
                unregisterCommand(entry.getKey());
            }
        }
    }

    public static void unregisterCommand(SubCommand command) {
        commands.remove(command.getCommand());
        if (command.getAliases() != null)
            for (String alias : command.getAliases())
                aliases.remove(alias);
    }

    private static void registerCommand(SubCommand command) {
        registerCommand(command, null);
    }

    public static void registerCommand(SubCommand command, McsgBotPlugin plugin) {
        if (commands.containsKey(command.getCommand())) {
            ChatManager
                    .printThrowable(getDefaultChat(),
                            new RuntimeException("Cannot register command: " + command.getCommand()
                                    + ". Command already exist"));
        } else {
            commands.put(command.getCommand(), command);
        }
        if (command.getAliases() != null) {
            for (String alias : command.getAliases()) {
                if (aliases.containsKey(alias)) {
                    ChatManager.printThrowable(getDefaultChat(), new RuntimeException("Cannot register alias: " + alias
                            + ". Alias already registered by " + aliases.get(alias).getCommand()));
                } else {
                    aliases.put(alias, command);
                }
            }
        }
        plug_comand.put(command, plugin);
    }

    private static Chat getDefaultChat() {
        return null;
    }

    public void executeAsync(final SubCommand sub, final Chat chat, final User sender, final String[] args) {
        new Thread() {
            public void run() {
                try {
                    sub.execute(chat, sender, args);
                } catch (Exception e) {
                    ChatManager.printThrowable(chat, e);
                }
            }
        }.start();
    }

    private String getCommand(String[] split) {
        return split[0].substring(1).toLowerCase();
    }

    public String[] getArgs(String[] split) {
        ArrayList<String> t = new ArrayList<String>(Arrays.asList(split));
        t.remove(0);
        return t.toArray(new String[t.size()]);
    }

    public static Chat getChat(String id) {
        return skype.getChat(id);
    }
}
