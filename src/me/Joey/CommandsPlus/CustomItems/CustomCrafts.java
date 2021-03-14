package me.Joey.CommandsPlus.CustomItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.Joey.CommandsPlus.CustomEnchantments.EnchantmentsPlus;
import net.md_5.bungee.api.ChatColor;

public class CustomCrafts {
	// initialize custom items
	public static void init() {
		createGunpowder();
		createTelekinesisBook();
		createSmeltingBook();
		createExperienceBook();
	}
	
	
	// functions to create/style custom items
	public static void createBottleOfXP() {
		// create item
		ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("bottle_of_xp"), item);
		SR.shape("LLL",
				 "LBL",
				 "LLL");
		
		SR.setIngredient('L', Material.LAPIS_LAZULI);
		SR.setIngredient('B', Material.GLASS_BOTTLE);
		Bukkit.getServer().addRecipe(SR);
		
		// shapeless recipe
		ShapelessRecipe SLR = new ShapelessRecipe(NamespacedKey.minecraft("bottle_of_xp_shapeless"), item);
		SLR.addIngredient(8, Material.LAPIS_LAZULI);
		SLR.addIngredient(1, Material.GLASS_BOTTLE);
		Bukkit.getServer().addRecipe(SLR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.bottleOfXP = item;
	}
	
	
	public static void createSlimeBall() {
		// create item
		ItemStack item = new ItemStack(Material.SLIME_BALL, 1);
		
		
		// create crafting recipe
		
		// shapeless recipe
		ShapelessRecipe SLR = new ShapelessRecipe(NamespacedKey.minecraft("slime_ball_shapeless"), item);
		SLR.addIngredient(1, Material.WHEAT);
		SLR.addIngredient(1, Material.LIME_DYE);
		Bukkit.getServer().addRecipe(SLR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.slimeBall = item;
	}
	
	
	public static void createTelekinesisBook() {
		// create item
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Telekinesis I");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		item.addUnsafeEnchantment(EnchantmentsPlus.TELEKINESIS, 1);
		
		// set enchantment
		
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("telekinesis_book"), item);
		SR.shape(" S ",
				 "SBS",
				 " S ");
		
		SR.setIngredient('B', Material.BOOK);
		SR.setIngredient('S', Material.SLIME_BALL);
		Bukkit.getServer().addRecipe(SR);
		
		
		 
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.telekinesisBook = item;
	}
	
	public static void createSmeltingBook() {
		// create item
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Smelting I");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		item.addUnsafeEnchantment(EnchantmentsPlus.SMELTING, 1);
		
		// set enchantment
		
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("smelting_book"), item);
		SR.shape("CCC",
				 "CBC",
				 "CCC");
		
		SR.setIngredient('B', Material.BOOK);
		SR.setIngredient('C', Material.COAL_BLOCK);
		Bukkit.getServer().addRecipe(SR);
		
		
		 
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.smeltingBook = item;
	}
	
	public static void createExperienceBook() {
		// create item
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Experience III");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		item.addUnsafeEnchantment(EnchantmentsPlus.EXPERIENCE, 3);
		
		// set enchantment
		
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("experience_book"), item);
		SR.shape("LLL",
				 "LBL",
				 "LLL");
		
		SR.setIngredient('B', Material.BOOK);
		SR.setIngredient('L', Material.LAPIS_BLOCK);
		Bukkit.getServer().addRecipe(SR);
		
		
		 
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.experienceBook = item;
	}
	
	
	public static void createGunpowder() {
		// create item
		ItemStack item = new ItemStack(Material.GUNPOWDER, 3);
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("gunpowder_custom"), item);
		SR.shape("RRR",
				 "FFF",
				 "CCC");
		
		SR.setIngredient('R', Material.REDSTONE);
		SR.setIngredient('F', Material.FLINT);
		SR.setIngredient('C', Material.COAL);
		Bukkit.getServer().addRecipe(SR);
		
		// shapeless recipe
		ShapelessRecipe SLR = new ShapelessRecipe(NamespacedKey.minecraft("gunpowder_custom_shapeless"), item);
		SLR.addIngredient(3, Material.REDSTONE);
		SLR.addIngredient(3, Material.FLINT);
		SLR.addIngredient(3, Material.COAL);
		Bukkit.getServer().addRecipe(SLR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.gunpowder = item;
	}
}
