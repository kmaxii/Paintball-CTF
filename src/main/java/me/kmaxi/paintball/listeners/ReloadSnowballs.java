package me.kmaxi.paintball.listeners;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
        PlayerInventory inv = player.getInventory();
        if (inv.contains(new ItemStack(Material.SNOWBALL))){
            return;
        }
        plugin.gameManager.gameFunctions.addSnowballs(player);
    }

    @EventHandler
    public void reload(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!plugin.gameManager.isInGame || !plugin.gameManager.players.keySet().contains(player.getUniqueId())){
            return;
        }
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            return;
        }
        if (!event.getMaterial().equals(Material.SNOWBALL)){
            return;
        }
        plugin.gameManager.gameFunctions.addSnowballs(player);
    }
}
