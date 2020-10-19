package me.kmaxi.paintball.utils;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.UUID;

public class InventoryManagment {
    private final PaintballMain plugin;

    public InventoryManagment(PaintballMain plugin) {
        this.plugin = plugin;
    }

    public static void clear(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.setHelmet(null);
        inv.setChestplate(null);
        inv.setLeggings(null);
        inv.setBoots(null);
        inv.clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setWalkSpeed(0.2f);
    }

    public void setArmor(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        PlayerInventory inv = player.getInventory();
        if (plugin.gameManager.players.get(uuid).getTeam().equals("blue")) {
            this.setArmor(inv, Color.BLUE);
        }
        if (plugin.gameManager.players.get(uuid).getTeam().equals("red")) {
            this.setArmor(inv, Color.RED);
        }
    }




    private void setArmor(PlayerInventory inv, Color color) {
        inv.setBoots(this.setArmor(new ItemStack(Material.LEATHER_BOOTS), color));
        inv.setLeggings(this.setArmor(new ItemStack(Material.LEATHER_LEGGINGS), color));
        inv.setChestplate(this.setArmor(new ItemStack(Material.LEATHER_CHESTPLATE), color));
    }
    private static ItemStack setArmor(ItemStack itemStack, Color color){
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(color);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}

