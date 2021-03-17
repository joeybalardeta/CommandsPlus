package me.Joey.CommandsPlus.CustomInventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class RecipeInventoryCreator {
	public static Inventory createRecipeInventory(String category, ItemStack masterItem) {
		Inventory recipeInventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + category + " | " + masterItem.getItemMeta().getDisplayName());
		
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		
		for (int i = 0; i < 54; i++) {
			recipeInventory.setItem(i, item);
		}
		
		ShapedRecipe shaped = null;
		
		for(Recipe recipe : Bukkit.getServer().getRecipesFor(masterItem)) {
			if(recipe instanceof ShapedRecipe) {
		        shaped = (ShapedRecipe)recipe;
		        break;
		    }
		}
		
		// set crafting item (1, 3)
		if (shaped.getIngredientMap().get('a') != null) {
			item = shaped.getIngredientMap().get('a');
		}
		else {
			item = new ItemStack(Material.AIR);
		}
		
		recipeInventory.setItem(10, item);
		
		
		// set crafting item (2, 3)
		if (shaped.getIngredientMap().get('b') != null) {
			item = shaped.getIngredientMap().get('b');
		}
		else {
			item = new ItemStack(Material.AIR);
		}
		
		recipeInventory.setItem(11, item);
		
		
		// set crafting item (3, 3)
		if (shaped.getIngredientMap().get('c') != null) {
			item = shaped.getIngredientMap().get('c');
		}
		else {
			item = new ItemStack(Material.AIR);
		}
		
		recipeInventory.setItem(12, item);
		
		
		// set crafting item (1, 2)
		if (shaped.getIngredientMap().get('d') != null) {
			item = shaped.getIngredientMap().get('d');
		}
		else {
			item = new ItemStack(Material.AIR);
		}
		
		recipeInventory.setItem(19, item);
		
		
		// set crafting item (2, 2)
		if (shaped.getIngredientMap().get('e') != null) {
			item = shaped.getIngredientMap().get('e');
		}
		else {
			item = new ItemStack(Material.AIR);
		}
		
		recipeInventory.setItem(20, item);
		
		
		// set crafting item (3, 2)
		if (shaped.getIngredientMap().get('f') != null) {
			item = shaped.getIngredientMap().get('f');
		}
		else {
			item = new ItemStack(Material.AIR);
		}
		
		recipeInventory.setItem(21, item);
		
		// set crafting item (1, 1)
		if (shaped.getIngredientMap().get('g') != null) {
			item = shaped.getIngredientMap().get('g');
		}
		else {
			item = new ItemStack(Material.AIR);
		}
		
		recipeInventory.setItem(28, item);
		
		
		// set crafting item (2, 1)
		if (shaped.getIngredientMap().get('h') != null) {
			item = shaped.getIngredientMap().get('h');
		}
		else {
			item = new ItemStack(Material.AIR);
		}
		
		recipeInventory.setItem(29, item);
		
		
		// set crafting item (3, 1)
		if (shaped.getIngredientMap().get('i') != null) {
			item = shaped.getIngredientMap().get('i');
		}
		else {
			item = new ItemStack(Material.AIR);
		}
		
		recipeInventory.setItem(30, item);
		
		
		item = new ItemStack(Material.CRAFTING_TABLE);
		recipeInventory.setItem(23, item);
		
		item = new ItemStack(masterItem);
		recipeInventory.setItem(25, item);
		
		// close menu button
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.RED + "Close Menu");
		item.setItemMeta(meta);
		recipeInventory.setItem(49, item);
		
		// back button
		item.setType(Material.ARROW);
		meta.setDisplayName(ChatColor.GOLD + "Go Back");
		item.setItemMeta(meta);
		recipeInventory.setItem(48, item);
		
		
		return recipeInventory;
	}
}
