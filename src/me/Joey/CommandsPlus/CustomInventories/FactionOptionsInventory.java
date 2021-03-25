package me.Joey.CommandsPlus.CustomInventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.Joey.CommandsPlus.FunctionsPlus;
import me.Joey.CommandsPlus.Main;
import net.md_5.bungee.api.ChatColor;

public class FactionOptionsInventory {
	public static void createFactionOptionsInventory(Player p) {
		InventoryManager.factionOptionsInventory = Bukkit.createInventory(null, 54, ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.DARK_GRAY + " | Factions");
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		
		for (int i = 0; i < 54; i++) {
			InventoryManager.factionOptionsInventory.setItem(i, item);
		}
		
		
		// Faction Options Menu item
		item = FunctionsPlus.getPlayerHead("r04dk1ll");
		
		
		// set item metadata
		SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
		skullMeta.setDisplayName(ChatColor.AQUA + "Faction ");
		List<String> lore = new ArrayList <>();
		if (Main.factionHashMap.get(p.getUniqueId().toString()) == null || Main.factionHashMap.get(p.getUniqueId().toString()).equals("")) {
			lore.add(ChatColor.WHITE + "Faction - " + ChatColor.YELLOW + "None");
		}
		else {
			lore.add(ChatColor.WHITE + "Faction - " + ChatColor.YELLOW + Main.factionHashMap.get(p.getUniqueId().toString()));
		}
		skullMeta.setLore(lore);
		item.setItemMeta(skullMeta);
		InventoryManager.factionOptionsInventory.setItem(22, item);
		
		
		// close menu button
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.RED + "Close Menu");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.factionOptionsInventory.setItem(49, item);
		
		// back button
		item.setType(Material.ARROW);
		meta.setDisplayName(ChatColor.GOLD + "Go Back");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.factionOptionsInventory.setItem(48, item);
	}
}
