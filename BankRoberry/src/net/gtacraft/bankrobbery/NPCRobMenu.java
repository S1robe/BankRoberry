package net.gtacraft.bankrobbery;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class NPCRobMenu {
	public static final String MENU_TITLE = "Cashier";

	private static Inventory robMenu = Bukkit.createInventory(null, 9, MENU_TITLE);

	public NPCRobMenu(NPC npc) {
		ItemStack blackPane = new ItemStack(160, 1, (byte) 7);
		blackPane.getItemMeta().setDisplayName(" ");
		blackPane.getItemMeta().setLore(new ArrayList<String>());

		ItemStack rob = new ItemStack(Material.GOLD_INGOT);
		ItemMeta robInfo = rob.getItemMeta();
		robInfo.setDisplayName(Utils.translate("&c&lRob Store"));
		List<String> robLore = new ArrayList<String>();
		robLore.add(Utils.translate("&7Click to start robbing the store."));
		robLore.add("");
		robLore.add(Utils.translate("&c&lWanted Level (&7+ 3&c&l)"));
		robLore.add("");
		robLore.add(Utils.translate("&c&lNOTE: &7When robbing the store, make sure"));
		robLore.add(Utils.translate("&7you stay inside the store. Otherwise, robbing"));
		robLore.add(Utils.translate("&7the store will be cancelled."));
		// store NPC's ID in lore as we can't add our own metadata to it
		robLore.add(Utils.translate("&0" + npc.getId() + ""));
		robInfo.setLore(robLore);
		rob.setItemMeta(robInfo);

		robMenu.setItem(0, blackPane);
		robMenu.setItem(1, blackPane);
		robMenu.setItem(2, blackPane);
		robMenu.setItem(4, blackPane);
		robMenu.setItem(6, blackPane);
		robMenu.setItem(7, blackPane);
		robMenu.setItem(8, blackPane);

		robMenu.setItem(4, rob);
	}

	public Inventory getInventory() {
		return robMenu;
	}
}
