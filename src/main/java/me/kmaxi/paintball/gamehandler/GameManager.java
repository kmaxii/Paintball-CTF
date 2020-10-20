package me.kmaxi.paintball.gamehandler;

import me.kmaxi.paintball.PaintballMain;
import me.kmaxi.paintball.utils.InventoryManagment;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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
        isInGame = false;

        ArrayList<String> winning = winningTeam();
        if (winning.size() == 1){
            if (winning.get(0) == null){
                announceDraw();
                resetPlayers();
                return;
            }
            annouceWinnerByCaptures(winning.get(0));
            return;
        }
        announceWinnerByKills(winning.get(0), winning.get(1), winning.get(2));


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

    private ArrayList<String> winningTeam(){
        ArrayList<String> winningTeam= new ArrayList<>();
        if (plugin.gameManager.flags.get("red").getCaptures() > plugin.gameManager.flags.get("blue").getCaptures()){
            winningTeam.add("blue");
            return winningTeam;
        }
        if (plugin.gameManager.flags.get("red").getCaptures() < plugin.gameManager.flags.get("blue").getCaptures()){
            winningTeam.add("red");
            return winningTeam;
        }
        AtomicInteger redKills = new AtomicInteger();
        AtomicInteger blueKills = new AtomicInteger();
        plugin.gameManager.players.values().forEach(playerManager -> {
            if (playerManager.getTeam().equals("red")){
                redKills.addAndGet(playerManager.getKills());
            }
            if (playerManager.getTeam().equals("blue")){
                blueKills.addAndGet(playerManager.getKills());
            }
        });
        int redKillsFinal = redKills.intValue();
        int blueKillsFinal = blueKills.intValue();
        if (redKillsFinal > blueKillsFinal){
            winningTeam.add("red");
            winningTeam.add(redKillsFinal + "");
            winningTeam.add(blueKillsFinal + "");
        }
        if (blueKillsFinal > redKillsFinal){
            winningTeam.add("blue");
            winningTeam.add(blueKillsFinal + "");
            winningTeam.add(redKillsFinal + "");
        }
        if(blueKillsFinal == redKillsFinal){
            winningTeam.add(null);
        }
        return winningTeam;
    }



    private void resetPlayers(){
        players.values().forEach(playerManager -> {
            PlayerInventory inv = playerManager.getPlayer().getInventory();
            inv.setHelmet(null);
            inv.setHelmet(null);
            inv.setLeggings(null);
            inv.setBoots(null);
            inv.clear();
            playerManager.getPlayer().teleport(jail);
        });
    }

    private void announceDraw(){
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_BREAK, 1, 1);
            player.sendTitle("It's a draw", "");
        });
    }
    private void annouceWinnerByCaptures(String winner){
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1, 1);
            player.sendTitle(winner + " has won the game", "by capturing the flag");
        });
    }
    private void announceWinnerByKills(String winner, String winningTeamKills, String losingTeamKills){
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
            player.sendTitle(winner + " has won the game", "getting the most kills");
        });
    }


    }




