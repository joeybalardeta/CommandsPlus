package me.Joey.CommandsPlus.CustomInventories;

import org.bukkit.inventory.Inventory;

import me.Joey.CommandsPlus.CustomInventories.CategorizedCraftInventories.*;

public class InventoryManager {
	// declare inventories
	
	// main menu
	public static Inventory masterMenuInventory; // master menu doesn't need to be initialized since it gets initialized for the player when they use /menu
	public static Inventory talentInventory;
	public static Inventory masterCraftsInventory;
	public static Inventory factionOptionsInventory;
	
	
	// categorized crafting menus
	public static Inventory weaponCraftsInventory;
	public static Inventory toolCraftsInventory;
	public static Inventory armorCraftsInventory;
	public static Inventory potionCraftsInventory;
	public static Inventory miscellaneousCraftsInventory;
	public static Inventory enchantedBookCraftsInventory;
	public static Inventory customCraftsInventory;
	public static Inventory talentItemCraftsInventory;
	
	
	// recipe crafting inventory
	public static Inventory recipeInventory;
	
	
	
	
	public static void init() {
		TalentsInventory.createTalentSelectionInventory();
		MasterCraftsInventory.createMasterCraftsInventory();
		
		// custom craft inventories
		WeaponCraftsInventory.createWeaponCraftsInventory();
		ToolCraftsInventory.createToolCraftsInventory();
		ArmorCraftsInventory.createArmorCraftsInventory();
		PotionCraftsInventory.createPotionCraftsInventory();
		EnchantedBookCraftsInventory.createEnchantedBookCraftsInventory();
		CustomCraftsInventory.createCustomCraftsInventory();
		TalentItemCraftsInventory.createTalentItemCraftsInventory();
		MiscellaneousCraftsInventory.createMiscellaneousCraftsInventory();
	}
}
