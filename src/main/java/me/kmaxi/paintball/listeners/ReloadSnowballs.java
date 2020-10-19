package me.kmaxi.paintball.listeners;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadSnowballs implements Listener {
    private final PaintballMain plugin;

    public ReloadSnowballs(PaintballMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void noSnowballsRemaning(ProjectileLaunchEvent event){
        if (!plugin.gameManager.isInGame
                || !(event.getEntity().getShooter() instanceof Player)
                || !event.getEntity().getType().equals(EntityType.SNOWBALL)) {
            return;
        }
        Player player = (Player) event.getEntity().getShooter();
        if (!plugin.gameManager.players.containsKey(player.getUniqueId())){
            return;
        }
        new BukkitRunnable(){

            @Override
            public void run() {
                PlayerInventory inv = player.getInventory();
                if (inv.contains(Material.SNOWBALL)){
                    Bukkit.broadcastMessage("Contains snow");
                    return;
                }
                plugin.gameManager.gameFunctions.addSnowballs(player);
            }
        }.runTaskLater(plugin, 1);

    }

    @EventHandler
    public void reload(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!plugin.gameManager.isInGame || !plugin.gameManager.players.keySet().contains(player.getUniqueId())){
            return;
        }
        if (event.getItem().getType() != Material.SNOWBALL){
            return;
        }
        if(event.getAction() == Action.RIGHT_CLICK_AIR|| event.getAction() == Action.RIGHT_CLICK_BLOCK){
            plugin.gameManager.players.get(player.getUniqueId()).setHasThrownSnowball(true);
            return;
        }
        if (plugin.gameManager.players.get(player.getUniqueId()).getHasThrownSnowball()){
            plugin.gameManager.players.get(player.getUniqueId()).setHasThrownSnowball(false);
            return;
        }

        Bukkit.broadcastMessage("reloading");
        plugin.gameManager.gameFunctions.addSnowballs(player);






    }
}
