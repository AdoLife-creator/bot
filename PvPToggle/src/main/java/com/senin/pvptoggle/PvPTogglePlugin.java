package com.senin.pvptoggle;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.UUID;

public class PvPTogglePlugin extends JavaPlugin {

    private final HashSet<UUID> pvpEnabled = new HashSet<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("pvp").setExecutor(new PvPCommand(this));
        getServer().getPluginManager().registerEvents(new PvPListener(this), this);
        getLogger().info("PvPToggle aktif! ✅");
    }

    @Override
    public void onDisable() {
        getLogger().info("PvPToggle devre dışı.");
    }

    public HashSet<UUID> getPvpEnabled() {
        return pvpEnabled;
    }
}