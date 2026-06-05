package com.adoray.jokegenerator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class JokeGenerator extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("joke").setExecutor((sender, cmd, label, args) -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cBu komut sadece oyuncular için!");
                return false;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("jokegenerator.use")) {
                player.sendMessage("§cBu komutu kullanma izniniz yok!");
                return false;
            }

            // Asynchronous olarak joke çek
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                try {
                    String joke = fetchJoke();
                    Bukkit.getScheduler().runTask(this, () -> {
                        player.sendMessage("§6═══════════════════");
                        player.sendMessage("§e" + joke);
                        player.sendMessage("§6═══════════════════");
                    });
                } catch (Exception e) {
                    player.sendMessage("§cJoke alınamadı!");
                    getLogger().warning("Joke hatası: " + e.getMessage());
                }
            });

            return true;
        });

        getLogger().info("JokeGenerator plugin aktif edildi!");
    }

    private String fetchJoke() throws Exception {
        URL url = new URL("https://official-joke-api.appspot.com/random_joke");
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        reader.close();

        String setup = jsonObject.get("setup").getAsString();
        String punchline = jsonObject.get("punchline").getAsString();

        return setup + "\n§b" + punchline;
    }

    @Override
    public void onDisable() {
        getLogger().info("JokeGenerator plugin kapatıldı.");
    }
}