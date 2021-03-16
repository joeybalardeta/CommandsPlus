package me.Joey.CommandsPlus.CustomInventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class MasterCraftsInventory {
	public static void createMasterCraftsInventory() {
		InventoryManager.masterCraftsInventory = Bukkit.createInventory(null, 54, ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.DARK_GRAY + " Menu");
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		
		for (int i = 0; i < 54; i++) {
			InventoryManager.masterCraftsInventory.setItem(i, item);
		}
		
		
		// Custom Weapon Crafts Menu item
		item.setType(Material.IRON_AXE);
		meta.setDisplayName(ChatColor.YELLOW + "Weapon Crafting Recipes");
		List<String> lore = new ArrayList <>();
		lore.clear();

		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		InventoryManager.masterCraftsInventory.setItem(19, item);
		
		
		// Custom Tool Crafts Menu item
		item.setType(Material.IRON_PICKAXE);
		meta.setDisplayName(ChatColor.YELLOW + "Tool Crafting Recipes");
		lore.clear();

		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		InventoryManager.masterCraftsInventory.setItem(20, item);
		
		
		// Custom Armor Crafts Menu item
		item.setType(Material.IRON_CHESTPLATE);
		meta.setDisplayName(ChatColor.YELLOW + "Armor Crafting Recipes");
		lore.clear();

		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		InventoryManager.masterCraftsInventory.setItem(21, item);
		
		
		// Custom Potion Crafts Menu item
		item.setType(Material.BREWING_STAND);
		meta.setDisplayName(ChatColor.YELLOW + "Potion Crafting Recipes");
		lore.clear();

		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		InventoryManager.masterCraftsInventory.setItem(22, item);
		
		
		// Miscellaneous Crafts Menu item
		item.setType(Material.LAVA_BUCKET);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName(ChatColor.YELLOW + "Miscellaneous Crafting Recipes");
		lore.clear();

		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.masterCraftsInventory.setItem(23, item);
		
		
		// Custom Crafts Menu item
		item.setType(Material.STICK);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName(ChatColor.YELLOW + "Existing Item Crafting Recipes");
		lore.clear();

		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.masterCraftsInventory.setItem(24, item);
		
		
		// Talent Crafts Menu item
		item.setType(Material.END_CRYSTAL);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName(ChatColor.YELLOW + "Talent Crafting Recipes");
		lore.clear();

		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.masterCraftsInventory.setItem(25, item);
		
		
		// Enchantment Crafts Menu item
		item.setType(Material.ENCHANTING_TABLE);
		meta.setDisplayName(ChatColor.YELLOW + "Enchanting Crafting Recipes");
		lore.clear();

		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.masterCraftsInventory.setItem(31, item);
		
		
		// close menu button
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.RED + "Close Menu");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.masterCraftsInventory.setItem(49, item);
		
		// back button
		item.setType(Material.ARROW);
		meta.setDisplayName(ChatColor.GOLD + "Go Back");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.masterCraftsInventory.setItem(48, item);
	}
}
