package me.kmaxi.paintball.listeners;

import me.kmaxi.paintball.PaintballMain;
import me.kmaxi.paintball.gamehandler.PlayerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TakeFlag implements Listener {
    private final PaintballMain plugin;

    public TakeFlag(PaintballMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void stepOnFlagBlock(PlayerMoveEvent event){
        if (!plugin.gameManager.isInGame){
            return;
        }
        Player player = event.getPlayer();
        if (!plugin.gameManager.players.containsKey(player.getUniqueId())){
            return;
        }
        PlayerManager playerManager = plugin.gameManager.players.get(player.getUniqueId());
        if(!playerManager.isAlive()){
            return;
        }
        Location location = player.getLocation();
        if (location.distance(plugin.gameManager.flags.get("red").getLocation()) <= 1.5){
            if (!playerManager.getTeam().equals("blue")){
                return;
            }
            if (plugin.gameManager.flags.get("red").isTaken()){
                return;
            }
            plugin.gameManager.flags.get("red").setTaken(true);
            plugin.playerManagment.gotFlag(player);
            return;

        }
        if (location.distance(plugin.gameManager.flags.get("blue").getLocation()) <= 1.5){
            if (!playerManager.getTeam().equals("red")){
                return;
            }
            if (plugin.gameManager.flags.get("blue").isTaken()){
                return;
            }
            plugin.gameManager.flags.get("blue").setTaken(true);
            plugin.playerManagment.gotFlag(player);
        }



    }
}
