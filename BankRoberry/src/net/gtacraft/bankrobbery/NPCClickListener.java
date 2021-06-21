package net.gtacraft.bankrobbery;


import me.Strobe.gtacore.Utilities.Wanted.WPlayer;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCClickListener implements Listener {
	@EventHandler
	public void onNPCRightClick(NPCRightClickEvent e) {
		ConfigurationSection section = NPCConfigHelper.getNPCSection(e.getNPC().getId());

		if (Main.getInstance().getConfig().contains("rob-npcs." + e.getNPC().getId()) && section != null) {
			if (RobPhase.valueOf(section.getString("phase")) == RobPhase.CAN_ROB) {
				if(WPlayer.getByPlayer(e.getClicker()).isCop()) {
					e.getClicker().sendMessage(Utils.translate("&cYou cannot rob stores while in /cop mode."));
					return;
				}
				e.getClicker().openInventory(new NPCRobMenu(e.getNPC()).getInventory());

				e.getClicker().playSound(e.getClicker().getLocation(), Sound.VILLAGER_IDLE, 10, 2);
			} else if (RobPhase.valueOf(section.getString("phase")) == RobPhase.ROBBING) {
				Utils.sendMessage(e.getClicker(), MessageType.ERROR, "This NPC is already being robbed.");
			}
		}
	}
}
