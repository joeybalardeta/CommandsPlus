package me.Joey.CommandsPlus;

import org.bukkit.Server;
import org.bukkit.entity.Player;

// this class is for extra functions I might want to use at some point 
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
}
