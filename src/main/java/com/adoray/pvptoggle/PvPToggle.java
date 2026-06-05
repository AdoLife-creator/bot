package com.adoray.pvptoggle;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PvPToggle extends JavaPlugin implements Listener {

    private final Set<UUID> pvpDisabled = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("pvp").setExecutor((sender, cmd, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cBu komut sadece oyuncular için!");
                return false;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("pvptoggle.use")) {
                player.sendMessage("§cBu komutu kullanma izniniz yok!");
                return false;
            }

            UUID playerUUID = player.getUniqueId();

            if (pvpDisabled.contains(playerUUID)) {
                pvpDisabled.remove(playerUUID);
                player.sendMessage("§a✓ PvP aktif edildi!");
            } else {
                pvpDisabled.add(playerUUID);
                player.sendMessage("§c✗ PvP deaktif edildi!");
            }

            return true;
        });

        getLogger().info("PvPToggle plugin aktif edildi! (Paper 1.21.4-1.21.8)");
    }

    @Override
    public void onDisable() {
        pvpDisabled.clear();
        getLogger().info("PvPToggle plugin kapatıldı.");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (pvpDisabled.contains(damager.getUniqueId())) {
                event.setCancelled(true);
            }
        }

        if (event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                if (pvpDisabled.contains(damaged.getUniqueId()) || pvpDisabled.contains(damager.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}