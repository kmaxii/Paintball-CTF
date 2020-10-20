package me.kmaxi.paintball.listeners;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class TakeDamage implements Listener {
    private final PaintballMain plugin;

    public TakeDamage(PaintballMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (!plugin.gameManager.isInGame){
            return;
        }
        if (event.getEntity() instanceof Player){
            return;
        }
        Player player = (Player) event.getEntity();
        if (!plugin.gameManager.players.keySet().contains(player.getUniqueId())){
            return;
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            event.setCancelled(true);
        }
        event.setDamage(0);

    }
}
