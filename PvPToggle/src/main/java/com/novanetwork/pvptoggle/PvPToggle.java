package com.novanetwork.pvptoggle;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.UUID;

public class PvPToggle extends JavaPlugin implements Listener {

    private final HashSet<UUID> pvpEnabled = new HashSet<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("PvPToggle aktif! - NovaNetwork");
    }

    @Override
    public void onDisable() {
        getLogger().info("PvPToggle deaktif!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komutu sadece oyuncular kullanabilir!");
            return true;
        }

        UUID uuid = player.getUniqueId();

        if (args.length == 0) {
            // /pvp → toggle
            if (pvpEnabled.contains(uuid)) {
                pvpEnabled.remove(uuid);
                player.sendMessage(ChatColor.RED + "✖ PvP " + ChatColor.BOLD + "kapatildi" + ChatColor.RESET + ChatColor.RED + "!");
            } else {
                pvpEnabled.add(uuid);
                player.sendMessage(ChatColor.GREEN + "✔ PvP " + ChatColor.BOLD + "acildi" + ChatColor.RESET + ChatColor.GREEN + "!");
            }
        } else if (args[0].equalsIgnoreCase("on")) {
            pvpEnabled.add(uuid);
            player.sendMessage(ChatColor.GREEN + "✔ PvP " + ChatColor.BOLD + "acildi" + ChatColor.RESET + ChatColor.GREEN + "!");
        } else if (args[0].equalsIgnoreCase("off")) {
            pvpEnabled.remove(uuid);
            player.sendMessage(ChatColor.RED + "✖ PvP " + ChatColor.BOLD + "kapatildi" + ChatColor.RESET + ChatColor.RED + "!");
        } else {
            player.sendMessage(ChatColor.YELLOW + "Kullanim: /pvp [on|off]");
        }

        return true;
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        boolean attackerPvP = pvpEnabled.contains(attacker.getUniqueId());
        boolean victimPvP = pvpEnabled.contains(victim.getUniqueId());

        if (!attackerPvP) {
            attacker.sendMessage(ChatColor.RED + "PvP'n kapali! Acmak icin: " + ChatColor.YELLOW + "/pvp on");
            event.setCancelled(true);
            return;
        }

        if (!victimPvP) {
            attacker.sendMessage(ChatColor.RED + victim.getName() + " adli oyuncunun PvP'si kapali!");
            event.setCancelled(true);
        }
    }
}