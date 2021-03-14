package me.Joey.CommandsPlus.CustomItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;

public class CustomPotions {
	// initialize custom items
	public static void init() {
		createAbsorptionPotion();
		createHastePotion();
	}
	
	// functions to create/style custom items
	public static void createAbsorptionPotion() {
		ItemStack item = new ItemStack(Material.POTION);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "Potion of Absorption");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.BLUE + "Absorption II (10:00)");
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		
		PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
		potionMeta.setColor(Color.fromRGB(255, 157, 0));
		potionMeta.setBasePotionData(new PotionData(PotionType.AWKWARD));
		potionMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		potionMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(potionMeta);
		
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("absorption_potion"), item);
		SR.shape("GGG",
				 "APA",
				 "WWW");
		
		SR.setIngredient('P', Material.POTION);
		SR.setIngredient('A', Material.APPLE);
		SR.setIngredient('G', Material.GOLD_BLOCK);
		SR.setIngredient('W', Material.NETHER_WART);
		Bukkit.getServer().addRecipe(SR);
		
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.absorptionPotion = item;
	}
	
	public static void createHastePotion() {
		ItemStack item = new ItemStack(Material.POTION);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "Potion of Haste");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.BLUE + "Haste II (3:00)");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		
		PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
		potionMeta.setColor(Color.fromRGB(255, 197, 38));
		potionMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		potionMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(potionMeta);
		
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("haste_potion"), item);
		SR.shape("RRR",
				 "SPS",
				 "WWW");
		
		SR.setIngredient('P', Material.POTION);
		SR.setIngredient('R', Material.REDSTONE);
		SR.setIngredient('S', Material.SUGAR);
		SR.setIngredient('W', Material.NETHER_WART);
		Bukkit.getServer().addRecipe(SR);
		
		// set ItemStack item to the item that was created in this function
		ItemsPlus.hastePotion = item;
	}
}
