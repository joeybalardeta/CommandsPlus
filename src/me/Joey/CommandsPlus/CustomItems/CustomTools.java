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
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CustomTools {
	// initialize custom items
	public static void init() {
		createTimberAxe();
		createRestonePickaxe();
		createReplantingHoe();
	}
	
	// functions to create/style custom items
	public static void createTimberAxe() {
		// create item
		ItemStack item = new ItemStack(Material.GOLDEN_AXE, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Timber Axe");
		List<String> lore = new ArrayList <>();
		lore.add(ChatColor.WHITE + "This axe can chop down entire");
		lore.add(ChatColor.WHITE + "trees in just a few swings!");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		
		item.setItemMeta(meta);
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("timber_axe"), item);
		SR.shape("OOO",
				 "OAO",
				 "OOO");
		
		SR.setIngredient('O', Material.OBSIDIAN);
		SR.setIngredient('A', Material.WOODEN_AXE);
		Bukkit.getServer().addRecipe(SR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.timberAxe = item;
	}
	
	public static void createRestonePickaxe() {
		// create item
		ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Redstone Pickaxe");
		List<String> lore = new ArrayList <>();
		lore.add(ChatColor.WHITE + "A redstone powered pickaxe capable");
		lore.add(ChatColor.WHITE + "of mining mutiple blocks at once!");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		
		item.setItemMeta(meta);
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("redstone_pickaxe"), item);
		SR.shape("BBB",
				 "RSR",
				 "RSR");
		
		SR.setIngredient('B', Material.NETHERITE_BLOCK);
		SR.setIngredient('R', Material.REDSTONE_BLOCK);
		SR.setIngredient('S', Material.STICK);
		Bukkit.getServer().addRecipe(SR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.redstonePickaxe = item;
	}
	
	public static void createReplantingHoe() {
		// create item
		ItemStack item = new ItemStack(Material.GOLDEN_HOE, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Replanting Hoe");
		List<String> lore = new ArrayList <>();
		lore.add(ChatColor.WHITE + "Replants harvested crops!");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		
		item.setItemMeta(meta);
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("replanting_hoe"), item);
		SR.shape("BBB",
				 "BHB",
				 "BBB");
		
		SR.setIngredient('H', Material.WOODEN_HOE);
		SR.setIngredient('B', Material.BONE_MEAL);
		Bukkit.getServer().addRecipe(SR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.replantingHoe = item;
	}
}
