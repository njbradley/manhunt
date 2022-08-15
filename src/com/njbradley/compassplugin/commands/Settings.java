package com.njbradley.compassplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import com.njbradley.compassplugin.Main;
import com.njbradley.compassplugin.PlayerData;
import com.njbradley.compassplugin.HunterData;

public class Settings implements CommandExecutor {
    Main plugin;

    public Settings(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("settings").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player) {
            if (args.length == 0) {
                printMenu(sender);
            } else {
                String settingName = args[0];
                if (settingName.equals("1") || settingName.equals("distanceResolution")) {
                    if (args.length == 1) {
                        sender.sendMessage("distanceResolution is " + plugin.distanceResolution + ".");
                        return true;
                    }
                    String value = args[1];
                    try {
                        int newDistanceResolution = Integer.parseInt(value);
                        if (newDistanceResolution >= 0) {
                            plugin.distanceResolution = newDistanceResolution;
                            Bukkit.broadcastMessage("distanceResolution is now " + plugin.distanceResolution + ".");
                        } else {
                            sender.sendMessage(ChatColor.RED + "distanceResolution must be >= 0.");
                        }
                    } catch(NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "distanceResolution must be an integer.");
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void printMenu(CommandSender sender) {
        sender.sendMessage(ChatColor.BLUE + "Manhunt Settings:");
        sender.sendMessage("1. distanceResolution (int) = " + plugin.distanceResolution);
    }
}
