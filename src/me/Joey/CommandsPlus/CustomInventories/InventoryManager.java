package me.Joey.CommandsPlus.CustomInventories;

import org.bukkit.inventory.Inventory;

import me.Joey.CommandsPlus.CustomInventories.CategorizedCraftInventories.WeaponCraftsInventory;

public class InventoryManager {
	// declare inventories
	
	// main menu
	public static Inventory masterMenuInventory; // master menu doesn't need to be initialized since it gets initialized for the player when they use /menu
	public static Inventory talentInventory;
	public static Inventory masterCraftsInventory;
	public static Inventory factionOptionsInventory;
	
	// categorized crafting menus
	public static Inventory weaponCraftsInventory;
	
	
	
	
	public static void init() {
		TalentsInventory.createTalentSelectionInventory();
		MasterCraftsInventory.createMasterCraftsInventory();
		WeaponCraftsInventory.createWeaponCraftsInventory();
	}
}
