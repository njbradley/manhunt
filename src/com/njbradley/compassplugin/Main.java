package com.njbradley.compassplugin;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.njbradley.compassplugin.commands.AddHunter;
import com.njbradley.compassplugin.commands.AddRunner;
import com.njbradley.compassplugin.commands.ListPlayers;
import com.njbradley.compassplugin.commands.RemovePlayer;
import com.njbradley.compassplugin.listeners.Listeners;

import net.minecraft.server.v1_16_R2.NBTTagCompound;

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
		
	public ItemStack createTrackerCompass(Location loc) {
		net.minecraft.server.v1_16_R2.ItemStack stack = CraftItemStack.asNMSCopy(new ItemStack(Material.COMPASS));
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound postag = new NBTTagCompound();
		NBTTagCompound nametag = new NBTTagCompound();
		nametag.setString("Name",  "{\"text\": \"Player Tracker\"}");
		tag.set("display", nametag);
		if (loc == null) {
			tag.setBoolean("LodestoneTracked", true);
			tag.setString("LodestoneDimension", "overworld");
			postag.setInt("X", 0);
			postag.setInt("Y", 0);
			postag.setInt("Z", 0);
		} else {
			if (loc.getWorld().getEnvironment() == Environment.NORMAL) {
				tag.setString("LodestoneDimension", "overworld");
			} else if (loc.getWorld().getEnvironment() == Environment.NETHER) {
				tag.setString("LodestoneDimension", "the_nether");
			} else if (loc.getWorld().getEnvironment() == Environment.THE_END) {
				tag.setString("LodestoneDimension", "the_end");
			}
			tag.setBoolean("LodestoneTracked", false);
			postag.setInt("X", loc.getBlockX());
			postag.setInt("Y", loc.getBlockY());
			postag.setInt("Z", loc.getBlockZ());
		}
		tag.set("LodestonePos", postag);
		stack.setTag(tag);
		ItemStack result = CraftItemStack.asBukkitCopy(stack);
		return result;
	}
	
  @Override
  public void onDisable() {
    getLogger().info("The manhunt plugin has been disabled.");
  }
}
