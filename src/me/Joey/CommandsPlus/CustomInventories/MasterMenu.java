package me.Joey.CommandsPlus.CustomInventories;

import me.Joey.CommandsPlus.*;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

public class MasterMenu {
	@SuppressWarnings("deprecation")
	public static void createMasterMenuInventory(Player p) {
		String talent = Main.talentHashMap.get(p.getUniqueId().toString());
		
		
		Main.masterMenuInventory = Bukkit.createInventory(null, 54, ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.DARK_GRAY + " Menu");
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("");
		item.setItemMeta(meta);
		
		
		for (int i = 0; i < 54; i++) {
			Main.masterMenuInventory.setItem(i, item);
		}
		
		
		// Player Info item
		item = FunctionsPlus.getPlayerHead(p.getName());
		SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
		
		
		skullMeta.setDisplayName(ChatColor.GOLD + "Player Info");
		List<String> lore = new ArrayList <>();
		lore.add(ChatColor.WHITE + "Name - " + ChatColor.YELLOW + p.getName());
		if (talent == null) {
			lore.add(ChatColor.WHITE + "Talent - " + ChatColor.YELLOW + "None");
		}
		else {
			lore.add(ChatColor.WHITE + "Talent - " + ChatColor.YELLOW + talent);
		}
		
		skullMeta.setLore(lore);
		skullMeta.setOwner(p.getName());
		item.setItemMeta(skullMeta);
		
		Main.masterMenuInventory.setItem(22, item);
		
		
		
		// Talent Menu item
		item.setType(Material.END_CRYSTAL);
		meta.setDisplayName(ChatColor.RED + "Talents");
		lore.clear();
		if (talent == null) {
			lore.add(ChatColor.WHITE + "Current Talent - " + ChatColor.YELLOW + "None");
		}
		else {
			lore.add(ChatColor.WHITE + "Current Talent - " + ChatColor.YELLOW + talent);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.masterMenuInventory.setItem(21, item);
		
		
		// Skills Menu item
		item.setType(Material.BLACK_GLAZED_TERRACOTTA);
		meta.setDisplayName(ChatColor.AQUA + "Skills");
		lore.clear();
		lore.add(ChatColor.WHITE + "Combat Points - " + ChatColor.YELLOW + Main.combatPointsTracker.get(p.getUniqueId().toString()));
		lore.add(ChatColor.WHITE + "Mining Points - " + ChatColor.YELLOW + Main.miningPointsTracker.get(p.getUniqueId().toString()));
		lore.add(ChatColor.WHITE + "Farming Points - " + ChatColor.YELLOW + Main.farmingPointsTracker.get(p.getUniqueId().toString()));
		lore.add(ChatColor.WHITE + "Enchanting Points - " + ChatColor.YELLOW + Main.enchantingPointsTracker.get(p.getUniqueId().toString()));
		lore.add(ChatColor.WHITE + "Alchemy Points - " + ChatColor.YELLOW + Main.alchemyPointsTracker.get(p.getUniqueId().toString()));
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.masterMenuInventory.setItem(23, item);
		
		
		// Faction Storage item
		item.setType(Material.CHEST);
		meta.setDisplayName(ChatColor.YELLOW + "Faction Storage");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.masterMenuInventory.setItem(31, item);
		
		
		// close menu button
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.RED + "Close Menu");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.masterMenuInventory.setItem(49, item);
				
	}
	
	
}
