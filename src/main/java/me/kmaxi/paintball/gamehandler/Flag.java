package me.kmaxi.paintball.gamehandler;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Flag {
    private final PaintballMain plugin;
    private Location baselocation;
    private Boolean isTaken;
    private Boolean isCaptured;
    private Boolean isDropped;
    private int captures;
    private Location takenLocation;
    private Color teamColor;


    public Flag(PaintballMain plugin, Location location, Color teamColor) {
        this.plugin = plugin;
        this.isCaptured = false;
        this.isTaken = false;
        this.captures = 0;
        this.baselocation = location;
        this.takenLocation = location;
        this.teamColor = teamColor;
        this.isDropped = false;

    }

    public void setLocation(Location location){
        this.takenLocation = location;
    }

    public void setTaken(Boolean taken) {
        if (taken){
            Block block = baselocation.getWorld().getBlockAt(baselocation);
            if (!(block.getType() == Material.RED_BANNER) || !(block.getType() == Material.BLUE_BANNER)){
                block.setType(Material.AIR);
            }
        }


        isTaken = taken;
    }


    public Location getLocation() {
        return this.takenLocation;
    }

    public Boolean isTaken() {
        return isTaken;
    }



    public void capture(){
        this.captures++;
        this.takenLocation = baselocation;
    }

    public int getCaptures() {
        return captures;
    }

    public void setTakenLocation(Location takenLocation) {
        this.takenLocation = takenLocation;
    }

    public Location getBaseLocation() {
        return baselocation;
    }


    public void setDropped(Boolean dropped) {
        isDropped = dropped;
    }

    public Boolean getDropped() {
        return isDropped;
    }
}
