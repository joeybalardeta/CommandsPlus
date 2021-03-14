package me.Joey.CommandsPlus.CustomItems;

import org.bukkit.inventory.ItemStack;

import me.Joey.CommandsPlus.CustomEnchantments.EnchantmentsPlus;

public class ItemsPlus {
	// custom weapons
	public static ItemStack thugnarsGlock;
	public static ItemStack dashSword;
	public static ItemStack trackingBow;
	
	
	// custom armor
	
	
	
	// custom tools
	public static ItemStack timberAxe;
	public static ItemStack redstonePickaxe;
	public static ItemStack replantingHoe;
	
	
	// custom potions
	public static ItemStack absorptionPotion;
	public static ItemStack hastePotion;
	
	
	// custom crafts
	public static ItemStack bottleOfXP;
	public static ItemStack slimeBall;
	public static ItemStack quickPick;
	public static ItemStack gunpowder;
	public static ItemStack telekinesisBook;
	public static ItemStack smeltingBook;
	public static ItemStack experienceBook;
	
	
	// talent items
	public static ItemStack stasisCrystal;
	public static ItemStack arcaneCrystal;
	public static ItemStack avianElytra;
	public static ItemStack bonkStick;
	
	
	// custom miscellaneous
	public static ItemStack enchantedZombieFlesh;
	public static ItemStack obsidianInfusedWater;
	
	

	// intialize all custom items from respective classes
	public static void init() {
		// register custom enchants
		EnchantmentsPlus.register();

		
		// initialize custom weapons
		CustomWeapons.init();
		
		
		// initialize custom armor
		CustomArmor.init();
		
		// initialize custom tools
		CustomTools.init();
		
		
		// initialize custom potions
		CustomPotions.init();
		
		
		// initialize custom class items
		TalentItems.init();
		
		// initialize custom crafts
		CustomCrafts.init();
		
		// initialize custom miscellaneous
		CustomMiscellaneous.init();

		
		
		
		
	}
	
	
	
}
