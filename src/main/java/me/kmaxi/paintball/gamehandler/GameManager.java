package me.kmaxi.paintball.gamehandler;

import me.kmaxi.paintball.PaintballMain;
import me.kmaxi.paintball.utils.InventoryManagment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GameManager {
    private final PaintballMain plugin;
    private boolean isStarting;
    public boolean isInGame;
    public HashMap<UUID, PlayerManager> players;

    public GameManager(PaintballMain plugin){
        this.plugin = plugin;
        this.isStarting = false;
        this.isInGame = false;
    }

    public void startGame(){ //Starts the game
        this.isStarting = true;
        for (Player player : Bukkit.getOnlinePlayers()) { //Loop for every player that is online
            UUID playerUUID = player.getUniqueId();
            if (players.keySet().contains(playerUUID)) { //If players HashMap already contains the player
                players.get(playerUUID).reset();
                continue;
            }
            players.put(playerUUID, new PlayerManager(playerUUID));
            InventoryManagment.clear(player);
        }

    }

    public void endGame(){

    }


}
