package com.njbradley.compassplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.njbradley.compassplugin.Main;
import com.njbradley.compassplugin.PlayerData;

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
		Player player = (Player) sender;
		Bukkit.broadcastMessage(player.getDisplayName() + " is now a hunter.");
		remover.remove(player);
		player.getInventory().setItem(8, plugin.createTrackerCompass(null));
		if (sender instanceof Player) {
			plugin.hunters.add(new PlayerData(player));
			return true;
		}
		return false;
	}
}
