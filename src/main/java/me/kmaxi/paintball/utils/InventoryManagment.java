package me.kmaxi.paintball.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class InventoryManagment {

    public static void clear(Player player){
        PlayerInventory inv = player.getInventory();
        inv.setHelmet(null);
        inv.setChestplate(null);
        inv.setLeggings(null);
        inv.setBoots(null);
        inv.clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setWalkSpeed(0.2f);
    }
}
