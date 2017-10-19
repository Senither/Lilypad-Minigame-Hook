package com.senither.lilypad.minigame.api;

import com.senither.lilypad.minigame.LilypadMinigameHook;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LilypadMinigameAPI {

    private static final HashMap<String, Placeholder> placeholders = new HashMap<>();
    private static boolean display = true;

    static {
        LilypadMinigameHook plugin = JavaPlugin.getPlugin(LilypadMinigameHook.class);

        LilypadMinigameAPI.registerPlaceholder(plugin, "display", () -> LilypadMinigameAPI.isDisplay() ? "true" : "false");
        LilypadMinigameAPI.registerPlaceholder(plugin, "serverMotd", () -> plugin.getServer().getMotd());
        LilypadMinigameAPI.registerPlaceholder(plugin, "playersMax", () -> "" + plugin.getServer().getMaxPlayers());
        LilypadMinigameAPI.registerPlaceholder(plugin, "playersOnline", () -> "" + plugin.getServer().getOnlinePlayers().size());
    }

    public static boolean registerPlaceholder(Plugin plugin, String placeholder, Closure value) {
        if (plugin == null || placeholder == null || placeholder.trim().length() == 0 || value == null) {
            return false;
        }

        if (placeholders.containsKey(placeholder)) {
            return false;
        }

        placeholders.put(placeholder, new Placeholder(plugin, placeholder, value));
        return true;
    }

    public static boolean teleportPlayer(String player, String server) {
        return LilypadMinigameHook.getInstance().getNetwork().teleportRequest(player, server);
    }

    public static int getPlaceholderCount() {
        return placeholders.size();
    }

    public static boolean isDisplay() {
        return display;
    }

    public static void setDisplay(boolean value) {
        display = value;
    }

    public static List<String> getPlaceholders() {
        List<String> keys = new ArrayList<>();
        keys.addAll(placeholders.keySet());
        
        return Collections.unmodifiableList(keys);
    }

    public static String invokePlaceholder(String placeholder) {
        if (!placeholders.containsKey(placeholder)) {
            return null;
        }
        return placeholders.get(placeholder).getValue();
    }

    public static HashMap<String, String> invokePlaceholders() {
        HashMap<String, String> callback = new HashMap<>();

        for (String placeholder : placeholders.keySet()) {
            callback.put(placeholder, invokePlaceholder(placeholder));
        }

        return callback;
    }
}
