package me.Joey.CommandsPlus.CustomInventories.CategorizedCraftInventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Joey.CommandsPlus.Main;
import me.Joey.CommandsPlus.CustomInventories.InventoryManager;
import net.md_5.bungee.api.ChatColor;

public class EnchantedBookCraftsInventory {
	public static void createEnchantedBookCraftsInventory() {
		InventoryManager.enchantedBookCraftsInventory = Bukkit.createInventory(null, 54, ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.DARK_GRAY + " | Potions");
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		
		for (int i = 0; i < 54; i++) {
			InventoryManager.enchantedBookCraftsInventory.setItem(i, item);
		}
		
		List<String> lore = new ArrayList <>();
		
		int slot = 10;
		for (ItemStack itemInList : Main.customEnchantedBookList) {
			InventoryManager.enchantedBookCraftsInventory.setItem(slot, itemInList);
			slot++;
		}
		
		
		// close menu button
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.RED + "Close Menu");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.enchantedBookCraftsInventory.setItem(49, item);
		
		// back button
		item.setType(Material.ARROW);
		meta.setDisplayName(ChatColor.GOLD + "Go Back");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.enchantedBookCraftsInventory.setItem(48, item);
	}
}
