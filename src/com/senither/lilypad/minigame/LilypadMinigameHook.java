package com.senither.lilypad.minigame;

import com.senither.lilypad.minigame.commands.AuthorCommand;
import com.senither.lilypad.minigame.commands.LeaveCommand;
import com.senither.lilypad.minigame.network.NetworkManager;
import com.senither.lilypad.minigame.utils.Envoyer;
import lilypad.client.connect.api.Connect;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class LilypadMinigameHook extends JavaPlugin {

    private final List<String> lobbies = new ArrayList<>();

    private Connect connect;
    private NetworkManager network;

    public static LilypadMinigameHook getInstance() {
        return JavaPlugin.getPlugin(LilypadMinigameHook.class);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Plugin plugin = getServer().getPluginManager().getPlugin("LilyPad-Connect");
        if (plugin == null) {
            setEnabled(false);
            Envoyer.getLogger().log(Level.SEVERE, "LilypadMinigameHook was shut down since LilyPad-Connect was not found!");
            return;
        }

        connect = getServer().getServicesManager().getRegistration(Connect.class).getProvider();

        for (String server : getConfig().getStringList("lobbies")) {
            if (!lobbies.contains(server)) {
                lobbies.add(server);
            }
        }

        connect.registerEvents(network = new NetworkManager(this));
        network.setGameChannel(getConfig().getString("channel", "GLOBAL"));

        getCommand("lilypadminigame").setExecutor(new AuthorCommand(this));
        getCommand("leave").setExecutor(new LeaveCommand(this, getConfig().getBoolean("leave-command.enabled", true)));
    }

    @Override
    public void onDisable() {
        Random random = new Random();

        String server = lobbies.get(random.nextInt(lobbies.size()));
        Envoyer.getLogger().log(Level.INFO,
                "Attempting to teleport players to \"{0}\" before the server shuts down", server
        );

        for (Player player : getServer().getOnlinePlayers()) {
            getNetwork().teleportRequest(player.getName(), server);
        }

        // Attempts to sleep for 1½ second to slow down the shutdown
        // process to people get to leave the server before it shuts down.
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException ignored) {
        }

        connect.unregisterEvents(network);
    }

    public Connect getConnect() {
        return connect;
    }

    public NetworkManager getNetwork() {
        return network;
    }

    public List<String> getLobbies() {
        return lobbies;
    }
}
