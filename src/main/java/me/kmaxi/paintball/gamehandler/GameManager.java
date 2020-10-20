package me.kmaxi.paintball.gamehandler;

import me.kmaxi.paintball.PaintballMain;
import me.kmaxi.paintball.utils.InventoryManagment;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameManager {
    private final PaintballMain plugin;
    private boolean isStarting;
    public boolean isInGame;
    public HashMap<String, Flag> flags;
    public HashMap<UUID, PlayerManager> players;
    public Location jail;
    public ArrayList<Location> redSpawnPoints;
    public ArrayList<Location> blueSpawnPoints;
    public GameFunctions gameFunctions;


    public GameManager(PaintballMain plugin){
        this.plugin = plugin;
        this.isStarting = false;
        this.isInGame = false;
        this.players = new HashMap<>();
        this.redSpawnPoints = new ArrayList<>();
        this.blueSpawnPoints = new ArrayList<>();
        this.gameFunctions = new GameFunctions(plugin);
        this.flags = new HashMap<>();
    }

    public void startGame(){ //Starts the game
        if (Bukkit.getOnlinePlayers().size() < 2){
            Bukkit.broadcastMessage(ChatColor.RED + "At least two players are needed");
            return;
        }
        if (this.checkIfConfigsAreThere()){
            Bukkit.broadcastMessage(ChatColor.RED + "Missing config file(s)");
            return;
        }
        this.initializeLocations();
        this.isStarting = true;
        for (Player player : Bukkit.getOnlinePlayers()) { //Loop for every player that is online
            UUID playerUUID = player.getUniqueId();
            if (players.keySet().contains(playerUUID)) { //If players HashMap already contains the player
                players.get(playerUUID).reset();
                continue;
            }
            players.put(playerUUID, new PlayerManager(player));
            InventoryManagment.clear(player);
        }
        new BukkitRunnable() { //Countdown for game start
            int time = 10;

            @Override
            public void run() {
                if (time > 0){
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "The game starts in " + time + " seconds");
                    Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 2);
                    });
                    time--;
                } else {
                    startGameStepTwo();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void startGameStepTwo(){
        gameFunctions.assignTeams();
        gameFunctions.placeFlags();
        players.keySet().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            player.getInventory().setItem(0, new ItemStack(Material.SNOWBALL, 64));
        });
        this.isStarting = false;
        this.isInGame = true;



    }

    public void endGame(){

    }

    private Boolean checkIfConfigsAreThere(){
        if (plugin.getConfig().contains("paintball.red.spawnPointAmount")
                && plugin.getConfig().contains("paintball.blue.spawnPointAmount")
                && plugin.getConfig().contains("paintball.jail")
                && plugin.getConfig().contains("paintball.red.flagposition")
                && plugin.getConfig().contains("paintball.blue.flagposition")){
            return false;
        }
        return true;
    }

    private void initializeLocations(){ //Initializes all locations in this class to the ones that stand in the config
        this.jail = (Location) plugin.getConfig().get("paintball.jail");
        this.flags.put("red", new Flag(plugin, (Location) plugin.getConfig().get("paintball.red.flagposition"), Color.RED));
        this.flags.put("blue", new Flag(plugin, (Location) plugin.getConfig().get("paintball.blue.flagposition"), Color.BLUE));
        this.redSpawnPoints.clear();
        this.blueSpawnPoints.clear();

        int spawnPointAmount = (int) plugin.getConfig().get("paintball.red.spawnPointAmount");
        for (int i = 1; i <= spawnPointAmount; i++){
            redSpawnPoints.add((Location) plugin.getConfig().get("paintball.red.spawnPoints." + i));
        }
        spawnPointAmount = (int) plugin.getConfig().get("paintball.blue.spawnPointAmount");
        for (int i = 1; i <= spawnPointAmount; i++){
            blueSpawnPoints.add((Location) plugin.getConfig().get("paintball.blue.spawnPoints." + i));
        }
    }


    }




