package net.gtacraft.bankrobbery;

import org.bukkit.configuration.ConfigurationSection;

public class NPCConfigHelper {
	public static ConfigurationSection getNPCSection(int id) {
		return Main.getInstance().getConfig().getConfigurationSection("rob-npcs." + id);
	}
}
