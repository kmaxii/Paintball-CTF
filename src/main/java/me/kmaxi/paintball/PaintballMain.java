package me.kmaxi.paintball;

import me.kmaxi.paintball.commands.Commands;
import me.kmaxi.paintball.gamehandler.GameManager;
import me.kmaxi.paintball.listeners.ReloadSnowballs;
import me.kmaxi.paintball.listeners.SnowballHit;
import me.kmaxi.paintball.listeners.TakeFlag;
import me.kmaxi.paintball.utils.InventoryManagment;
import me.kmaxi.paintball.utils.PlayerManagment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballMain extends JavaPlugin {
    public Commands commands;
    public GameManager gameManager;
    public InventoryManagment inventoryManagment;
    public PlayerManagment playerManagment;

    @Override
    public void onEnable(){
        Bukkit.broadcastMessage(ChatColor.WHITE + "Paintball CTF" + ChatColor.GREEN + " has been enabled");
        this.instanceClasses();
        this.loadConfig();
        this.registerEvents();

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
        this.playerManagment = new PlayerManagment(this);
    }

    private void loadConfig(){
        this.saveConfig();
        saveConfig();
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new ReloadSnowballs(this), this);
        Bukkit.getPluginManager().registerEvents(new SnowballHit(this), this);
        Bukkit.getPluginManager().registerEvents(new TakeFlag(this), this);

    }
}
