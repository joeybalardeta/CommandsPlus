package me.Joey.CommandsPlus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemsPlus {
	
	// item declarations
	
	// custom weapons
	public static ItemStack superTNT;
	public static ItemStack thugnarsGlock;
	public static ItemStack dashSword;
	
	
	// custom armor
	public static ItemStack avianElytra;
	
	
	// custom tools
	public static ItemStack timberAxe;
	public static ItemStack redstonePickaxe;
	
	
	// custom potions
	public static ItemStack absorbtion;
	
	
	// custom talismans
	
	
	// custom miscellaneous
	public static ItemStack enchantedZombieFlesh;

	
	// custom crafts
	public static ItemStack bottleOfXP;
	public static ItemStack slimeBall;
	public static ItemStack quickPick;
	public static ItemStack telekinesisBook;
	public static ItemStack smeltingBook;
	
	
	// custom admin items
	public static ItemStack bonkStick;
	
	
	
	// item initializer (item creation function calls)
	public static void init() {
		// register custom enchants
		EnchantmentsPlus.register();
		
		// custom weapons
		createThugnarsGlock();
		createDashSword();
		
		// custom armor
		createAvianElytra();
		
		// custom tools
		createTimberAxe();
		createRestonePickaxe();
		
		
		// custom potions

		
		
		// custom talismans
		
		
		// custom miscellaneous
		createEnchantedZombieFlesh();

		
		// custom crafts
		createBottleOfXP();
		createSlimeBall();
		createTelekinesisBook();
		createSmeltingBook();
		
		
		// custom admin items
		createBonkStick();
		
		
	}

	// item creation functions
	
	// custom weapons
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
		thugnarsGlock = item;
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
		dashSword = item;
	}
	
	
	// custom armor
	public static void createAvianElytra() {
		// create item
		ItemStack item = new ItemStack(Material.ELYTRA, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable(true);
		meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
		meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		
		item.setItemMeta(meta);
		
		
		// set ItemStack item to the item that was created in this function
		avianElytra = item;
	}
	
	
	// custom tools
	
	public static void createTimberAxe() {
		// create item
		ItemStack item = new ItemStack(Material.GOLDEN_AXE, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Timber Axe");
		List<String> lore = new ArrayList <>();
		lore.add(ChatColor.WHITE + "This axe can chop down entire");
		lore.add(ChatColor.WHITE + "trees in just a few swings!");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		
		item.setItemMeta(meta);
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("timber_axe"), item);
		SR.shape("OOO",
				 "OAO",
				 "OOO");
		
		SR.setIngredient('O', Material.OBSIDIAN);
		SR.setIngredient('A', Material.WOODEN_AXE);
		Bukkit.getServer().addRecipe(SR);
		
		
		// set ItemStack item to the item that was created in this function
		timberAxe = item;
	}
	
	public static void createRestonePickaxe() {
		// create item
		ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Redstone Pickaxe");
		List<String> lore = new ArrayList <>();
		lore.add(ChatColor.WHITE + "A redstone powered pickaxe capable");
		lore.add(ChatColor.WHITE + "of mining mutiple blocks at once!");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
		
		item.setItemMeta(meta);
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("redstone_pickaxe"), item);
		SR.shape("BBB",
				 "RSR",
				 "RSR");
		
		SR.setIngredient('B', Material.NETHERITE_BLOCK);
		SR.setIngredient('R', Material.REDSTONE_BLOCK);
		SR.setIngredient('S', Material.STICK);
		Bukkit.getServer().addRecipe(SR);
		
		
		// set ItemStack item to the item that was created in this function
		redstonePickaxe = item;
	}
	
	
	// custom potions
	
	
	// custom talismans
	
	
	// custom miscellaneous
	
	public static void createEnchantedZombieFlesh() {
		// create item
		ItemStack item = new ItemStack(Material.ROTTEN_FLESH, 1);
		
		
		// set item metadata
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Enchanted Zombie Flesh");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "This item hums with energy,");
		lore.add(ChatColor.LIGHT_PURPLE + "what power could it hold?");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		
		
		// create crafting recipe
		
		// shaped recipe
		ShapedRecipe SR = new ShapedRecipe(NamespacedKey.minecraft("enchanted_zombie_flesh"), item);
		SR.shape("RRR",
				 "RRR",
				 "RRR");
		
		SR.setIngredient('R', Material.ROTTEN_FLESH);
		Bukkit.getServer().addRecipe(SR);
		
		// shapeless recipe
		ShapelessRecipe SLR = new ShapelessRecipe(NamespacedKey.minecraft("enchanted_zombie_flesh_shapeless"), item);
		SLR.addIngredient(9, Material.ROTTEN_FLESH);
		Bukkit.getServer().addRecipe(SLR);
		
		
		// set ItemStack item to the item that was created in this function
		enchantedZombieFlesh = item;
	}
	
	
	// custom crafts
	
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
		bottleOfXP = item;
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
		slimeBall = item;
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
		telekinesisBook = item;
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
		smeltingBook = item;
	}
	
	
	// custom admin items
	
	public static void createBonkStick() {
		ItemStack item = new ItemStack(Material.STICK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Bonk Stick");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Knockback XXV");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.KNOCKBACK, 25, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		
		item.setItemMeta(meta);
		
		bonkStick = item;
	}
	
}
