package me.Joey.CommandsPlus.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class CustomCrafts {
	// initialize custom items
	public static void init() {
		createGunpowder();
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
