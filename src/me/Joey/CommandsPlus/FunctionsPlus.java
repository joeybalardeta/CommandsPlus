package me.Joey.CommandsPlus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
	public void getPlayerDirection(Player player) {
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
		meta.setDisplayName(ChatColor.AQUA + "Avian");
		List<String> lore = new ArrayList <>();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Avians are well known for their jumping");
		lore.add(ChatColor.YELLOW + "abilities and aerial prowess in battle.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.GREEN + "Double Jump" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can jump one extra time in midair!");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.AQUA + "Hurricane Momentum" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you deal extra damage while flying.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.talentInventory.setItem(0, item);
		
		// Pyrokinetic class item
		item.setType(Material.BLAZE_POWDER);
		meta.setDisplayName(ChatColor.RED + "Pyrokinetic");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Born in the nether, Pyrokinetics have harnessed");
		lore.add(ChatColor.YELLOW + "the ability to excite atoms");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.LIGHT_PURPLE + "Obsidian Skin" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you're immune to lava and fire damage.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.DARK_RED + "Fire Storm" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you deal extra damage while on fire.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Weaknesses:");
		lore.add(ChatColor.AQUA + "Water" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you take damage in water and rain.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.talentInventory.setItem(1, item);
		
		// Cobble Man class item
		item.setType(Material.COBBLESTONE);
		meta.setDisplayName(ChatColor.GRAY + "COBBLE MAN");
		lore.clear();
		lore.add(ChatColor.WHITE + "Description:");
		lore.add(ChatColor.YELLOW + "Forged from the depths of the underworld, Cobble Man");
		lore.add(ChatColor.YELLOW + "is known for his strength and infinite stores of cobble.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Abilities:");
		lore.add(ChatColor.WHITE + "(" + ChatColor.BLUE + "Passive" + ChatColor.WHITE + ") " + ChatColor.GRAY + "Stonewall" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can only be killed by a pickaxe.");
		lore.add(ChatColor.WHITE + "(" + ChatColor.RED + "Active" + ChatColor.WHITE + ") " + ChatColor.DARK_PURPLE + "Black Magic" + ChatColor.WHITE + " - " + ChatColor.YELLOW + "you can summon cobblestone at will.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		Main.talentInventory.setItem(2, item);
		
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
}
