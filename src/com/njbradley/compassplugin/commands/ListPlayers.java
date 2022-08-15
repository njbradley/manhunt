package com.njbradley.compassplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import com.njbradley.compassplugin.Main;
import com.njbradley.compassplugin.PlayerData;

public class ListPlayers implements CommandExecutor {
    Main plugin;

    public ListPlayers(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("players").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "Runners: " + plugin.runners.size());
        for (PlayerData rd : plugin.runners) {
            sender.sendMessage(rd.player.getDisplayName());
        }
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hunters: " + plugin.hunters.size());
        for (PlayerData hd : plugin.hunters) {
            sender.sendMessage(hd.player.getDisplayName());
        }
        return true;
    }
}
