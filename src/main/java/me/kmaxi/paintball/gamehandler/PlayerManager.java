package me.kmaxi.paintball.gamehandler;

import java.util.UUID;

public class PlayerManager {
    private UUID uuid;
    private int kills;
    private int deaths;
    private int flagCaptures;
    private int killstreak;
    private Boolean isInGame;



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
}
