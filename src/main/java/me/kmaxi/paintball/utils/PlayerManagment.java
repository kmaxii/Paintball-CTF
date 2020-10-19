package me.kmaxi.paintball.utils;

import me.kmaxi.paintball.PaintballMain;
import me.kmaxi.paintball.gamehandler.PlayerManager;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.UUID;

public class PlayerManagment {
    private final PaintballMain plugin;

    public PlayerManagment(PaintballMain plugin) {
        this.plugin = plugin;
    }

    public void teleportToSpawnPoint(UUID uuid){
        Player player = plugin.gameManager.players.get(uuid).getPlayer();
        if (plugin.gameManager.players.get(uuid).getTeam().equals("red")){
            int spawnPointAmount = plugin.gameManager.redSpawnPoints.size();
            int randomNumber = new Random().nextInt(spawnPointAmount);
            Location location = plugin.gameManager.redSpawnPoints.get(randomNumber);
            player.teleport(location);
            plugin.gameManager.players.get(uuid).setAlive(true);
            return;
        }
        if (plugin.gameManager.players.get(uuid).getTeam().equals("blue")){
            int spawnPointAmount = plugin.gameManager.blueSpawnPoints.size();
            int randomNumber = new Random().nextInt(spawnPointAmount);
            Location location = plugin.gameManager.blueSpawnPoints.get(randomNumber);
            player.teleport(location);
            plugin.gameManager.players.get(uuid).setAlive(true);
        }
    }

    public void die(Player player){
        Location deathLocation = player.getLocation();
        PlayerManager playerManager = plugin.gameManager.players.get(player.getUniqueId());
        playerManager.die();
        FireworkEffect fireworkEffect;
        if (playerManager.getTeam().equals("red")){
            fireworkEffect = FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BALL).withColor(Color.RED).withFade(Color.RED).build();
        }
        else {
            fireworkEffect = FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BALL).withColor(Color.BLUE).withFade(Color.BLUE).build();
        }
        new InstantFirework(fireworkEffect, deathLocation);
        deathLocation.getWorld().playSound(deathLocation, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 10, 2);
        player.teleport(plugin.gameManager.jail);
        new BukkitRunnable(){
            int time = 10;
            @Override
            public void run() {
                if (time == 0){
                    teleportToSpawnPoint(player.getUniqueId());
                    playerManager.setAlive(true);
                    cancel();
                }
                player.sendMessage(ChatColor.GOLD + "" + time + " seconds to respawn");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
                time--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void kill(Player player){
        plugin.gameManager.players.get(player.getUniqueId()).kill();
    }

    public void gotFlag(Player player){
        PlayerManager playerManager = plugin.gameManager.players.get(player.getUniqueId());
        playerManager.setHasFlag(true);
        PlayerInventory inv = player.getInventory();
        if (playerManager.getTeam().equals("red")){
            inv.setHelmet(new ItemStack(Material.BLUE_BANNER));
        }
        if (playerManager.getTeam().equals("blue")){
            inv.setHelmet(new ItemStack(Material.RED_BANNER));
        }
        new BukkitRunnable(){

            @Override
            public void run() {
                if (!playerManager.getHasFlag() || !plugin.gameManager.isInGame){
                    inv.setHelmet(null);
                    playerManager.setHasFlag(false);
                    plugin.gameManager.flags.get(playerManager.getTeam()).capture();
                    if(plugin.gameManager.flags.get(playerManager.getTeam()).getCaptures() == 3){
                        plugin.gameManager.endGame();
                    }
                    cancel();
                }
                Location check = player.getLocation();
                check.setY(check.getY() - 1);
                if (check.getBlock().getType().equals(Material.LAPIS_BLOCK)){
                    if (playerManager.getTeam().equals("blue") && !plugin.gameManager.flags.get("red").isTaken()){
                        playerManager.flagCapture();
                    }
                }
                if (check.getBlock().getType().equals(Material.REDSTONE_BLOCK)){
                    if (playerManager.getTeam().equals("red") && !plugin.gameManager.flags.get("red").isTaken()){
                        playerManager.flagCapture();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 2);

    }





}
