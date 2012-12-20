package net.teamwraith.wraithnbt;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRoot implements CommandExecutor {

	private WraithNBT plugin;
	
	public CommandRoot(WraithNBT plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender,
			org.bukkit.command.Command command, String label, String[] args) {
		
		// TODO This will be used to get an NBT value from an item.
		if (args[0].equals("get")) {
			sender.sendMessage(ChatColor.RED + "This feature has not been implemented yet.");
			return true;
		// Sets an NBT value.
		} else if (args[0].equals("set")) {
			
			
			
		} else {
			
		}
		
		return true;
	}

}
