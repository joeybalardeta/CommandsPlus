package me.Joey.CommandsPlus.CustomInventories;

import org.bukkit.inventory.Inventory;

public class InventoryManager {
	// declare inventories
	public static Inventory masterMenuInventory; // master menu doesn't need to be initialized since it gets initialized for the player when they use /menu
	public static Inventory talentInventory;
	public static Inventory masterCraftsInventory;
	public static Inventory factionOptionsInventory;
	
	
	
	public static void init() {
		TalentsInventory.createTalentSelectionInventory();
		MasterCraftsInventory.createMasterCraftsInventory();
	}
}
