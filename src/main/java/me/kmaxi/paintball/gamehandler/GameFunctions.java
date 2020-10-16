package me.kmaxi.paintball.gamehandler;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class GameFunctions {
    private final PaintballMain plugin;


    public GameFunctions(PaintballMain plugin) {
        this.plugin = plugin;
    }

    public void assignTeams(){
        ArrayList<UUID> allPlayers = new ArrayList<>();
        plugin.gameManager.players.keySet().forEach(uuid -> {
            allPlayers.add(uuid);
        });
        Collections.shuffle(allPlayers);
        int i = 1;
        for (UUID uuid : allPlayers) {
            if (i % 2 == 0) {
                plugin.gameManager.players.get(uuid).setTeam("red");
            } else {
                plugin.gameManager.players.get(uuid).setTeam("blue");
            }
            i++;
            plugin.inventoryManagment.setArmor(uuid); //Sets the armor for the player
        }
    }
    public void placeFlags(){
        if (!plugin.gameManager.redFlagTaken){
            Location redFlagLocation = plugin.gameManager.redFlagLocation;
            redFlagLocation.getWorld().getBlockAt(redFlagLocation).setType(Material.RED_BANNER);
        }
        if (!plugin.gameManager.blueFlagTaken){
            Location blueFlagLocation = plugin.gameManager.blueFlagLocation;
            blueFlagLocation.getWorld().getBlockAt(blueFlagLocation).setType(Material.BLUE_BANNER);
        }
    }

    public void addSnowballs(Player player){
        new BukkitRunnable(){
            int time = 5;
            @Override
            public void run() {
                if (time > 0){
                    player.sendMessage(ChatColor.YELLOW + "" + time + " seconds remaining");
                    player.playSound(player.getLocation(), Sound.BLOCK_SNOW_BREAK, 2, 2);
                    time--;
                } else {
                    PlayerInventory inv = player.getInventory();
                    inv.setItem(0, new ItemStack(Material.SNOWBALL, 64));
                    cancel();
                }

            }
        }.runTaskTimer(plugin, 0, 20);
    }




}
