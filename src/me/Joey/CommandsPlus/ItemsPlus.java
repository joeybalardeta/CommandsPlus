package me.Joey.CommandsPlus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemsPlus {
	
	// item declarations
	
	// custom weapons
	
	
	// custom tools
	
	
	// custom potions
	
	
	// custom talismans 
	
	
	
	// random
	public static ItemStack bonkStick;
	
	
	// item initializer
	public static void init() {
		createBonkStick();
	}

	
	
	
	public static void createBonkStick() {
		ItemStack item = new ItemStack(Material.STICK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Bonk Stick");
		List<String> lore = new ArrayList <>();
		lore.add("B O N K");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.KNOCKBACK, 25, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		
		item.setItemMeta(meta);
		
		bonkStick = item;
	}
}
