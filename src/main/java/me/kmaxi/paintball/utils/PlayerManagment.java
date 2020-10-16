package me.kmaxi.paintball.utils;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class PlayerManagment {
    private final PaintballMain plugin;

    public PlayerManagment(PaintballMain plugin) {
        this.plugin = plugin;
    }

    public void teleportToSpawnPoint(UUID uuid){
        Player player = Bukkit.getPlayer(uuid);
        if (plugin.gameManager.players.get(uuid).getTeam().equals("red")){
            int spawnPointAmount = plugin.gameManager.redSpawnPoints.size();
            spawnPointAmount++;
            int randomNumber = new Random().nextInt(spawnPointAmount);
            player.teleport(plugin.gameManager.redSpawnPoints.get(randomNumber));
            plugin.gameManager.players.get(uuid).setAlive(true);
            return;
        }
        if (plugin.gameManager.players.get(uuid).getTeam().equals("blue")){
            int spawnPointAmount = plugin.gameManager.blueSpawnPoints.size();
            spawnPointAmount++;
            int randomNumber = new Random().nextInt(spawnPointAmount);
            player.teleport(plugin.gameManager.blueSpawnPoints.get(randomNumber));
            plugin.gameManager.players.get(uuid).setAlive(true);
        }
    }
}
