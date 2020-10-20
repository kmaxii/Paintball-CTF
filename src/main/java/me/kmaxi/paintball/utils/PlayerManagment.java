package me.kmaxi.paintball.utils;

import me.kmaxi.paintball.PaintballMain;
import me.kmaxi.paintball.gamehandler.Flag;
import me.kmaxi.paintball.gamehandler.PlayerManager;
import org.bukkit.*;
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
        if (playerManager.getHasFlag()){
            dropFlag(playerManager);
            player.getInventory().setHelmet(null);
        }
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

    }

    public void dropFlag(PlayerManager playerManager){
        Player player = playerManager.getPlayer();
        Location location = player.getLocation();
        String playerTeam = playerManager.getTeam();
        String flagTeam = null;
        if (playerTeam.equals("red")){
            flagTeam = "blue";
        }
        if (playerTeam.equals("blue")){
            flagTeam = "red";
        }
        location.getWorld().getBlockAt(location).setType(Material.RED_BANNER);
        plugin.gameManager.flags.get(flagTeam).setTakenLocation(location);
        Bukkit.broadcastMessage(player.getName() + " has dropped the " + flagTeam + "flag");
        plugin.gameManager.flags.get(flagTeam).setTaken(false);
        plugin.gameManager.flags.get(flagTeam).setDropped(true);
        String finalFlagTeam = flagTeam;
        new BukkitRunnable(){
            int timeCheck = 300;

            @Override
            public void run() {
                timeCheck--;
                if(!plugin.gameManager.flags.get(finalFlagTeam).getDropped()){
                    cancel();
                }
                if (timeCheck == 0){
                    Flag flag = plugin.gameManager.flags.get(finalFlagTeam);
                    flag.getLocation().getWorld().getBlockAt(flag.getLocation()).setType(Material.AIR);
                    flag.setLocation(flag.getBaseLocation());
                    flag.setTaken(false);
                    flag.setDropped(false);
                    plugin.gameManager.gameFunctions.placeFlags();
                    Bukkit.broadcastMessage(ChatColor.WHITE + finalFlagTeam + " flag was returned because no one has picked it up");

                    plugin.gameManager.players.values().forEach(playerManager1 -> {
                        if (playerManager1.getTeam().equals(plugin.gameManager.players.get(player.getUniqueId()).getTeam())){
                            playerManager1.getPlayer().playSound(playerManager1.getPlayer().getLocation(), Sound.ENTITY_GUARDIAN_DEATH_LAND, 1, 1);
                        }
                        else {
                            playerManager1.getPlayer().playSound(playerManager1.getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1);
                        }
                    });
                }


            }
        }.runTaskTimer(plugin, 0, 2);

    }





}
