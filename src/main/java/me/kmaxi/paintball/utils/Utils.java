package me.kmaxi.paintball.utils;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

public class Utils {

    public static void setBanner(DyeColor color, Location location){
        location.getBlock().setType(Material.STANDING_BANNER);

        Banner banner = (Banner) location.getBlock().getState();
        banner.setBaseColor(color);
        banner.update();
    }

    public static ItemStack getBanner(DyeColor color){
        ItemStack itemStack = new ItemStack(Material.BANNER);
        BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
        meta.setBaseColor(color);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
