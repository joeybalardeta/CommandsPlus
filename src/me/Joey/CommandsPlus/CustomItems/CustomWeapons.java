package me.Joey.CommandsPlus.CustomItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CustomWeapons {

	// initialize custom items
	public static void init() {
		createThugnarsGlock();
		createDashSword();
		createTrackingBow();
	}
	
	
	// functions to create/style custom items
	public static void createThugnarsGlock() {
		// create item
		ItemStack item = new ItemStack(Material.LEVER, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Thugnar's Glock");
		
		item.setItemMeta(meta);
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("thugnars_glock"), item);
		SR.shape("NNN",
				 "NLN",
				 "NNN");
		
		SR.setIngredient('N', Material.NETHERITE_BLOCK);
		SR.setIngredient('L', Material.LEVER);
		Bukkit.getServer().addRecipe(SR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.thugnarsGlock = item;
	}
	
	public static void createDashSword() {
		// create item
		ItemStack item = new ItemStack(Material.GOLDEN_SWORD, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "Dash Sword");
		
		item.setItemMeta(meta);
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("dash_sword"), item);
		SR.shape("EEE",
				 "EGE",
				 "EEE");
		
		SR.setIngredient('E', Material.ENDER_PEARL);
		SR.setIngredient('G', Material.GOLDEN_SWORD);
		Bukkit.getServer().addRecipe(SR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.dashSword = item;
	}
	
	public static void createTrackingBow() {
		// create item
		ItemStack item = new ItemStack(Material.BOW, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Tracking Bow");
		List<String> lore = new ArrayList <>();
		lore.add(ChatColor.YELLOW + "Pretty self explanatory.");
		lore.add(ChatColor.WHITE + "");
		lore.add(ChatColor.GRAY + "Aimbot I");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("tracking_bow"), item);
		SR.shape("EEE",
				 "EBE",
				 "EEE");
		
		SR.setIngredient('E', Material.ENDER_PEARL);
		SR.setIngredient('B', Material.BOW);
		Bukkit.getServer().addRecipe(SR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.trackingBow = item;
	}
}
