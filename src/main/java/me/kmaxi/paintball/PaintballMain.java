package me.kmaxi.paintball;

import me.kmaxi.paintball.commands.Commands;
import me.kmaxi.paintball.gamehandler.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintballMain extends JavaPlugin {
    public Commands commands;
    public GameManager gameManager;

    @Override
    public void onEnable(){
        Bukkit.broadcastMessage(ChatColor.WHITE + "Paintball CTF" + ChatColor.GREEN + " has been enabled");
        this.instanceClasses();

    }

    @Override
    public void onDisable(){

    }

    private void instanceClasses(){
        this.gameManager = new GameManager(this);
        this.commands = new Commands(this);
        commands.registerCommands();
    }
}
