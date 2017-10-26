package com.senither.lilypad.minigame.commands;

import com.senither.lilypad.minigame.LilypadMinigameHook;
import com.senither.lilypad.minigame.utils.Envoyer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AuthorCommand implements CommandExecutor {

    private final LilypadMinigameHook plugin;

    public AuthorCommand(LilypadMinigameHook plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Envoyer.sendMessage(sender, String.format(
                "&bLilypad Minigame Hook &3v&b%s &3was developed by &bSenither",
                plugin.getDescription().getVersion()
        ));
        Envoyer.sendMessage(sender, "&3Plugin: &bhttps://github.com/Senither/Lilypad-Minigame-Hook");
        return true;
    }
}
