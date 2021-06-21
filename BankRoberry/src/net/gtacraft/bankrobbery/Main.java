package net.gtacraft.bankrobbery;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.minelink.ctplus.CombatTagPlus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Main plugin;
	public WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	public static CombatTagPlus ctp;

	public void onEnable() {
		plugin = this;

		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new NPCClickListener(), this);
		pm.registerEvents(new NPCRobMenuListener(), this);
		pm.registerEvents(new MoveListener(), this);

		if(getCombatTagPlus() == null){
			System.out.println("CombatTagsPlus was not found! Disabling plugin...");
			Bukkit.getPluginManager().disablePlugin(this);
		} else {
			ctp = getCombatTagPlus();
		}

		this.getCommand("robnpc").setExecutor(new RobNPCCommand());
		this.getCommand("rob").setExecutor(new RobNPCCommand());

		this.saveDefaultConfig();

		// reset all NPCs on start up
		if (this.getConfig().getConfigurationSection("rob-npcs") != null) {
			for (String id : this.getConfig().getConfigurationSection("rob-npcs").getKeys(false)) {
				ConfigurationSection section = NPCConfigHelper.getNPCSection(Integer.valueOf(id));
				section.set("phase", RobPhase.CAN_ROB.toString());
				saveConfig();
			}
		}
	}

	public void onDisable() {
		plugin = null;
	}

	public static Main getInstance() {
		return plugin;
	}

	private CombatTagPlus getCombatTagPlus(){
		Plugin plugin = getServer().getPluginManager().getPlugin("CombatTagPlus");
		if(!(plugin instanceof CombatTagPlus)){
			return null;
		}
		return (CombatTagPlus) plugin;
	}
}
