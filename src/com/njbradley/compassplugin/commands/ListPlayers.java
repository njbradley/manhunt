package com.njbradley.compassplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
		sender.sendMessage("Current runners: " + plugin.runners.size());
		for (PlayerData rd : plugin.runners) {
			sender.sendMessage(rd.player.getDisplayName());
		}
		sender.sendMessage("Current hunters: " + plugin.hunters.size());
		for (PlayerData hd : plugin.hunters) {
			sender.sendMessage(hd.player.getDisplayName());
		}
		return true;
	}
}
