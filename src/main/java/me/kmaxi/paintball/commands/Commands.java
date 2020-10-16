package me.kmaxi.paintball.commands;

import me.kmaxi.paintball.PaintballMain;

public class Commands {

    private PaintballMain plugin;

    public String cmd1 = "start"; //Command to start the game
    public String cmd2 = "flag"; //Command to set a position of a flag.
    public String cmd3 = "spawnpoint"; //Command to set a spawn point
    public String cmd4 = "jail"; //set jail for when people are dead
    public String cmd5 = "end"; //ends the game
    public String cmd6 = "reset"; //resets the config

    public Commands(PaintballMain plugin){ this.plugin = plugin; }

    public void registerCommands(){
        plugin.getCommand("paintball").setExecutor(new CommandsManager(plugin));
    }
}
