package me.kmaxi.paintball.commands;

import me.kmaxi.paintball.PaintballMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsManager implements CommandExecutor {
    private PaintballMain plugin;

    public CommandsManager(PaintballMain plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (args.length == 0){ //If the command entered is just /paintball without any args
            this.commandHelp(sender); //sends information off all available commands
            return true;
        }
        Player player = null;
        if (sender instanceof Player){
            player = ((Player) sender).getPlayer();
        }
        if (args.length == 1){ //If just one argument was added
            if (args[0].equalsIgnoreCase(plugin.commands.cmd1)){ //if the argument equals to the string that STARTS a game
                plugin.gameManager.startGame();
                return true;
            }
            if (args[0].equalsIgnoreCase(plugin.commands.cmd5)){ //if the argument equals to the string that ends a game
                plugin.gameManager.endGame();
                return true;
            }
            if(args[0].equalsIgnoreCase(plugin.commands.cmd6)){ //If it equals to the command that resets config
                this.clearConfig(sender);
                return true;
            }

            if (player == null){ //If sender is console then it stops them from using any future commands
                sender.sendMessage(ChatColor.RED + "Wrong command or this may only be executed by players");
                return true;
            }
            if (args[0].equalsIgnoreCase(plugin.commands.cmd4)){ //If the argument equals to the string that sets the position of the jail
                this.setJail(player);
                return true;
            }
            if (args[0].equalsIgnoreCase(plugin.commands.cmd2)
                    || args[0].equalsIgnoreCase(plugin.commands.cmd3)){ //If the command requires another argument
                this.missingArgument(sender);
                return true;
            }
        }
        if (args.length == 2){
            if (args[0].equalsIgnoreCase(plugin.commands.cmd2)){ //If the command equals to the one that sets a flag
                if (args[1].equalsIgnoreCase("red")){ //Set red flag location
                    this.setFlag(player, "red");
                    return true;
                }
                if (args[1].equalsIgnoreCase("blue")){ //Set blue flag location
                    this.setFlag(player, "blue");
                    return true;
                }
                this.missingArgument(sender);
                return true;
            }
            if (args[0].equalsIgnoreCase(plugin.commands.cmd3)){ //Command to set spawn point
                if (args[1].equalsIgnoreCase("red")){ //Adds a red spawn point
                    this.addSpawnPoint(player, "red");
                    return true;
                }
                if (args[1].equalsIgnoreCase("blue")){ //Adds a blue spawn point
                    this.addSpawnPoint(player, "blue");
                    return true;
                }
            }
        }


        return true;

    }

    private void commandHelp(CommandSender sender){ //sends information off all available commands
        sender.sendMessage(ChatColor.YELLOW + "----------" + ChatColor.WHITE + " Commands: " + ChatColor.YELLOW + "-------------------");
        sender.sendMessage(ChatColor.GOLD + "/paintball " + plugin.commands.cmd1 + ChatColor.WHITE + " starts the game");
        sender.sendMessage(ChatColor.GOLD + "/paintball " + plugin.commands.cmd5 + ChatColor.WHITE + " forces the game to end");
        sender.sendMessage(ChatColor.GOLD + "/paintball " + plugin.commands.cmd4 + ChatColor.WHITE + " sets the location of the jail");
        sender.sendMessage(ChatColor.GOLD + "/paintball " + plugin.commands.cmd3 + ChatColor.RED + " red" + ChatColor.BLUE + " blue" + ChatColor.WHITE + " adds a spawn point for the chosen team");
        sender.sendMessage(ChatColor.GOLD + "/paintball " + plugin.commands.cmd2 + ChatColor.RED + " red" + ChatColor.BLUE + " blue" + ChatColor.WHITE + ChatColor.WHITE + " sets the flags location");
    }

    private void missingArgument(CommandSender sender){
        sender.sendMessage(ChatColor.RED + "Missing argument.");
    }

    private void setJail(Player player){
        plugin.getConfig().set("paintball.jail", player.getLocation());
        plugin.saveConfig();
        player.sendMessage(ChatColor.GREEN + "Jail set!");
    }

    private void setFlag(Player player, String team){
        plugin.getConfig().set("paintball." + team + ".flagposition", player.getLocation());
        plugin.saveConfig();
        player.sendMessage(ChatColor.GREEN + team + " flag location set");
    }

    private void addSpawnPoint(Player player, String team){ //Adds a spawn point
        int currentSpawnPoints = 0; //How many spawn points a team has
        if (plugin.getConfig().contains("paintball." + team + ".spawnPointAmount")){ //If there is one ore more spawnpoint
            currentSpawnPoints = (int) plugin.getConfig().get("paintball." + team + ".spawnPointAmount"); //Sets the int for spawnpoints to it
            currentSpawnPoints++;
            player.sendMessage("This is " + team + " teams " + currentSpawnPoints + "rd spawn point");
        } else { //If there are no spawnpoints
            currentSpawnPoints = 1; //Changes current spawn point to 1 because one is made
            player.sendMessage("This is " + team + "s first spawn point");
        }
        plugin.getConfig().set("paintball." + team + ".spawnPoints." + currentSpawnPoints, player.getLocation()); //Adds the spawn point
        plugin.getConfig().set("paintball." + team + ".spawnPointAmount", currentSpawnPoints); //Updates the spawn point amount in the config
        plugin.saveConfig();
        player.sendMessage(ChatColor.GREEN + team + " spawn point added");
    }

    private void clearConfig(CommandSender sender){
        for (String key : plugin.getConfig().getKeys(false)){
            plugin.getConfig().set(key, null);
        }
        plugin.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Config has been cleard!");
    }


}


