package com.senither.lilypad.minigame.api;

import com.senither.lilypad.minigame.LilypadMinigameHook;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

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

    /**
     * Registers a dynamic placeholder that can be used on any of
     * the servers defined as a lobby server via the config file.
     *
     * @param plugin      The main instance of the plugin attempting to register the placeholder.
     * @param placeholder The string version of the placeholder.
     * @param value       The value of the placeholder, either a Closure or a lambda expression must be given.
     * @return {@code true} if the placeholder was registered correctly, {@code false} if it failed somewhere, or the placeholder already exists.
     */
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

    /**
     * Gets the size of the placeholders map, for
     * all placeholders currently registered.
     *
     * @return The size of the placeholders map.
     */
    public static int getPlaceholderCount() {
        return placeholders.size();
    }

    /**
     * Get an immutable list of placeholder keys, each string corresponds to a
     * placeholder that has already been registered with Lilypad Minigame Hook.
     *
     * @return An immutable list of placeholder keys.
     */
    public static List<String> getPlaceholders() {
        List<String> keys = new ArrayList<>();
        keys.addAll(placeholders.keySet());

        return Collections.unmodifiableList(keys);
    }

    /**
     * Teleports(Transfers) the given player to the given server.
     *
     * @param player The player that should be teleported.
     * @param server The name of the server the player should be teleported to.
     * @return {@code true} on success, {@code false} otherwise.
     */
    public static boolean teleportPlayer(String player, String server) {
        return LilypadMinigameHook.getInstance().getNetwork().teleportRequest(player, server);
    }

    /**
     * Checks the display status of the server, if display is set to {@code true} the server will
     * be displayed on game-boards, and if it's set to false it will be hidden from game-boards.
     *
     * @return {@code true} if the game is being displayed on game-boards, {@code false} otherwise.
     */
    public static boolean isDisplay() {
        return display;
    }

    /**
     * Sets the display status of the server, if the value given is {@code true} the server will be
     * displayed on game-boards, and if it's set to false it will be hidden from game-boards.
     */
    public static void setDisplay(boolean value) {
        display = value;
    }

    /**
     * Invokes the placeholder with the given name if it exists, returning
     * the value produced by the placeholders closure callback.
     *
     * @param placeholder
     * @return {@code null} if the given placeholder is a valid placeholder, or some {@code string} value that represents the content of the placeholder given by the {@code Closure}.
     * @see com.senither.lilypad.minigame.api.Closure Closure
     */
    public static String invokePlaceholder(String placeholder) {
        if (!placeholders.containsKey(placeholder)) {
            return null;
        }
        return placeholders.get(placeholder).getValue();
    }

    /**
     * Loops over and invokes all the placeholders, storing the placeholder data in a
     * {@code HashMap}, where the key is the placeholder string value, and the value
     * is the result returned from invoking the placeholder {@code Closure} method.
     * <p>
     * This method is used internally to get the value of all the placeholders all at once.
     *
     * @return An immutable map of placeholders, where the key is the placeholder name, and the value is the result returned from invoking the placeholder {@code Closure} method.
     * @see com.senither.lilypad.minigame.api.Closure Closure
     * @see com.senither.lilypad.minigame.api.LilypadMinigameAPI#invokePlaceholder(String) invokePlaceholder(String)
     */
    public static Map<String, String> invokePlaceholders() {
        HashMap<String, String> callback = new HashMap<>();

        for (String placeholder : placeholders.keySet()) {
            callback.put(placeholder, invokePlaceholder(placeholder));
        }

        return Collections.unmodifiableMap(callback);
    }
}
