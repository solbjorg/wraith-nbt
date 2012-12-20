package net.teamwraith.wraithnbt;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Material;

public abstract class NBTItem {
 
	private org.bukkit.inventory.ItemStack item;
	
	private List<String> tags = new ArrayList<String>();
	
	public NBTItem(Material material) {
		item = new org.bukkit.inventory.ItemStack(material);
	}
	
}
