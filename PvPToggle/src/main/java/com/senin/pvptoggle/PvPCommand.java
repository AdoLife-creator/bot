package com.senin.pvptoggle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PvPCommand implements CommandExecutor {

    private final PvPTogglePlugin plugin;

    public PvPCommand(PvPTogglePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut sadece oyuncular için!");
            return true;
        }

        if (!player.hasPermission("pvptoggle.use")) {
            player.sendMessage(color(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        UUID uuid = player.getUniqueId();

        if (plugin.getPvpEnabled().contains(uuid)) {
            plugin.getPvpEnabled().remove(uuid);
            player.sendMessage(color(plugin.getConfig().getString("messages.pvp-disabled")));
        } else {
            plugin.getPvpEnabled().add(uuid);
            player.sendMessage(color(plugin.getConfig().getString("messages.pvp-enabled")));
        }

        return true;
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}