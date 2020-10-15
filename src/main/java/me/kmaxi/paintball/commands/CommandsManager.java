package me.kmaxi.paintball.commands;

import me.kmaxi.paintball.PaintballMain;

public class CommandsManager {

    private PaintballMain plugin;

    public String cmd1 = "start"; //Command to start the game
    public String cmd2 = "flag"; //Command to set a flag.
    public String cmd3 = "spawnpoint"; //Command to set a spawn point
    public String cmd4 = "jail"; //set jail for when people are dead

    public CommandsManager(PaintballMain plugin){ this.plugin = plugin; }
}
