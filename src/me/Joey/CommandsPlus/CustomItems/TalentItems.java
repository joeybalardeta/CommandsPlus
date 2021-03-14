package me.Joey.CommandsPlus.CustomItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Joey.CommandsPlus.FunctionsPlus;
import net.md_5.bungee.api.ChatColor;

public class TalentItems {
	// initialize custom items
	public static void init() {
		createAvianElytra();
		createStasisCrystal();
		createArcaneCrystal();
		createBonkStick();
	}
	
	
	// functions to create/style custom items
	public static void createAvianElytra() {
		// create item
		ItemStack item = new ItemStack(Material.ELYTRA, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable(true);
		meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
		meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		
		item.setItemMeta(meta);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.avianElytra = item;
	}
	
	public static void createStasisCrystal() {
		ItemStack item = FunctionsPlus.getPlayerHead("Tecno_");
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Stasis Crystal");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "The fabled Stasis Crystal...");
		lore.add(ChatColor.LIGHT_PURPLE + "what power could it hold?");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.stasisCrystal = item;
	}
	
	
	public static void createArcaneCrystal() {
		ItemStack item = FunctionsPlus.getPlayerHead("Chaochris");
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Arcane Crystal");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "The fabled Arcane Crystal...");
		lore.add(ChatColor.LIGHT_PURPLE + "what power could it hold?");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.arcaneCrystal = item;
	}
	
	public static void createBonkStick() {
		ItemStack item = new ItemStack(Material.STICK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Bonk Stick");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Punish them for their actions.");
		lore.add(ChatColor.YELLOW + "Send them to fart castle.");
		meta.setLore(lore);
		
		
		item.setItemMeta(meta);
		
		ItemsPlus.bonkStick = item;
	}
}
