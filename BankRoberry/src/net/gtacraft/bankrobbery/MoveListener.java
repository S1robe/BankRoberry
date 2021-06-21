package net.gtacraft.bankrobbery;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import static net.gtacraft.bankrobbery.NPCRobMenuListener.robs;

public class MoveListener implements Listener {

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if (robs.containsKey(p.getUniqueId())) {
			Location pLoc = p.getLocation();
			RobStorageObject obj = robs.get(p.getUniqueId());

			if (!(RobPhase
					.valueOf(NPCConfigHelper.getNPCSection(obj.npc.getId()).getString("phase")) == RobPhase.ROBBED)) {

					p.playSound(pLoc, Sound.ZOMBIE_WOODBREAK, 10, 1);

					NPCConfigHelper.getNPCSection(obj.npc.getId()).set("phase", RobPhase.CAN_ROB.toString());
					Main.getInstance().saveConfig();


				for (int i : obj.tasks) {
					Bukkit.getScheduler().cancelTask(i);
				}
				robs.remove(p.getUniqueId());
				Main.ctp.getTagManager().untag(p.getUniqueId());

			}

		}
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();

		if (robs.containsKey(p.getUniqueId())) {
			Location pLoc = p.getLocation();
			RobStorageObject obj = robs.get(p.getUniqueId());

			Location min = new Location(
					Bukkit.getWorld(NPCConfigHelper.getNPCSection(obj.npc.getId()).getString("min.world")),
					NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("min.x"),
					NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("min.y"),
					NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("min.z"));

			Location max = new Location(
					Bukkit.getWorld(NPCConfigHelper.getNPCSection(obj.npc.getId()).getString("max.world")),
					NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("max.x"),
					NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("max.y"),
					NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("max.z"));

			if (!(RobPhase
					.valueOf(NPCConfigHelper.getNPCSection(obj.npc.getId()).getString("phase")) == RobPhase.ROBBED)) {
				if (!locationIsInCuboid(pLoc, min, max)) {
					p.sendMessage(Utils.translate("&c&l(!) &7You have left the store. Unsuccessful robbery."));

					p.playSound(pLoc, Sound.ZOMBIE_WOODBREAK, 10, 1);

					NPCConfigHelper.getNPCSection(obj.npc.getId()).set("phase", RobPhase.CAN_ROB.toString());
					Main.getInstance().saveConfig();

					for (int i : obj.tasks) {
						Bukkit.getScheduler().cancelTask(i);
					}
					robs.remove(p.getUniqueId());
					Main.ctp.getTagManager().untag(p.getUniqueId());
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		if (robs.containsKey(p.getUniqueId())) {
			Location pLoc = p.getLocation();
			RobStorageObject obj = robs.get(p.getUniqueId());
				Location min = new Location(
						Bukkit.getWorld(NPCConfigHelper.getNPCSection(obj.npc.getId()).getString("min.world")),
						NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("min.x"),
						NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("min.y"),
						NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("min.z"));

				Location max = new Location(
						Bukkit.getWorld(NPCConfigHelper.getNPCSection(obj.npc.getId()).getString("max.world")),
						NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("max.x"),
						NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("max.y"),
						NPCConfigHelper.getNPCSection(obj.npc.getId()).getDouble("max.z"));

				if (!(RobPhase
						.valueOf(NPCConfigHelper.getNPCSection(obj.npc.getId()).getString("phase")) == RobPhase.ROBBED)) {
					if (!locationIsInCuboid(pLoc, min, max)) {
						p.sendMessage(Utils.translate("&c&l(!) &7You have left the store. Unsuccessful robbery."));

						p.playSound(pLoc, Sound.ZOMBIE_WOODBREAK, 10, 1);

						NPCConfigHelper.getNPCSection(obj.npc.getId()).set("phase", RobPhase.CAN_ROB.toString());
						Main.getInstance().saveConfig();

						for (int i : obj.tasks) {
							Bukkit.getScheduler().cancelTask(i);
						}
						robs.remove(p.getUniqueId());
						Main.ctp.getTagManager().untag(p.getUniqueId());
					}
				}


		}
	}

	public boolean locationIsInCuboid(Location playerLocation, Location min, Location max) {
		boolean trueOrNot = false;

		if (playerLocation.getWorld() == min.getWorld() && playerLocation.getWorld() == max.getWorld()) {
			if (playerLocation.getX() >= min.getX() && playerLocation.getX() <= max.getX()) {
				if (playerLocation.getY() >= min.getY() && playerLocation.getY() <= max.getY()) {
					if (playerLocation.getZ() >= min.getZ() && playerLocation.getZ() <= max.getZ()) {
						trueOrNot = true;
					}
				}
			}

			if (playerLocation.getX() <= min.getX() && playerLocation.getX() >= max.getX()) {
				if (playerLocation.getY() <= min.getY() && playerLocation.getY() >= max.getY()) {
					if (playerLocation.getZ() <= min.getZ() && playerLocation.getZ() >= max.getZ()) {
						trueOrNot = true;
					}
				}
			}
		}

		return trueOrNot;
	}
}
