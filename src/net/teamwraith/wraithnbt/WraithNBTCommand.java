package net.teamwraith.wraithnbt;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author EternalFacepalm
 */
public class WraithNBTCommand implements CommandExecutor {

	Player player;
	
    private String colorCode(String str) {
	   str = ChatColor.translateAlternateColorCodes('&', str);
	   return str;
    }
    
    private void listEnchantments() {
    	player.sendMessage(WraithNBT.PREFIX + " The available enchantments are:");
		
		for (Enchantment e : Enchantment.values()) {
			player.sendMessage(ChatColor.AQUA + e.getName());
		}
    }
    
    private void listPotionEffects(){
    	player.sendMessage(WraithNBT.PREFIX + " The avaliable potion effects are:");
    	
		for (PotionEffectType e : PotionEffectType.values()){
			// Due to PotionEffectType returning null values
			if (e instanceof PotionEffectType)
				player.sendMessage(ChatColor.AQUA + e.getName());
		}
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {    	
        if (!(sender instanceof Player)) {
        	sender.sendMessage(WraithNBT.ERROR_PREFIX + " Only players can edit NBT tags.");
        	return true;
        }
        
        player = (Player) sender;
        ItemStack item = player.getItemInHand();
       
        if (args.length == 0 || args[0].equalsIgnoreCase("help")){
        	sender.sendMessage(WraithNBT.ERROR_PREFIX + " Options: head, colour, name, potion, enchant, lore, book");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("head")) {
        	if (!player.hasPermission("wraithnbt.head")){
        		sender.sendMessage(WraithNBT.ERROR_PREFIX + " You lack the necessary permissions to perform this command.");
        	}
        	
        	if (args.length != 2){
        		sender.sendMessage(WraithNBT.ERROR_PREFIX + " Usage: /nbt head <name>");
        		return true;
        	}
        	
            String owner = args[1];
            
            if (item.getType().equals(Material.SKULL_ITEM)) {
            	SkullMeta sm = (SkullMeta) item.getItemMeta();
            	sm.setOwner(owner);
            	item.setItemMeta(sm);
            	
                sender.sendMessage(WraithNBT.PREFIX + " SkullOwner set to: " + ChatColor.ITALIC + owner);
                return true;
            }
            sender.sendMessage(WraithNBT.ERROR_PREFIX + " You can only perform this command whilst holding a Steve head.");
            return true;
        }
        else if (args[0].equalsIgnoreCase("colour")) {
        	if (!player.hasPermission("wraithnbt.colour")){
        		sender.sendMessage(WraithNBT.ERROR_PREFIX + " You lack the necessary permissions to perform this command.");
        	}
        	
    	    if (args.length != 4){
        		sender.sendMessage(WraithNBT.ERROR_PREFIX + " Usage: /nbt <colour> <RED> <GREEN> <BLUE>");
        		return true;
        	}
    	    
            Color color;
            int r,g,b;
            
            try {
	            r = Integer.parseInt(args[1]);
	            g = Integer.parseInt(args[2]);
	            b = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
            	sender.sendMessage(WraithNBT.ERROR_PREFIX + "Colours must be integers.");
            	return true;
            }
            
            if (r > 255 || g > 255 || b > 255 ||
            	r < 0   || g < 0   || b < 0) {
                sender.sendMessage(WraithNBT.ERROR_PREFIX + " RGB values vary from 0 - 255.");
                return true;
            }
            
            color = Color.fromRGB(r, g, b);

            Material m = item.getType();
            
            if (
            	m.equals(Material.LEATHER_HELMET) || 
            	m.equals(Material.LEATHER_CHESTPLATE) || 
            	m.equals(Material.LEATHER_LEGGINGS) || 
            	m.equals(Material.LEATHER_BOOTS)
            ) {
                if (color != null) {
                	LeatherArmorMeta lm = (LeatherArmorMeta) item.getItemMeta();
                	lm.setColor(color);
                	item.setItemMeta(lm);
                    sender.sendMessage(WraithNBT.PREFIX + " Colour set.");
                    return true;
                }
                sender.sendMessage(WraithNBT.ERROR_PREFIX + " RGB values cannot be anything but 0 - 255.");
                return true;
            }
            sender.sendMessage(WraithNBT.ERROR_PREFIX + " You can only colour leather armour.");
            return true;
        }
        else if (args[0].equalsIgnoreCase("name")) {
        	if (!player.hasPermission("wraithnbt.name")){
        		sender.sendMessage(WraithNBT.ERROR_PREFIX + " You lack the necessary permissions to perform this command.");
        	}
        	
    	    if (args.length < 2){
        		sender.sendMessage(WraithNBT.ERROR_PREFIX + " Usage: /nbt name <new display name>");
        		return true;
        	}
    	    
    	    String result = "";
    	    for (int i = 1; i < args.length; i++){
    	    	result += args[i];
    	    	if (!(i == args.length-1)){
    	    		result += " ";
    	    	}
    	    }

    	    ItemMeta im = (ItemMeta) item.getItemMeta();
    	    im.setDisplayName(colorCode(result));
    	    item.setItemMeta(im);
    	    
            sender.sendMessage(WraithNBT.PREFIX + " Item name set.");
            return true;
        }
        else if (args[0].equalsIgnoreCase("book")){
        	if (!player.hasPermission("wraithnbt.book")){
        		sender.sendMessage(WraithNBT.ERROR_PREFIX + " You lack the necessary permissions to perform this command.");
        	}
    	    
    	    if (args.length < 3){
    		    sender.sendMessage(WraithNBT.ERROR_PREFIX + " Usage: /nbt book author/title <input>");
    		    return true;
    	    }
    	    
    	    if (!item.getType().equals(Material.WRITTEN_BOOK)){
    		    sender.sendMessage(WraithNBT.ERROR_PREFIX + " You can only perform this command whilst holding a signed book.");
    		    return true;
    	    }
    	    
    	    String result = "";
    	    for (int i = 2; i < args.length; i++){
    	    	result += args[i];
    	    	if (!(i == args.length-1)){
    	    		result += " ";
    	    	}
    	    }
    	    
    	    BookMeta meta = (BookMeta) item.getItemMeta();
    	    
    	    // Sets author
    	    if (args[1].equalsIgnoreCase("author")) {
    	    	meta.setAuthor(colorCode(result));
    	    }
    	    
    	    // Sets title
    	    else if (args[1].equalsIgnoreCase("title")) {
    	    	meta.setTitle(colorCode(result));
    	    }
    	    
    	    item.setItemMeta(meta);
    	    return true;
        }
        else if (args[0].equalsIgnoreCase("lore")) {
        	if (!player.hasPermission("wraithnbt.lore")){
        		sender.sendMessage(WraithNBT.ERROR_PREFIX + " You lack the necessary permissions to perform this command.");
        	}
        	
    	    if (args.length < 3){
    		    sender.sendMessage(WraithNBT.ERROR_PREFIX + " Usage: /nbt lore <#line> <text>");
    		    return true;
    	    }
    	    
        	ItemMeta meta = (ItemMeta) item.getItemMeta();
        	ArrayList<String> lore = new ArrayList<String>();
        	
        	// If the item already has lore
        	if (meta.getLore() != null){
        		lore = (ArrayList<String>) meta.getLore();
        	}
        	
        	String result = "";
    	    for (int i = 2; i < args.length; i++){
    	    	result += args[i];
    	    	// Makes sure that there isn't an extra space at the end.
    	    	if (!(i == args.length-1)){
    	    		result += " ";
    	    	}
    	    }
    	    
    	    // pos is the line the lore will be inserted in.
    	    int pos = 0;
    	    
    	    try {
    	    	pos = Integer.parseInt(args[1])-1;
    	    } catch (NumberFormatException e) {
    	    	sender.sendMessage(WraithNBT.ERROR_PREFIX + "Line number must be an integer.");
    	    }
    	    
    	    // Allows adding of new lines.
    	    if (pos >= lore.size()){
    	    	lore.add(colorCode(result));
    	    } else {
    	    	// Makes sure that the user actually inputs a valid line number
    	    	try {
    	    		lore.set(pos, colorCode(result));
    	    	} catch (IndexOutOfBoundsException e){
    	    		sender.sendMessage(WraithNBT.ERROR_PREFIX + " Invalid line number.");
    	    	}
    	    }
    	    
        	meta.setLore(lore);
        	item.setItemMeta(meta);
        	sender.sendMessage(WraithNBT.PREFIX + " Line "+(pos+1)+" added to lore: "+result);
        	return true;
        }
        else if (args[0].equalsIgnoreCase("potion")) {
        	if (!player.hasPermission("wraithnbt.potion")){
        		sender.sendMessage(WraithNBT.ERROR_PREFIX + " You lack the necessary permissions to perform this command.");
        	}
        	
        	if (args.length == 2){
	        	if (args[1].equalsIgnoreCase("help")){
	        		listPotionEffects();
	        		sender.sendMessage(
	        			WraithNBT.ERROR_PREFIX + " Usage: /nbt potion <effect> <duration in ticks> <level> [<splash (true/false)>]"
	        		);
	        		return true;
	        	}
        	}
        	
    	    if (args.length < 4 || args.length > 5){
    		    sender.sendMessage(
    		    	WraithNBT.ERROR_PREFIX + 
    		    	" Usage: /nbt potion <effect> <duration> <level> [<splash (true/false)>] OR /nbt potion help"
    		    );
    		    return true;
    	    }
    	    
    	    int duration;
        	int level; 
    	    
    	    try { 
    	    	duration = Integer.parseInt(args[2])*20;
            	level = Integer.parseInt(args[3]);
    	    } catch (NumberFormatException e) {
    	    	player.sendMessage(WraithNBT.ERROR_PREFIX + " Duration and level must be integers.");
    	    	return true;
    	    }
    	    
            boolean splash = false;
            
            if (args.length == 5){
            	splash = Boolean.getBoolean(args[4]);
            }
            
            PotionEffectType type = PotionEffectType.getByName(args[1].toUpperCase());
            
            
            if (type == null){
            	sender.sendMessage(WraithNBT.ERROR_PREFIX + " Effect "+args[1]+" not found.");
            	return true;
            }
            
            PotionEffect effect;
            
            effect = type.createEffect(duration, level);
            
            if (!item.getType().equals(Material.POTION)) {
            	sender.sendMessage(WraithNBT.ERROR_PREFIX + " You can only perform this command whilst holding a potion.");
                return true;
            }
            
            if (effect != null) {
            	PotionMeta pm = (PotionMeta) item.getItemMeta();
            	pm.addCustomEffect(effect, false);
            	
            	// Apply splash.
            	if (splash){
            		Potion potion;
	            	try {
	            		potion = Potion.fromItemStack(item);
	            	} catch (IllegalArgumentException e){
	                	sender.sendMessage(WraithNBT.ERROR_PREFIX + " " + e.getMessage() + ".");
	                	return true;
	                }
	            	
	            	potion.setSplash(splash);
	            	potion.apply(item);
            	}
            	item.setItemMeta(pm);
            	
                sender.sendMessage(WraithNBT.PREFIX + " Potion effect added.");
                return true;
            }
            sender.sendMessage(WraithNBT.ERROR_PREFIX + " Invalid effect.");
            return true;
        }
        else if (args[0].equalsIgnoreCase("enchant")) {
        	if (!player.hasPermission("wraithnbt.enchant")){
        		sender.sendMessage(WraithNBT.ERROR_PREFIX + " You lack the necessary permissions to perform this command.");
        	}
        	
        	if (args.length >= 2){
		    	if (args[1].equalsIgnoreCase("help")){
		    		listEnchantments();
		    		sender.sendMessage(WraithNBT.ERROR_PREFIX + " Usage: /nbt enchant <enchantment> <level>");
		    		return true;
		    	}
        	}
        	
    	    if (args.length != 3){
    		    sender.sendMessage(WraithNBT.ERROR_PREFIX + " Usage: /nbt enchant <enchantment> <level> OR /nbt enchant help");
    		    return true;
    	    }
    	    
            Enchantment enchantment = Enchantment.getByName(args[1].toUpperCase());
            
            int level = 1;
            
            try {
            	level = Integer.parseInt(args[2]);
            } catch (NumberFormatException e){
            	sender.sendMessage(WraithNBT.ERROR_PREFIX + " Level must be an integer.");
            }
            
            if (enchantment != null) {
                item.addUnsafeEnchantment(enchantment, level);
                sender.sendMessage(WraithNBT.PREFIX + " Enchantment added.");
                return true;
            }
            sender.sendMessage(WraithNBT.ERROR_PREFIX + "Invalid enchantment. /nbt enchant help");
            return true;
        }
    
    // No options were met.
    sender.sendMessage(WraithNBT.ERROR_PREFIX + " Options: head, colour, name, potion, enchant, lore, book");
    return true;
    }
}