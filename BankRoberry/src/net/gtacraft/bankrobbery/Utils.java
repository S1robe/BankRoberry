package net.gtacraft.bankrobbery;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.stream.Stream;

public class Utils {
	public static void sendMessage(Player p, MessageType type, String message) {
		if (type == MessageType.SUCCESS) {
			p.sendMessage(Utils.translate("&a" + message));
		} else if (type == MessageType.ERROR) {
			p.sendMessage(Utils.translate("&4" + message));
		}
	}
	
	public static String translate(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	public static int countEmptySlots(Player p) {
		return (int) Stream.of(p.getInventory().getContents()).filter(Objects::isNull).count();
	}
}
