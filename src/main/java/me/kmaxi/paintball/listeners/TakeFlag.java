package me.kmaxi.paintball.listeners;

import me.kmaxi.paintball.PaintballMain;
import me.kmaxi.paintball.gamehandler.Flag;
import me.kmaxi.paintball.gamehandler.PlayerManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

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

        pickUpFlag(player, "red");
        pickUpFlag(player, "blue");

    }


    public void returnFlag(Player player){
        Flag flag = plugin.gameManager.flags.get(plugin.gameManager.players.get(player.getUniqueId()).getTeam());
        flag.getLocation().getWorld().getBlockAt(flag.getLocation()).setType(Material.AIR);
        flag.setTaken(false);
        flag.setDropped(false);
        plugin.gameManager.gameFunctions.placeFlags();
        flag.resetLocation();

        plugin.gameManager.players.values().forEach(playerManager1 -> {
            if (playerManager1.getTeam().equals(plugin.gameManager.players.get(player.getUniqueId()).getTeam())){
                playerManager1.getPlayer().playSound(playerManager1.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
            }
            else {
                playerManager1.getPlayer().playSound(playerManager1.getPlayer().getLocation(), Sound.ANVIL_BREAK, 1, 1);
            }
        });

    }

    public void pickUpFlag(Player player, String teamName){
        Location location = player.getLocation();
        PlayerManager playerManager = plugin.gameManager.players.get(player.getUniqueId());
        if (location.distance(plugin.gameManager.flags.get(teamName).getLocation()) <= 1.5){

            if (plugin.gameManager.flags.get(teamName).getDropped()){
                if (playerManager.getTeam().equals(teamName)){
                    Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has returned the " + teamName + " flag");
                    returnFlag(player);
                    return;
                }
            }
            if (plugin.gameManager.flags.get(teamName).isTaken()){
                return;
            }
            if(playerManager.getTeam().equals(teamName)){
                if(playerManager.getHasFlag()){
                    captureFlag(playerManager);
                }

                return;
            }
            plugin.gameManager.flags.get(teamName).setTaken(true);
            plugin.playerManagment.gotFlag(player);
            Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has picked up the " + teamName + ChatColor.YELLOW + " flag");
            plugin.gameManager.flags.get(teamName).getLocation().getBlock().setType(Material.AIR);
            plugin.gameManager.players.values().forEach(playerManager1 -> {
                if (playerManager1.getTeam().equals(teamName)){
                    playerManager1.getPlayer().playSound(playerManager1.getPlayer().getLocation(), Sound.GHAST_CHARGE, 1, 1);
                }
                else {
                    playerManager1.getPlayer().playSound(playerManager1.getPlayer().getLocation(), Sound.WOLF_WHINE, 1, 1);
                }
            });
        }
    }

    public void captureFlag(PlayerManager playerManager){
        Player player = playerManager.getPlayer();
        playerManager.setHasFlag(false);
        String capturedFlagTeam = "";
        if (playerManager.getTeam().equals("red")){
            capturedFlagTeam = "blue";
        }
        if (playerManager.getTeam().equals("blue")){
            capturedFlagTeam = "red";
        }
        plugin.gameManager.flags.get(capturedFlagTeam).capture();
        player.getInventory().setHelmet(null);
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has captured the " + capturedFlagTeam + " flag");
        String finalCapturedFlagTeam1 = capturedFlagTeam;
        plugin.gameManager.players.values().forEach(playerManager1 -> {
            if (playerManager1.getTeam().equals(finalCapturedFlagTeam1)){
                playerManager1.getPlayer().playSound(playerManager1.getPlayer().getLocation(), Sound.GHAST_SCREAM, 1, 1);
            }
            else {
                playerManager1.getPlayer().playSound(playerManager1.getPlayer().getLocation(), Sound.WOLF_HOWL, 1, 1);
            }
        });
        if (plugin.gameManager.flags.get(capturedFlagTeam).getCaptures() == 3){
            plugin.gameManager.endGame();
            return;
        }
        String finalCapturedFlagTeam = capturedFlagTeam;
        new BukkitRunnable(){

            @Override
            public void run() {
                plugin.gameManager.flags.get(finalCapturedFlagTeam).setTaken(false);
                plugin.gameManager.gameFunctions.placeFlags();

            }
        }.runTaskLater(plugin, 100);




    }
}
