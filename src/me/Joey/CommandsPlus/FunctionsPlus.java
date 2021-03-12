package me.Joey.CommandsPlus;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import me.Joey.CommandsPlus.Particles.ParticleData;
import me.Joey.CommandsPlus.Particles.ParticleEffects;
import net.md_5.bungee.api.ChatColor;

// this class is for big functions used in the main loop
public class FunctionsPlus {
	static Plugin plugin;
	
	
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
	
	// get player level
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
		// save player deaths
		Main.playerDataConfig.set("Users." + playerSave.getUniqueId() + ".stats" + ".deaths", Main.playerDeathsHashMap.get(playerSave.getUniqueId().toString()));
		
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
	
	public static Entity getNearestEntityInSight(Player p, int range, int scanRange){
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
			if(block.getType() != Material.AIR && block.getType() != Material.CAVE_AIR && block.getType() != Material.WATER && block.getType() != Material.LAVA){
				break;
			}

			for(Entity entity : entities){
				if(entity.getLocation().distance(p.getEyeLocation()) <= range && (entity.getLocation().distance(block.getLocation()) <= scanRange || entity.getLocation().add(0,1,0).distance(block.getLocation()) <= scanRange)) {
					return entity;
				}
			}
		}


		return null;
	}
	
	
	public static void playSound(Player p, String soundType) {
		if (soundType.equals("actionDenied")){
			p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0f);
		}
		
		else if (soundType.equals("actionAccepted")){
			
		}
	}
	
	public static int getAmount(Player arg0, ItemStack arg1) {
        if (arg1 == null)
            return 0;
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack slot = arg0.getInventory().getItem(i);
            if (slot == null || !slot.isSimilar(arg1))
                continue;
            amount += slot.getAmount();
        }
        return amount;
    }
	
	// set a player's rank
	public static void setRank(Player p, Player target, String rank) { 
		Main.playerRankHashMap.put(target.getUniqueId().toString(), "" + rank);
		Main.playerDataConfig.set("Users." + target.getUniqueId() + ".stats" + ".rank", rank);
		if (p != null) {
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Set rank of " + ChatColor.BLUE + target.getName() + ChatColor.WHITE + " to " + ChatColor.RED + rank);
			target.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Your rank was changed to " + ChatColor.RED + rank);
		}
	}
	
	
	// restore all talent health sets and particles
	@SuppressWarnings("deprecation")
	public static void restoreTalentEffects(Player online, String talent) {
		ParticleData particle = new ParticleData(online.getUniqueId());
		ParticleEffects trails = new ParticleEffects(online);
		
		if (particle.hasID()) {
			particle.endTask();
			particle.removeID();
		}
		
		if (talent == null) {
			online.setMaxHealth(20);
		}
		
		if (talent.equals("Avian")) {
			online.setMaxHealth(16);
			online.getInventory().setChestplate(ItemsPlus.avianElytra);
		}
		
		if (talent.equals("Pyrokinetic")) {
			online.setMaxHealth(20);
			trails.startPyrokineticParticles();
		}
		
		if (talent.equals("Hydrokinetic")) {
			online.setMaxHealth(20);
		}
		
		if (talent.equals("Frostbender")) {
			online.setMaxHealth(20);
		}
		
		if (talent.equals("Terran")) {
			online.setMaxHealth(20);
		}
		
		if (talent.equals("Biokinetic")) {
			online.setMaxHealth(20);
		}
		
		if (talent.equals("Enderian")) {
			online.setMaxHealth(16);
			trails.startEnderianParticles();
		}
		
		if (talent.equals("SHERIFF")) {
			online.setMaxHealth(40);
		}
		
		if (talent.equals("COBBLE MAN")) {
			online.setMaxHealth(40);
		}
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
