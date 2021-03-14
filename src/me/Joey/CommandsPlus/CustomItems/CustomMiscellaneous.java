package me.Joey.CommandsPlus.CustomItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CustomMiscellaneous {
	// initialize custom items
	public static void init() {
		createEnchantedZombieFlesh();
		createObsidianInfusedWater();
	}
	
	
	// functions to create/style custom items
	public static void createEnchantedZombieFlesh() {
		// create item
		ItemStack item = new ItemStack(Material.ROTTEN_FLESH, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Enchanted Zombie Flesh");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "This item hums with energy,");
		lore.add(ChatColor.LIGHT_PURPLE + "what power could it hold?");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("enchanted_zombie_flesh"), item);
		SR.shape("RRR",
				 "RRR",
				 "RRR");
		
		SR.setIngredient('R', Material.ROTTEN_FLESH);
		Bukkit.getServer().addRecipe(SR);
		
		// shapeless recipe
		ShapelessRecipe SLR = new ShapelessRecipe(NamespacedKey.minecraft("enchanted_zombie_flesh_shapeless"), item);
		SLR.addIngredient(9, Material.ROTTEN_FLESH);
		Bukkit.getServer().addRecipe(SLR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.enchantedZombieFlesh = item;
	}
	
	public static void createObsidianInfusedWater() {
		// create item
		ItemStack item = new ItemStack(Material.WATER_BUCKET, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Obsidian Infused Water");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "This water can be placed in the Nether.");
		lore.add(ChatColor.RED + "SINGLE USE");
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("obsidian_infused_water"), item);
		SR.shape("OOO",
				 "OWO",
				 "OOO");
		
		SR.setIngredient('W', Material.WATER_BUCKET);
		SR.setIngredient('O', Material.OBSIDIAN);
		Bukkit.getServer().addRecipe(SR);

		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.obsidianInfusedWater = item;
	}
}
