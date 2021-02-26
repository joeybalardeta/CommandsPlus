package me.Joey.CommandsPlus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

// this class is for big functions used in the main loop
public class FunctionsPlus {
	public static boolean day(Server server, Player p) {
	    String worldName = p.getServer().getName();
	    long time = server.getWorld(worldName).getTime();

	    if(time > 0 && time < 12300) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	
	public static long getTime(Server server, Player p) {
	    String worldName = p.getWorld().getName();
	    long time = server.getWorld(worldName).getTime();


	    return time;

	}
	
	// player data fetch functions
	public static int getLevel(Player p) {
		return 0;
	}
	
	// get player direction
	public static void getPlayerDirectionCardinal(Player player) {
		double rotation = player.getLocation().getYaw() - 180;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            player.sendMessage("North");
        }
        if (22.5 <= rotation && rotation < 67.5) {
            player.sendMessage("North East");
        }
        if (67.5 <= rotation && rotation < 112.5) {
            player.sendMessage("East");
        }
        if (112.5 <= rotation && rotation < 157.5) {
            player.sendMessage("SouthEast");
        }
        if (157.5 <= rotation && rotation < 202.5) {
            player.sendMessage("South");
        }
        if (202.5 <= rotation && rotation < 247.5) {
            player.sendMessage("SouthWest");
        }
        if (247.5 <= rotation && rotation < 292.5) {
            player.sendMessage("West");
        }
        if (292.5 <= rotation && rotation < 337.5) {
            player.sendMessage("NorthWest");
        }
        if (337.5 <= rotation && rotation <= 360) {
            player.sendMessage("North");
        }
	}
	
	public static double getPlayerDirectionFloat(Player player) {
		double rotation = player.getLocation().getYaw() - 180;
        if (rotation < 0) {
            rotation += 360.0;
        }
        return rotation;
	}
	
	
	
	// calculate the player's level
	public static void calculateLevel(HumanEntity humanEntity) {
		Player p = (Player) humanEntity;
		
		Main.miningPointsTracker.get(p.getUniqueId().toString());
		Main.combatPointsTracker.get(p.getUniqueId().toString());
		Main.farmingPointsTracker.get(p.getUniqueId().toString());
		Main.alchemyPointsTracker.get(p.getUniqueId().toString());
		
		
	}
	
