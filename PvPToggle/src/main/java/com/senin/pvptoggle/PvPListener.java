package com.senin.pvptoggle;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPListener implements Listener {

    private final PvPTogglePlugin plugin;

    public PvPListener(PvPTogglePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        boolean attackerHasPvP = plugin.getPvpEnabled().contains(attacker.getUniqueId());
        boolean victimHasPvP = plugin.getPvpEnabled().contains(victim.getUniqueId());

        if (!attackerHasPvP || !victimHasPvP) {
            event.setCancelled(true);
            attacker.sendMessage(color(plugin.getConfig().getString("messages.pvp-blocked")));
        }
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}