package me.Joey.CommandsPlus.CustomItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.Joey.CommandsPlus.CustomEnchantments.EnchantmentsPlus;
import net.md_5.bungee.api.ChatColor;

public class CustomEnchantedBooks {
	// initialize custom items
		public static void init() {
			createTelekinesisBook();
			createSmeltingBook();
			createExperienceBook();
		}
		
		
		// functions to create/style custom items
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
}
