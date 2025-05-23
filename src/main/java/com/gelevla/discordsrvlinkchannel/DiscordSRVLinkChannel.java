package com.gelevla.discordsrvlinkchannel;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscordSRVLinkChannel extends JavaPlugin {

    private JDAListener jdaListener;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Check if DiscordSRV is available
        if (!isDiscordSRVAvailable()) {
            getLogger().severe("DiscordSRV not found! This plugin requires DiscordSRV to function.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize JDA listener
        jdaListener = new JDAListener(this);

        // Register the listener with DiscordSRV
        DiscordUtil.getJda().addEventListener(jdaListener);

        getLogger().info("DiscordSRV-LinkChannel has been enabled!");
    }

    @Override
    public void onDisable() {
        // Unregister the listener
        if (jdaListener != null && DiscordUtil.getJda() != null) {
            DiscordUtil.getJda().removeEventListener(jdaListener);
        }

        getLogger().info("DiscordSRV-LinkChannel has been disabled!");
    }

    private boolean isDiscordSRVAvailable() {
        return getServer().getPluginManager().getPlugin("DiscordSRV") != null &&
                DiscordSRV.getPlugin() != null;
    }
}