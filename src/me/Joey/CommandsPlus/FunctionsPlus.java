package me.Joey.CommandsPlus;

import java.io.IOException;

import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

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
}
