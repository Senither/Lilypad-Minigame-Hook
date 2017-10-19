package com.senither.lilypad.minigame.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Envoyer {

    private static final Logger logger;

    static {
        logger = Logger.getLogger("Lilypad-Minigame-Hook");
    }

    public static Logger getLogger() {
        return logger;
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorize(List<String> messages) {
        List<String> message = new ArrayList<>();
        for (String str : messages) {
            if (str == null) {
                continue;
            }
            message.add(colorize("&7" + str));
        }
        return message;
    }

    public static String decolorize(String message) {
        return ChatColor.stripColor(message);
    }

    public static List<String> decolorize(List<String> messages) {
        List<String> message = new ArrayList<>();
        for (String str : messages) {
            if (str == null) {
                continue;
            }
            message.add(decolorize(str));
        }
        return message;
    }

    public static void missingPermission(Player player, String permission) {
        player.sendMessage(ChatColor.RED + "Influent permissions to execute this command.");
        player.sendMessage(ChatColor.RED + "You're missing the permission node " + ChatColor.ITALIC + permission);
    }

    public static void missingPermission(CommandSender player, String permission) {
        player.sendMessage(ChatColor.RED + "Influent permissions to execute this command.");
        player.sendMessage(ChatColor.RED + "You're missing the permission node " + ChatColor.ITALIC + permission);
    }

    public static void sendMessage(CommandSender player, String message) {
        player.sendMessage(colorize(message));
    }

    public static void broadcast(String string) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendMessage(player, string);
        }
    }
}