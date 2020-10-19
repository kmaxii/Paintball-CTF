package me.kmaxi.paintball.listeners;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SnowballHit implements Listener {

    private final PaintballMain plugin;

    public SnowballHit(PaintballMain plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onHit(ProjectileHitEvent event){
        if (!plugin.gameManager.isInGame
                || !event.getEntity().getType().equals(EntityType.SNOWBALL)
                || !(event.getEntity().getShooter() instanceof Player)
                || !(event.getHitEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity().getShooter();
        if (!plugin.gameManager.players.containsKey(player.getUniqueId())){
            return;
        }
        if (!plugin.gameManager.players.get(player.getUniqueId()).isAlive()){
            return;
        }
        Player hit = (Player) event.getHitEntity();
        String throwerTeam = plugin.gameManager.players.get(player.getUniqueId()).getTeam();
        String hitTeam = plugin.gameManager.players.get(hit.getUniqueId()).getTeam();
        if (throwerTeam.equals(hitTeam)){
            return;
        }
        if (!plugin.gameManager.players.get(hit.getUniqueId()).isAlive()){
            return;
        }

        plugin.playerManagment.die(hit);
        plugin.playerManagment.kill(player);
        Bukkit.broadcastMessage(ChatColor.GRAY + player.getDisplayName() + " shoot " + ChatColor.GRAY + hit.getDisplayName());
    }
}
