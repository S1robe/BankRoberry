package net.gtacraft.bankrobbery;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import me.Strobe.gtacore.Utilities.CoreUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RobNPCCommand implements CommandExecutor {
	private FileConfiguration config = Main.getInstance().getConfig();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("robnpc")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Utils.translate("&4You must be a player to execute this command!"));

				return false;
			}

			Player p = (Player) sender;
			NPC selected = CitizensAPI.getDefaultNPCSelector().getSelected(sender);

			if (selected == null) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lStore Robbery"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/robnpc create"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/robnpc robtime <time in seconds>"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/robnpc radius &7(Must have world edit cuboid selected)"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/robnpc pigmen"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/robnpc additem <chance>"));

				return false;
			}

			if (args.length == 0) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lStore Robbery"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/robnpc create"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/robnpc robtime <time in seconds>"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/robnpc radius &7(Must have world edit cuboid selected)"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/robnpc pigmen"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/robnpc additem <chance>"));

				return false;
			}

			if (args[0].equalsIgnoreCase("create")) {
				if (config.getConfigurationSection("rob-npcs." + selected.getId()) == null) {
					config.set("rob-npcs." + selected.getId() + ".time-to-rob", 120);
					config.createSection("rob-npcs." + selected.getId() + ".loot.items");
					config.createSection("rob-npcs." + selected.getId() + ".loot.commands");
					config.set("rob-npcs." + selected.getId() + ".phase", RobPhase.CAN_ROB.toString());
					Main.getInstance().saveConfig();

					Utils.sendMessage(p, MessageType.SUCCESS, "The selected NPC was assigned as a rob NPC.");

					return true;
				} else {
					Utils.sendMessage(p, MessageType.ERROR,
							"The selected NPC could not be assigned. This may be due to it already being configured.");

					return false;
				}
			} else {
				// sub-commands requiring an NPC select to be a rob NPC

				UUID uuid = UUID.randomUUID();
				Map<String, Object> values = new HashMap<String, Object>();

				if (config.getConfigurationSection("rob-npcs." + selected.getId()) == null) {
					Utils.sendMessage(p, MessageType.ERROR, "The selected NPC is not a rob NPC.");

					return false;
				}

				if (args[0].equalsIgnoreCase("robtime")) {
					if (args.length < 2) {
						Utils.sendMessage(p, MessageType.ERROR, "You have not specified a time.");

						return false;
					}

					int seconds = Integer.valueOf(args[1]);

					config.set("rob-npcs." + selected.getId() + ".time-to-rob", seconds);
					Main.getInstance().saveConfig();

					Utils.sendMessage(p, MessageType.SUCCESS,
							"Time to rob of " + seconds + " seconds for the selected NPC.");

				} else if (args[0].equalsIgnoreCase("additem")) {
					if (args.length < 2) {
						Utils.sendMessage(p, MessageType.ERROR, "Must specify a percentage to add.");

						return false;
					}

					ItemStack hand = p.getItemInHand();
					values.put("item", hand);
					values.put("amount", hand.getAmount());
					values.put("chance", args[1]);
					config.set("rob-npcs." + selected.getId() + ".loot.items." + uuid, values);
					Main.getInstance().saveConfig();

					Utils.sendMessage(p, MessageType.SUCCESS, "Added item to rob NPC's loot.");
				} else if (args[0].equalsIgnoreCase("addcmd")) {
					if (args.length < 3) {
						Utils.sendMessage(p, MessageType.ERROR, "Must specify a percentage and command to add.");

						return false;
					}

					StringBuilder builder = new StringBuilder();

					for (int i = 2; i < args.length; i++) {
						builder.append(args[i] + " ");
					}

					values.put("command", builder.toString().trim());
					values.put("chance", args[1]);
					config.set("rob-npcs." + selected.getId() + ".loot.commands." + uuid, values);
					Main.getInstance().saveConfig();

					Utils.sendMessage(p, MessageType.SUCCESS, "Added command to rob NPC's loot.");
				} else if (args[0].equalsIgnoreCase("radius")) {

					WorldEditPlugin worldEditPlugin = null;
					worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
					if(worldEditPlugin == null){
						p.sendMessage("Error with region undoing! Error: WorldEdit is null.");
						return true;
					}

					Selection selection = Main.getInstance().worldEdit.getSelection(p);
					if (selection != null) {
						Location min = selection.getMinimumPoint();
						Location max = selection.getMaximumPoint();

						// min
						config.set("rob-npcs." + selected.getId() + ".min.world", min.getWorld().getName());
						config.set("rob-npcs." + selected.getId() + ".min.x", min.getX());
						config.set("rob-npcs." + selected.getId() + ".min.y", min.getY());
						config.set("rob-npcs." + selected.getId() + ".min.z", min.getZ());

						// max
						config.set("rob-npcs." + selected.getId() + ".max.world", max.getWorld().getName());
						config.set("rob-npcs." + selected.getId() + ".max.x", max.getX());
						config.set("rob-npcs." + selected.getId() + ".max.y", max.getY());
						config.set("rob-npcs." + selected.getId() + ".max.z", max.getZ());

						Main.getInstance().saveConfig();

						Utils.sendMessage(p, MessageType.SUCCESS, "The selection was set as this rob NPC's rob area.");
					} else {
						Utils.sendMessage(p, MessageType.ERROR, "You do not have a WorldEdit selection.");
					}
				} else if (args[0].equalsIgnoreCase("pigmen")) {
					Location pLoc = p.getLocation();

					config.set("rob-npcs." + selected.getId() + ".pigmen.world", pLoc.getWorld().getName());
					config.set("rob-npcs." + selected.getId() + ".pigmen.x", pLoc.getX());
					config.set("rob-npcs." + selected.getId() + ".pigmen.y", pLoc.getY());
					config.set("rob-npcs." + selected.getId() + ".pigmen.z", pLoc.getZ());

					Utils.sendMessage(p, MessageType.SUCCESS, "Zombie pigmen location set.");
					
					Main.getInstance().saveConfig();
				} else {
					Utils.sendMessage(p, MessageType.ERROR, "Not a valid sub-command of /robnpc.");

					return false;
				}
			}
		}
		else if (cmd.getName().equalsIgnoreCase("rob")){
			if(sender.isOp()){
				if(args[0].equalsIgnoreCase("untag"))
					if(Bukkit.getPlayer(args[1]) != null){
						Main.ctp.getTagManager().untag(Bukkit.getPlayer(args[1]).getUniqueId());
						sender.sendMessage(CoreUtils.colorize("Player: " + args[1] + " untagged" ));
						return true;
					}
			}
		}

		return false;
	}
}
