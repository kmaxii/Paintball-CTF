package me.kmaxi.paintball.gamehandler;

import com.sun.org.apache.xpath.internal.operations.Bool;

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



    public PlayerManager(UUID uuid) {
        this.uuid = uuid;
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
    }

    public void flagCapture(){
        this.flagCaptures++;
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
}

