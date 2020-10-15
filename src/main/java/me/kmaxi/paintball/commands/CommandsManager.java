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
        if (args.length == 1){ //If just one argument was added
            if (args[0].equalsIgnoreCase(plugin.commands.cmd1)){ //if the argument equals to the string that starts a game
                plugin.gameManager.startGame();
                return true;
            }
            if (args[0].equalsIgnoreCase(plugin.commands.cmd5)){ //if the argument equals to the string that ends a game
                plugin.gameManager.endGame();
                return true;
            }
            if (!(sender instanceof Player)){ //If sender is console then it stops them from using any future commands
                sender.sendMessage(ChatColor.RED + "Wrong command or this may only be executed by players");
                return true;
            }
            if (args[0].equalsIgnoreCase(plugin.commands.cmd4)){ //If the argument equals to the string that sets the position of the jail
                this.setJail(((Player) sender).getPlayer());
                return true;
            }
            if (args[0].equalsIgnoreCase(plugin.commands.cmd2) || args[0].equalsIgnoreCase(plugin.commands.cmd3)){ //If the command requires another argument
                this.missingArgument(sender);
                return true;
            }
        }


        return true;

    }

    private void commandHelp(CommandSender sender){ //sends information off all available commands
        sender.sendMessage(ChatColor.YELLOW + "----------" + ChatColor.WHITE + " Commands: " + ChatColor.YELLOW + "-------------------");
        sender.sendMessage(ChatColor.GOLD + "/paintball " + plugin.commands.cmd1 + ChatColor.WHITE + " starts the game");
        sender.sendMessage(ChatColor.GOLD + "/paintball " + plugin.commands.cmd5 + ChatColor.WHITE + " forces the game to end");
        sender.sendMessage(ChatColor.GOLD + "/paintball " + plugin.commands.cmd4 + ChatColor.WHITE + " sets the location of the jail");
        sender.sendMessage(ChatColor.GOLD + "/paintball " + plugin.commands.cmd3 + ChatColor.RED + " red" + ChatColor.BLUE + " blue" + ChatColor.WHITE + "adds a spawn point for the chosen team");
        sender.sendMessage(ChatColor.GOLD + "/paintball " + plugin.commands.cmd2 + ChatColor.RED + " red" + ChatColor.BLUE + " blue" + ChatColor.WHITE + ChatColor.WHITE + " sets the flags location");
    }

    private void missingArgument(CommandSender sender){
        sender.sendMessage(ChatColor.RED + "Missing argument." + ChatColor.GRAY +  "Please add " + ChatColor.RED + "red " + ChatColor.GRAY + " or " + ChatColor.BLUE + "blue");
    }

    private void setJail(Player player){
        //TODO set jail position

    }


}


