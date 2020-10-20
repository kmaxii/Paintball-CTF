package me.kmaxi.paintball.gamehandler;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerManager {
    private UUID uuid;
    private int kills;
    private int deaths;
    private int flagCaptures;
    private int killstreak;
    private Boolean isAlive;
    private Boolean isInGame;
    private String team;
    private Boolean hasFlag;
    private Player player;
    private Boolean hasThrownSnowball;



    public PlayerManager(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.reset();
    }

    public void reset(){
        this.setKills(0);
        this.setDeaths(0);
        this.flagCaptures = 0;
        this.killstreak = 0;
        this.setInGame(false);
        this.team = " ";
        this.isAlive = false;
        this.setHasThrownSnowball(false);
        this.hasFlag = false;

    }


    public Boolean isInGame() {
        return isInGame;
    }

    public void setInGame(Boolean inGame) {
        isInGame = inGame;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths){
        this.deaths = deaths;
    }

    public void kill(){
        this.kills++;
        this.killstreak++;
    }

    public void die(){
        this.deaths++;
        this.killstreak = 0;
        this.setAlive(false);
        this.setHasFlag(false);
    }

    public void flagCapture(){
        this.flagCaptures++;
        this.setHasFlag(false);
    }

    public void setTeam(String team){
        this.team = team;
    }

    public String getTeam(){
        return this.team;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    public Boolean isAlive(){
        return isAlive;
    }

    public void setHasFlag(Boolean hasFlag) {
        this.hasFlag = hasFlag;
    }
    public Boolean getHasFlag() {
        return hasFlag;
    }

    public Player getPlayer() {
        return player;
    }

    public void setHasThrownSnowball(Boolean hasThrownSnowball) {
        this.hasThrownSnowball = hasThrownSnowball;
    }

    public Boolean getHasThrownSnowball() {
        return hasThrownSnowball;
    }
}

