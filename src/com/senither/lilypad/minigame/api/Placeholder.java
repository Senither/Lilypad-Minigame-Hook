package com.senither.lilypad.minigame.api;

import org.bukkit.plugin.Plugin;

public class Placeholder {

    private final Plugin plugin;
    private final String key;
    private final Closure value;

    public Placeholder(Plugin plugin, String key, Closure value) {
        this.plugin = plugin;
        this.key = key;
        this.value = value;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getKey() {
        return key;
    }

    public Closure getClosure() {
        return value;
    }

    public String getValue() {
        if (plugin != null && plugin.isEnabled()) {
            return value.onPlaceholder();
        }
        return null;
    }
}
