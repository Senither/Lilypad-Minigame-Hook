package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.LilypadMinigameHook;
import com.senither.lilypad.minigame.api.LilypadMinigameAPI;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;

public class LeaveCommand implements CommandExecutor {

    private final Random random = new Random();
    private final HashMap<String, Long> intervalCheck = new HashMap<>();

    private final boolean enabled;
    private final LilypadMinigameHook plugin;
    private final String disabledMessage;

    public LeaveCommand(LilypadMinigameHook plugin, boolean enabled) {
        this.plugin = plugin;
        this.enabled = enabled;

        disabledMessage = this.enabled ? null : plugin.getConfig().getString("leave-command.disabled-message");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (!enabled) {
            Envoyer.sendMessage(sender, disabledMessage);
            return false;
        }

        Player player = ((Player) sender).getPlayer();

        if (!intervalCheck.containsKey(player.getName())) {
            aboutToLeaveMessage(player);
            return true;
        }

        if (intervalCheck.get(player.getName()) >= System.currentTimeMillis()) {
            String name = plugin.getLobbies().get(
                    random.nextInt(plugin.getLobbies().size())
            );

            return LilypadMinigameAPI.teleportPlayer(player.getName(), name);
        }
        return false;
    }

    private void aboutToLeaveMessage(Player player) {
        intervalCheck.put(player.getName(), System.currentTimeMillis() + (8 * 1000));
        Envoyer.sendMessage(player, "&cYou are about to leave the server!");
        Envoyer.sendMessage(player, "&cUse &4/leave &cagain if you want to continue.");
    }
}
