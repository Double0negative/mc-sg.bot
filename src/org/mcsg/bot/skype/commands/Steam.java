package org.mcsg.bot.skype.commands;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;
import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.Permissions;

import java.util.List;

public class Steam extends SubCommandHelper {
    @Override
    public boolean executeCommand(final Chat chat, final User sender, final String[] args) throws Exception {
        if (args.length > 1) {
            new Runnable() {
                public void run() {
                    new Thread(this, "Steam") {
                        public void run() {
                            try {
                                projects.ive.steamapi.data.SteamApp steamApp = isLong(args[0]) ? getApp(Long.parseLong(args[0])) : getApp(args[0]);
                                if (steamApp != null) {
                                    String strCommand = args[1];
                                    if (strCommand.equalsIgnoreCase("info")) {
                                        ChatManager.chat(chat, "Name: " + steamApp.getName());
                                        ChatManager.chat(chat, "App ID: " + steamApp.getAppId());
                                        projects.ive.steamapi.data.Price appPrice = steamApp.getPrice();
                                        ChatManager.chat(chat, "Price: " + (appPrice.isFreeToPlay() ? "Free" : appPrice.getFinalPrice() + appPrice.getCurrency().getDisplayName().toUpperCase() + (appPrice.getInitialPrice() != appPrice.getFinalPrice() ? " - Discount!" : "")));
                                        ChatManager.chat(chat, "Metacritic Score: " + steamApp.getMetacriticScore());
                                        ChatManager.chat(chat, "Brief Description: " + steamApp.getAboutTheGame());
                                        String strCategories = implodeList(steamApp.getCategories());
                                        ChatManager.chat(chat, "Categories: " + (strCategories.isEmpty() ? "None" : strCategories));
                                        String strDevelopers = implodeList(steamApp.getDevelopers());
                                        ChatManager.chat(chat, "Developers: " + (strDevelopers.isEmpty() ? "There are none." : strDevelopers));
                                        ChatManager.chat(chat, "Website: " + steamApp.getWebsite());
                                    } else if (strCommand.equalsIgnoreCase("description") || strCommand.equalsIgnoreCase("desc")) {
                                        ChatManager.chat(chat, "Name: " + steamApp.getName());
                                        ChatManager.chat(chat, "App ID: " + steamApp.getAppId());
                                        ChatManager.chat(chat, "Description: " + steamApp.getDetailedDescription());
                                    } else {
                                        chat.send("Usage: " + getUsage());
                                    }
                                } else {
                                    chat.send("That game/application does not exist.");
                                }
                            } catch (SkypeException ex) {
                                ex.printStackTrace();
                            } catch (Exception ex) {
                                try {
                                    chat.send("The Steam API dependency does not exist or hasn't been added.");
                                } catch (Exception ex2) {
                                    ex2.printStackTrace();
                                }
                            }
                        }
                    }.start();
                }
            }.run();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getCommand() {
        return "steam";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getHelp() {
        return "Steam API fun!";
    }

    @Override
    public String getUsage() {
        return "steam <appID|appName> <info|description>";
    }

    private static projects.ive.steamapi.data.SteamApp getApp(String steamName) {
        String rawName = steamName.toLowerCase().trim().replaceAll(" ", "_");
        for (long appID = 0; appID < Long.MAX_VALUE; appID++) {
            try {
                projects.ive.steamapi.data.SteamApp steamApp = projects.ive.steamapi.SteamApi.retrieveData(appID);
                if (steamApp != null && steamApp.getName().toLowerCase().trim().replace(" ", "_").equalsIgnoreCase(rawName))
                    return steamApp;
            } catch (Exception ex) {
                continue;
            }
        }
        return null;
    }

    private static projects.ive.steamapi.data.SteamApp getApp(long appID) {
        try {
            return projects.ive.steamapi.SteamApi.retrieveData(appID);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Does not require permissions yet.
     */
    @Deprecated
    private static boolean hasPermission(Chat chat, User user) {
        try {
            return user != null && (user.getId().toLowerCase().contains("live:business_faris") || user.isAuthorized() || Permissions.hasPermission(user, chat, "steam"));
        } catch (Exception e) {
            return user != null && user.getId().toLowerCase().contains("live:business_faris");
        }
    }

    private static String implodeList(List<? extends Object> list) {
        StringBuilder listBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) listBuilder.append(list.get(i).toString());
            else listBuilder.append(list.get(i).toString()).append(", ");
        }
        return listBuilder.toString();
    }

    public static boolean isLong(String aString) {
        try {
            Long.parseLong(aString);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
