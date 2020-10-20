package me.kmaxi.paintball.listeners;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final PaintballMain plugin;

    public PlayerJoin(PaintballMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(!plugin.gameManager.isInGame){
            return;
        }
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
        event.getPlayer().teleport(plugin.gameManager.jail);
    }
}
