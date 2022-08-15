package com.njbradley.compassplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import com.njbradley.compassplugin.Main;
import com.njbradley.compassplugin.PlayerData;
import com.njbradley.compassplugin.HunterData;

public class RemovePlayer implements CommandExecutor {
    Main plugin;

    public RemovePlayer(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("remove").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (args.length > 1) {
            return false;
        }
        if (args.length == 1) {
            player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "The given player does not exist.");
                return true;
            }
        } else if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(ChatColor.RED + "You are not a player.");
            return true;
        }
        remove(player);
        Bukkit.broadcastMessage(player.getDisplayName() + " is no longer part of the manhunt.");
        return true;
    }

    public void remove(Player player) {
        for (PlayerData rd : plugin.runners) {
            if (player == rd.player) {
                plugin.runners.remove(rd);
                break;
            }
        }
        for (HunterData ht : plugin.hunters) {
            if (ht.trackedPlayer != null && ht.trackedPlayer.player == player) {
                ht.trackedPlayer = null;
            }
        }
        for (PlayerData rd : plugin.hunters) {
            if (player == rd.player) {
                plugin.hunters.remove(rd);
                player.getInventory().remove(Material.COMPASS);
                if (player.getInventory().getItemInOffHand().getType() == Material.COMPASS) {
                    player.getInventory().setItemInOffHand(null);
                }
                break;
            }
        }
    }
}
