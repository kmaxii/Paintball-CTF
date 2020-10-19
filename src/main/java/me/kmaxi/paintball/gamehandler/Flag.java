package me.kmaxi.paintball.gamehandler;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Flag {
    private final PaintballMain plugin;
    private Location location;
    private Boolean isTaken;
    private Boolean isCaptured;
    private int captures;


    public Flag(PaintballMain plugin, Location location) {
        this.plugin = plugin;
        this.isCaptured = false;
        this.isTaken = false;
        this.captures = 0;
        this.location = location;

    }

    public void setLocation(Location location){
        this.location = location;
    }

    public void setTaken(Boolean taken) {
        if (taken){
            Block block = location.getWorld().getBlockAt(location);
            if (!(block.getType() == Material.RED_BANNER) || !(block.getType() == Material.BLUE_BANNER)){
                block.setType(Material.AIR);

            }
        }


        isTaken = taken;
    }

    public void setCaptured(Boolean captured) {
        isCaptured = captured;
    }

    public Location getLocation() {
        return location;
    }

    public Boolean isTaken() {
        return isTaken;
    }

    public Boolean isCaptured() {
        return isCaptured;
    }

    public void capture(){
        this.captures++;
    }

    public int getCaptures() {
        return captures;
    }
}
