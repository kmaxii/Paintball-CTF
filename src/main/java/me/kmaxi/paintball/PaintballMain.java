package me.kmaxi.paintball;

import me.kmaxi.paintball.commands.Commands;
import me.kmaxi.paintball.gamehandler.GameManager;
import me.kmaxi.paintball.utils.InventoryManagment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballMain extends JavaPlugin {
    public Commands commands;
    public GameManager gameManager;
    public InventoryManagment inventoryManagment;

    @Override
    public void onEnable(){
        Bukkit.broadcastMessage(ChatColor.WHITE + "Paintball CTF" + ChatColor.GREEN + " has been enabled");
        this.instanceClasses();
        this.loadConfig();

    }

    @Override
    public void onDisable(){
        this.saveConfig();

    }

    private void instanceClasses(){
        this.gameManager = new GameManager(this);
        this.commands = new Commands(this);
        this.inventoryManagment = new InventoryManagment(this);
        commands.registerCommands();
    }

    private void loadConfig(){
        this.saveConfig();
        saveConfig();
    }
}
