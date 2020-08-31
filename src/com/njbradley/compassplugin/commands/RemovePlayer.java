package com.njbradley.compassplugin.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.njbradley.compassplugin.Main;
import com.njbradley.compassplugin.PlayerData;

public class RemovePlayer implements CommandExecutor {
	Main plugin;
	
	public RemovePlayer(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("remove").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("You are no longer part of the manhunt.");
		remove ((Player) sender);
		return true;
	}

	public void remove(Player player) {
		for (PlayerData rd : plugin.runners) {
			if (player == rd.player) {
				plugin.runners.remove(rd);
				break;
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
