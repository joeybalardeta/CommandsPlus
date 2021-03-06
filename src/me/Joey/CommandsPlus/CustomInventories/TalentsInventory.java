package me.Joey.CommandsPlus.CustomInventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class TalentsInventory {
	public static void createTalentSelectionInventory() {
		InventoryManager.talentInventory = Bukkit.createInventory(null, 54, ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.DARK_GRAY + " | Talents");
		
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		
		for (int i = 0; i < 54; i++) {
			InventoryManager.talentInventory.setItem(i, item);
		}
		
		
		// Avian class item
		item = new ItemStack(Material.FEATHER);
		meta.setDisplayName(ChatColor.GOLD + "Avian");
		List<String> lore = new ArrayList <>();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Avians are well known for their wind based");
		lore.add(ChatColor.YELLOW + "abilities and aerial prowess in battle.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.GOLD + "Angel Wings" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you always have an Elytra equipped.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.GREEN + "Blessed Wind" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take no fall damage and");
		lore.add(ChatColor.YELLOW + "regenerate health at high altitudes.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.AQUA + "Hurricane Momentum" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you deal 60% more");
		lore.add(ChatColor.YELLOW + "damage while flying.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.GRAY + "Head Room" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you become weak and slow while at");
		lore.add(ChatColor.YELLOW + "low altitudes.");
		lore.add(ChatColor.DARK_GREEN + "Low Defense" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you cannot equip chestplates and"); 
		lore.add(ChatColor.YELLOW + "you have 2 less hearts of health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(18, item);
		
		// Pyrokinetic class item
		item.setType(Material.BLAZE_POWDER);
		meta.setDisplayName(ChatColor.DARK_RED + "Pyrokinetic");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Born in the nether, Pyrokinetics have harnessed");
		lore.add(ChatColor.YELLOW + "the power of fire to fuel themselves in battle.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.LIGHT_PURPLE + "Obsidian Skin" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you're immune to fire.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.RED + "Fire Storm" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you deal 30% more damage while on fire.");
		lore.add(ChatColor.YELLOW + "(does not stack with strength)");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.DARK_RED + "Blazing Fists" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you have a small chance of sending");
		lore.add(ChatColor.YELLOW + "out a fiery explosion when meleeing enemies.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.BLUE + "Water" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take more damage in water and rain.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(19, item);
		
		// Hydrokinetic class item
		item.setType(Material.COD);
		meta.setDisplayName(ChatColor.BLUE + "Hydrokinetic");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Originating from the depths of the sea, Hydrokinetics have");
		lore.add(ChatColor.YELLOW + "gained the ocean's favor and can command its power at will.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.AQUA + "Conduit Flux" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you have permanent Conduit Power.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.DARK_AQUA + "Cyclone" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you swim extremely fast.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.DARK_RED + "Fire" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take more damage from fire.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(20, item);
		
		// Frostbender class item
		item.setType(Material.BLUE_ICE);
		meta.setDisplayName(ChatColor.AQUA + "Frostbender");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Frostbenders are often described as lone wanderers.");
		lore.add(ChatColor.YELLOW + "Not much is known about them, only one thing for certain.");
		lore.add(ChatColor.YELLOW + "Don't let them freeze you.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.LIGHT_PURPLE + "Cryostasis Regeneration" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "when you are heavily");
		lore.add(ChatColor.YELLOW + "wounded, gain a protective layer of ice and regeneration.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.DARK_PURPLE + "Black Ice" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you have the ability to freeze others");
		lore.add(ChatColor.YELLOW + "at will.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.DARK_RED + "Fire" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take more damage from fire.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(21, item);
		
		// Terran class item
		item.setType(Material.GRASS_BLOCK);
		meta.setDisplayName(ChatColor.DARK_GREEN + "Terran");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Terrans are known for their ground based");
		lore.add(ChatColor.YELLOW + "abilities and down to earth personalities.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.DARK_GREEN + "Terraportation" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can teleport");
		lore.add(ChatColor.YELLOW + "vertically through the ground.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.GRAY + "Tough Skin" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you have permanent Resistance I.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.DARK_RED + "Geothermic Regeneration" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you gain");
		lore.add(ChatColor.YELLOW + "regeneration while deep underground.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.DARK_AQUA + "Heights" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take 30% more fall damage.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(22, item);
		
		// Biokinetic class item
		item.setType(Material.RED_TULIP);
		meta.setDisplayName(ChatColor.GREEN + "Biokinetic");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Biokinetics have been praised throughout history");
		lore.add(ChatColor.YELLOW + "for their extraordinary healing abilities");
		lore.add(ChatColor.YELLOW + "and loyalty to their allies.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.AQUA + "Calming touch" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you heal horses while");
		lore.add(ChatColor.YELLOW + "riding them.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.RED + "Mending Aura" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can heal allies with your");
		lore.add(ChatColor.YELLOW + "Arcane Crystal.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.DARK_PURPLE + "Arcane Surge" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "area of effect ability");
		lore.add(ChatColor.YELLOW + "that gives all enemies weakness.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.DARK_GREEN + "Poison" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take more damage from poison sources.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(23, item);
		
		// Enderian class item
		item.setType(Material.ENDER_PEARL);
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Enderian");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Enderians are quiet beings, often traveling");
		lore.add(ChatColor.YELLOW + "with a tight pack of trusted friends.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.LIGHT_PURPLE + "Dimensional Manipulation" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you don't");
		lore.add(ChatColor.YELLOW + "consume ender pearls when teleporting.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.DARK_PURPLE + "Voidwalker" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "all mobs ignore you.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.DARK_RED + "Dual-Phase Matter" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can't get hit by arrows.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.DARK_GREEN + "Antimatter Synthesis" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you regenerate health");
		lore.add(ChatColor.YELLOW + "in the end.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.BLUE + "Water" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take more damage in water and rain.");
		lore.add(ChatColor.DARK_GREEN + "Low Defense" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you have 2 less hearts of health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(24, item);
		
		
		// Cobble man class item
		item.setType(Material.COBBLESTONE);
		meta.setDisplayName(ChatColor.GRAY + "COBBLE MAN");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Forged from the depths of the underworld, Cobble Man");
		lore.add(ChatColor.YELLOW + "is known for his strength and infinite stores of cobble.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.RED + "Stonewall" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can only be killed by a pickaxe.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.DARK_PURPLE + "Black Magic" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can summon cobblestone at will.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.DARK_RED + "Imopster" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "killing someone grants you a minute of invisibility.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(25, item);
		
		// Sheriff class item
		item.setType(Material.STICK);
		meta.setDisplayName(ChatColor.GOLD + "SHERIFF");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Born in the west, the Sheriff stops at nothing to");
		lore.add(ChatColor.YELLOW + "ensure the safety of his people.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.GOLD + "Giddy Up" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can summon a mighty steed.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.DARK_RED + "The Jail" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can put players in The Jail.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(26, item);
				
		
		// close menu button
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.RED + "Close Menu");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(49, item);
		
		// back button
		item.setType(Material.ARROW);
		meta.setDisplayName(ChatColor.GOLD + "Go Back");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		InventoryManager.talentInventory.setItem(48, item);
		
	}
}
