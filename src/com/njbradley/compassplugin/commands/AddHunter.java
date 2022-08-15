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

public class AddHunter implements CommandExecutor {
    Main plugin;
    RemovePlayer remover;

    public AddHunter(Main plugin) {
        remover = new RemovePlayer(plugin);
        this.plugin = plugin;
        plugin.getCommand("hunter").setExecutor(this);
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
        remover.remove(player);
        HunterData data = new HunterData(player);
        plugin.giveTracker(data, null);
        plugin.hunters.add(data);
        Bukkit.broadcastMessage(player.getDisplayName() + " is now a hunter.");
        return true;
    }
}
