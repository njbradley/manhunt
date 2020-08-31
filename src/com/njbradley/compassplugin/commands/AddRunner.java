package com.njbradley.compassplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.njbradley.compassplugin.Main;
import com.njbradley.compassplugin.PlayerData;

public class AddRunner implements CommandExecutor {
	Main plugin;
	RemovePlayer remover;
	
	public AddRunner(Main plugin) {
		remover = new RemovePlayer(plugin);
		this.plugin = plugin;
		plugin.getCommand("runner").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		Bukkit.broadcastMessage(player.getDisplayName() + " is now a runner.");
		remover.remove(player);
		if (sender instanceof Player) {
			plugin.runners.add(new PlayerData(player));
			return true;
		}
		return false;
	}
}
