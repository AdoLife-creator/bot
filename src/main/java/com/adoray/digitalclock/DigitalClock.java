package com.adoray.digitalclock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DigitalClock extends JavaPlugin {

    private static final Map<String, String> TIMEZONE_MAP = new HashMap<>();

    static {
        // Popüler zaman dilimleri
        TIMEZONE_MAP.put("istanbul", "Europe/Istanbul");
        TIMEZONE_MAP.put("london", "Europe/London");
        TIMEZONE_MAP.put("newyork", "America/New_York");
        TIMEZONE_MAP.put("losangeles", "America/Los_Angeles");
        TIMEZONE_MAP.put("tokyo", "Asia/Tokyo");
        TIMEZONE_MAP.put("sydney", "Australia/Sydney");
        TIMEZONE_MAP.put("dubai", "Asia/Dubai");
        TIMEZONE_MAP.put("bangkok", "Asia/Bangkok");
        TIMEZONE_MAP.put("singapore", "Asia/Singapore");
        TIMEZONE_MAP.put("hongkong", "Asia/Hong_Kong");
        TIMEZONE_MAP.put("delhi", "Asia/Kolkata");
        TIMEZONE_MAP.put("moscow", "Europe/Moscow");
        TIMEZONE_MAP.put("saopaulo", "America/Sao_Paulo");
        TIMEZONE_MAP.put("dubai", "Asia/Dubai");
    }

    @Override
    public void onEnable() {
        // /clock komutu
        getCommand("clock").setExecutor((sender, cmd, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cBu komut sadece oyuncular için!");
                return false;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("digitalclock.use")) {
                player.sendMessage("§cBu komutu kullanma izniniz yok!");
                return false;
            }

            if (args.length == 0) {
                // Sunucu zaman dilimi
                displayAllClocks(player);
            } else {
                // Belirtilen zaman dilimi
                String timezone = args[0].toLowerCase();
                displayClock(player, timezone);
            }

            return true;
        });

        // /clocklist komutu
        getCommand("clocklist").setExecutor((sender, cmd, label, args) -> {
            if (!sender.hasPermission("digitalclock.list")) {
                sender.sendMessage("§cBu komutu kullanma izniniz yok!");
                return false;
            }

            listTimezones(sender);
            return true;
        });

        getLogger().info("DigitalClock plugin aktif edildi!");
    }

    private void displayClock(Player player, String timezone) {
        try {
            String zoneId = TIMEZONE_MAP.getOrDefault(timezone, null);
            
            if (zoneId == null) {
                player.sendMessage("§c❌ Bilinmeyen zaman dilimi: " + timezone);
                player.sendMessage("§e/clocklist yazarak mevcut dilimleri görebilirsiniz.");
                return;
            }

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(zoneId));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss | EEEE, dd MMMM yyyy");
            String timeString = now.format(formatter);

            player.sendMessage("§6╔════════════════════════════════╗");
            player.sendMessage("§6║ §e" + timezone.toUpperCase() + " SAATI");
            player.sendMessage("§6║ §b" + timeString);
            player.sendMessage("§6║ UTC" + (now.getOffset().getTotalSeconds() >= 0 ? "+" : "") + 
                    String.format("%d:%02d", 
                    now.getOffset().getTotalSeconds() / 3600, 
                    (Math.abs(now.getOffset().getTotalSeconds()) % 3600) / 60));
            player.sendMessage("§6╚════════════════════════════════╝");

        } catch (Exception e) {
            player.sendMessage("§c❌ Hata: " + e.getMessage());
            getLogger().warning("Clock hatası: " + e.getMessage());
        }
    }

    private void displayAllClocks(Player player) {
        player.sendMessage("§6╔════════════════════════════════╗");
        player.sendMessage("§6║ §e🕐 DÜNYA SAATLERİ");
        player.sendMessage("§6╚════════════════════════════════╝");

        for (Map.Entry<String, String> entry : TIMEZONE_MAP.entrySet()) {
            try {
                ZonedDateTime now = ZonedDateTime.now(ZoneId.of(entry.getValue()));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String timeString = now.format(formatter);

                player.sendMessage(String.format("§e%15s §6→ §b%s", 
                    entry.getKey().toUpperCase(), timeString));

            } catch (Exception e) {
                getLogger().warning("Timezone hatası: " + entry.getValue());
            }
        }

        player.sendMessage("§6════════════════════════════════");
        player.sendMessage("§e/clock <şehir> §6yazarak detaylı saat görebilirsiniz.");
    }

    private void listTimezones(CommandSender sender) {
        sender.sendMessage("§6╔════════════════════════════════╗");
        sender.sendMessage("§6║ §eMevcut Zaman Dilimleri:");
        sender.sendMessage("§6╚════════════════════════════════╝");

        int count = 0;
        for (String timezone : TIMEZONE_MAP.keySet()) {
            sender.sendMessage("§e  • " + timezone);
            count++;
            if (count % 3 == 0) {
                sender.sendMessage("");
            }
        }

        sender.sendMessage("§6════════════════════════════════");
        sender.sendMessage("§bToplam: §e" + TIMEZONE_MAP.size() + " zaman dilimi");
    }

    @Override
    public void onDisable() {
        getLogger().info("DigitalClock plugin kapatıldı.");
    }
}