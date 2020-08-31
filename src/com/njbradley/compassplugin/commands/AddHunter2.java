package com.njbradley.compassplugin.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.njbradley.compassplugin.Main;

public class AddHunter implements CommandExecutor {
	Main plugin;
	
	public AddHunter(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("manhunt").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("command run");
		
		return false;
	}
}