	public static void savePlayerData(Player playerSave, boolean isLeaving) {
		// save mining points
		Main.playerDataConfig.set("Users." + playerSave.getUniqueId() + ".stats" + ".miningPoints", Main.miningPointsTracker.get(playerSave.getUniqueId().toString()));
		
		
		// save combat points
		Main.playerDataConfig.set("Users." + playerSave.getUniqueId() + ".stats" + ".combatPoints", Main.combatPointsTracker.get(playerSave.getUniqueId().toString()));
		
		
		// save farming points
		Main.playerDataConfig.set("Users." + playerSave.getUniqueId() + ".stats" + ".farmingPoints", Main.farmingPointsTracker.get(playerSave.getUniqueId().toString()));
		
		
		// save enchanting points
		Main.playerDataConfig.set("Users." + playerSave.getUniqueId() + ".stats" + ".enchantingPoints", Main.enchantingPointsTracker.get(playerSave.getUniqueId().toString()));
		
		
		// save alchemy points
		Main.playerDataConfig.set("Users." + playerSave.getUniqueId() + ".stats" + ".alchemyPoints", Main.alchemyPointsTracker.get(playerSave.getUniqueId().toString()));
		
		
		// set data save cooldown
		Main.canSaveDataHashMap.put(playerSave.getUniqueId().toString(), false);
		try {
			Main.playerDataConfig.save(Main.playerData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // this is important to have when editing server files, otherwise nothing gets changed
		
		
		// removes HashMaps if player is leaving the server 
		if (isLeaving) {
			Main.miningPointsTracker.remove(playerSave.getUniqueId().toString());
			Main.combatPointsTracker.remove(playerSave.getUniqueId().toString());
			Main.farmingPointsTracker.remove(playerSave.getUniqueId().toString());
			Main.enchantingPointsTracker.remove(playerSave.getUniqueId().toString());
			Main.alchemyPointsTracker.remove(playerSave.getUniqueId().toString());
		}
		
		
		// calculates player's level
		calculateLevel(playerSave);
		
		
	}
	
	
	
	public static void createTalentSelectionInventory() {
		Main.talentInventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Pick a talent! Choose wisely!");
		
		
		ItemStack item = new ItemStack(Material.FEATHER);
		ItemMeta meta = item.getItemMeta();
		
		
		// Avian class item
		meta.setDisplayName(ChatColor.GOLD + "Avian");
		List<String> lore = new ArrayList <>();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Avians are well known for their wind based");
		lore.add(ChatColor.YELLOW + "abilities and aerial prowess in battle.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.GOLD + "Angel Wings" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you always have an Elytra equipped.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.GREEN + "Blessed Wind" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take no fall damage.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.AQUA + "Hurricane Momentum" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you deal double");
		lore.add(ChatColor.YELLOW + "damage while flying.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.GRAY + "Head Room" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you become weak and slow while under");
		lore.add(ChatColor.YELLOW + "low ceilings.");
		lore.add(ChatColor.DARK_GREEN + "Low Defense" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you cannot equip chestplates and"); 
		lore.add(ChatColor.YELLOW + "you have 2 less hearts of health.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.talentInventory.setItem(0, item);
		
		// Pyrokinetic class item
		item.setType(Material.BLAZE_POWDER);
		meta.setDisplayName(ChatColor.DARK_RED + "Pyrokinetic");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Born in the nether, Pyrokinetics have harnessed");
		lore.add(ChatColor.YELLOW + "the power of fire to fuel themselves in battle.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.LIGHT_PURPLE + "Obsidian Skin" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you're immune to fire and take");
		lore.add(ChatColor.YELLOW + "less damage in the Nether.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.RED + "Fire Storm" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you deal 50% more damage while on fire.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.BLUE + "Water" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take damage in water and rain.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.talentInventory.setItem(1, item);
		
		// Hydrokinetic class item
		item.setType(Material.PRISMARINE_SHARD);
		meta.setDisplayName(ChatColor.BLUE + "Hydrokinetic");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Originating from the depths of the sea, Hydrokinetics have");
		lore.add(ChatColor.YELLOW + "gained the ocean's favor and can command its power at will.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.AQUA + "Water Magic" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you swim faster and can't drown.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.DARK_AQUA + "Cyclone" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you deal 30% more damage in water.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.BLUE + "Hydrokinetic Regeneration" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you regenerate");
		lore.add(ChatColor.YELLOW + "health in water.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.DARK_RED + "Fire" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take extra damage in the Nether and from fire.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.talentInventory.setItem(2, item);
		
		// Frostbender class item
		item.setType(Material.BLUE_ICE);
		meta.setDisplayName(ChatColor.AQUA + "Frostbender");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Frostbenders are often described as lone wanderers.");
		lore.add(ChatColor.YELLOW + "Not much is known about them, only one thing for certain.");
		lore.add(ChatColor.YELLOW + "Don't let them freeze you.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.LIGHT_PURPLE + "Cryostasis Regeneration" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "when you are heavily");
		lore.add(ChatColor.YELLOW + "wounded, gain a protective layer of ice and regeneration.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.WHITE + "Snowshoes" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you move faster on ice and snow.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.DARK_PURPLE + "Black Ice" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you have the ability to freeze others");
		lore.add(ChatColor.YELLOW + "at will.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.DARK_RED + "Fire" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take extra damage in the Nether and from fire.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.talentInventory.setItem(3, item);
		
		// Cobble man class item
		item.setType(Material.COBBLESTONE);
		meta.setDisplayName(ChatColor.GRAY + "COBBLE MAN");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Forged from the depths of the underworld, Cobble Man");
		lore.add(ChatColor.YELLOW + "is known for his strength and infinite stores of cobble.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.DARK_RED + "Stonewall" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can only be killed by a pickaxe.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.DARK_PURPLE + "Black Magic" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can summon cobblestone at will.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.talentInventory.setItem(18, item);
		
		
		// close menu button
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.RED + "Close Menu");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.talentInventory.setItem(26, item);
		
	}
	
	
	
	public static Entity getNearestEntityInSight(Player p, int range){
		List<Entity> entities = p.getNearbyEntities(range,range,range);
		
		Iterator<Entity> iterator = entities.iterator(); 
		while(iterator.hasNext()){
			Entity next = iterator.next(); 
			if(!(next instanceof LivingEntity) || next == p){
				iterator.remove();
			}
		}
		
		List<Block> sight = p.getLineOfSight((Set<Material>) null, range);
		for(Block block : sight){
			if(block.getType() != Material.AIR){
				break;
			}

			for(Entity entity : entities){
				if(entity.getLocation().distance(p.getEyeLocation()) <= range && (entity.getLocation().distance(block.getLocation()) <= 2 || entity.getLocation().add(0,1,0).distance(block.getLocation()) <= 1)) {
					return entity;
				}
			}
		}


		return null;
	}
	
	
	@SuppressWarnings("deprecation")
	public static ItemStack getPlayerHead(String player) {
		boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD");
		
		Material type = Material.matchMaterial(isNewVersion? "PLAYER_HEAD" : "SKULL_ITEM");
		ItemStack item = new ItemStack(type, 1);
		
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		
		meta.setOwner(player);
		meta.setDisplayName(ChatColor.DARK_PURPLE + player + "'s " + ChatColor.WHITE + "head");
		
		item.setItemMeta(meta);
		
		return item;
	}
}
