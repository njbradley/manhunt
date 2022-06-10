package com.njbradley.compassplugin;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.njbradley.compassplugin.commands.AddHunter;
import com.njbradley.compassplugin.commands.AddRunner;
import com.njbradley.compassplugin.commands.ListPlayers;
import com.njbradley.compassplugin.commands.RemovePlayer;
import com.njbradley.compassplugin.listeners.Listeners;

public class Main extends JavaPlugin {
	World world;
	public ArrayList<PlayerData> runners;
	public ArrayList<PlayerData> hunters;
		
  @Override
  public void onEnable() {
		runners = new ArrayList<PlayerData>();
		hunters = new ArrayList<PlayerData>();
    getLogger().info("The manhunt plugin has been enabled.");
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		new AddHunter(this);
    new AddRunner(this);
		new RemovePlayer(this);
		new ListPlayers(this);
  }
	
  	public void giveTracker(Player player, Location loc) {
  		int x = 0;
  		int y = 0;
  		int z = 0;
  		String dimension = "the_end";
  		if (loc != null) {
			if (loc.getWorld().getEnvironment() == Environment.NORMAL) {
				dimension = "overworld";
			} else if (loc.getWorld().getEnvironment() == Environment.NETHER) {
				dimension = "the_nether";
			} else if (loc.getWorld().getEnvironment() == Environment.THE_END) {
				dimension = "the_end";
			}
			x = loc.getBlockX();
			y = loc.getBlockY();
			z = loc.getBlockZ();
		}
  		String nbt = "{\"LodestonePos\":{\"X\":" + x + ",\"Y\":" + y + ",\"Z\":" + z + "},\"LodestoneDimension\":\"" + dimension
  				+ "\",\"LodestoneTracked\":0b}";
  		String command = "give @p compass" + nbt;
  		getServer().dispatchCommand(player, command);
  	}
  	
  	public void clearTracker(Player player) {
  		String command = "clear @p compass";
  		getServer().dispatchCommand(player, command);
  	}
  	
//	public ItemStack createTrackerCompass(Location loc) {
//		
////		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(new ItemStack(Material.COMPASS));
////		NBTTagCompound tag = new NBTTagCompound();
////		NBTTagCompound postag = new NBTTagCompound();
////		NBTTagCompound nametag = new NBTTagCompound();
////		nametag.putString("Name",  "{\"text\": \"Player Tracker\"}");
////		tag.set("display", nametag);
////		if (loc == null) {
////			tag.setBoolean("LodestoneTracked", true);
////			tag.setString("LodestoneDimension", "overworld");
////			postag.setInt("X", 0);
////			postag.setInt("Y", 0);
////			postag.setInt("Z", 0);
////		} else {
////			if (loc.getWorld().getEnvironment() == Environment.NORMAL) {
////				tag.setString("LodestoneDimension", "overworld");
////			} else if (loc.getWorld().getEnvironment() == Environment.NETHER) {
////				tag.setString("LodestoneDimension", "the_nether");
////			} else if (loc.getWorld().getEnvironment() == Environment.THE_END) {
////				tag.setString("LodestoneDimension", "the_end");
////			}
////			tag.setBoolean("LodestoneTracked", false);
////			postag.setInt("X", loc.getBlockX());
////			postag.setInt("Y", loc.getBlockY());
////			postag.setInt("Z", loc.getBlockZ());
////		}
////		tag.set("LodestonePos", postag);
////		stack.setTag(tag);
////		ItemStack result = CraftItemStack.asBukkitCopy(stack);
////		return result;
//	}
	
  @Override
  public void onDisable() {
    getLogger().info("The manhunt plugin has been disabled.");
  }
}
