package me.kmaxi.paintball;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballMain extends JavaPlugin {

    @Override
    public void onEnable(){
        Bukkit.broadcastMessage(ChatColor.WHITE + "Paintball CTF" + ChatColor.GREEN + " has been enabled");
    }

    @Override
    public void onDisable(){

    }
}
