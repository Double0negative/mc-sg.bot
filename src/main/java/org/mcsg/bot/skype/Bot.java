package org.mcsg.bot.skype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.mcsg.bot.skype.commands.Connect4Command;
import org.mcsg.bot.skype.commands.DefineCommand;
import org.mcsg.bot.skype.commands.GameStatsCommand;
import org.mcsg.bot.skype.commands.GenImageCommand;
import org.mcsg.bot.skype.commands.GetUsersCommand;
import org.mcsg.bot.skype.commands.GitHubListenerCommand;
import org.mcsg.bot.skype.commands.HeartCommand;
import org.mcsg.bot.skype.commands.HiCommand;
import org.mcsg.bot.skype.commands.ImageSearchCommand;
import org.mcsg.bot.skype.commands.IsCommand;
import org.mcsg.bot.skype.commands.JavaCommand;
import org.mcsg.bot.skype.commands.KickCommand;
import org.mcsg.bot.skype.commands.KillProcCommand;
import org.mcsg.bot.skype.commands.LeaveCommand;
import org.mcsg.bot.skype.commands.ManageChatCommand;
import org.mcsg.bot.skype.commands.MinecraftPingCommand;
import org.mcsg.bot.skype.commands.PermissionCommand;
import org.mcsg.bot.skype.commands.PingCommand;
import org.mcsg.bot.skype.commands.ProcInCommand;
import org.mcsg.bot.skype.commands.RandomNumberCommand;
import org.mcsg.bot.skype.commands.ShellCommand;
import org.mcsg.bot.skype.commands.ShellWriteCommand;
import org.mcsg.bot.skype.commands.SourceCommand;
import org.mcsg.bot.skype.commands.StopCommand;
import org.mcsg.bot.skype.commands.SubCommand;
import org.mcsg.bot.skype.commands.TicTacToeCommand;
import org.mcsg.bot.skype.commands.UUIDCommandCommand;
import org.mcsg.bot.skype.commands.VersionCommand;
import org.mcsg.bot.skype.commands.VideoSearchCommand;
import org.mcsg.bot.skype.commands.WeatherCommand;
import org.mcsg.bot.skype.commands.WebAbstractCommand;
import org.mcsg.bot.skype.commands.WebSearchCommand;
import org.mcsg.bot.skype.commands.WikipediaSearchCommand;
import org.mcsg.bot.skype.message.MessagePaster;
import org.mcsg.bot.skype.web.GistAPI;
import org.mcsg.bot.skype.web.GithubListener;

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

    public static final String version = "3.00 Fluffy Clouds ";

    private static Skype skype;

    private static HashMap<String, SubCommand> commands = new HashMap<>();
    private static HashMap<String, SubCommand> aliases = new HashMap<>();
    private static HashMap<SubCommand, McsgBotPlugin> plug_comand = new HashMap<>();

    public static final HashMap<Chat, Integer> messageCount = new HashMap<Chat, Integer>();

    public static void main(String[] args) throws Exception {
        System.out.println("Starting MC-SG.bot version " + version);

        new Bot().start();

    }

    public void start() throws Exception {
        Settings.load();

        System.out.println("Logging in to skype account...");

        skype = new SkypeBuilder(Settings.Root.Bot.username, Settings.Root.Bot.password).withAllResources().build();
        skype.login();

        System.out.println("Log in successful. Starting bot.");

        ChatManager.start();
        GithubListener.listen();

        registerCommand(new PingCommand());
        registerCommand(new MinecraftPingCommand());
        registerCommand(new HiCommand());
        registerCommand(new RandomNumberCommand());
        registerCommand(new HeartCommand());
        registerCommand(new KickCommand());
        registerCommand(new GetUsersCommand());
        registerCommand(new ShellCommand());
        registerCommand(new PermissionCommand());
        registerCommand(new ShellWriteCommand());
        registerCommand(new KillProcCommand());
        registerCommand(new ProcInCommand());
        registerCommand(new StopCommand());
        registerCommand(new SourceCommand());
        registerCommand(new ManageChatCommand());
        registerCommand(new WikipediaSearchCommand());
        registerCommand(new WebSearchCommand());
        registerCommand(new WebAbstractCommand());
        registerCommand(new DefineCommand());
        registerCommand(new ImageSearchCommand());
        registerCommand(new VideoSearchCommand());
        registerCommand(new LeaveCommand());
        registerCommand(new JavaCommand());
        registerCommand(new IsCommand());
        registerCommand(new UUIDCommandCommand());
        registerCommand(new WeatherCommand());
        registerCommand(new VersionCommand());
        registerCommand(new Connect4Command());
        registerCommand(new TicTacToeCommand());
        registerCommand(new GameStatsCommand());
        registerCommand(new GenImageCommand());
        registerCommand(new GitHubListenerCommand());
        registerCommand(new RegisterPluginCommand());

        skype.getEventDispatcher().registerListener(new Listener() {
            @EventHandler
            public void onMessage(MessageReceivedEvent e) {

                ChatMessage recieved = e.getMessage();
                String text = recieved.getContent().asPlaintext();

                int count = messageCount.getOrDefault(recieved.getChat(), 0);
                messageCount.put(recieved.getChat(), count + 1);

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
                        executeAsync(sub, getCommand(split), recieved.getChat(), recieved.getSender(), args);
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
        System.out.println("Bot active, listening for commands.");
        if (Settings.Root.Bot.lastchat != null && Settings.Root.Bot.lastchat.length() > 0) {
            Chat chat = skype.getOrLoadChat(Settings.Root.Bot.lastchat);
            if (chat != null) {
                chat.sendMessage("Bot version " + version + " has started.");
            }
            Settings.Root.Bot.lastchat = "";
            Settings.save();
        }
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

    public void executeAsync(final SubCommand sub, final String cmd, final Chat chat, final User sender,
            final String[] args) {
        new Thread() {
            public void run() {
                try {
                    sub.execute(cmd, chat, sender, args);
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
