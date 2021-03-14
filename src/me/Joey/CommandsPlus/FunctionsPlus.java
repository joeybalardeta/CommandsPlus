package me.Joey.CommandsPlus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.Joey.CommandsPlus.CustomItems.ItemsPlus;
import me.Joey.CommandsPlus.Particles.ParticleData;
import me.Joey.CommandsPlus.Particles.ParticleEffects;
import net.md_5.bungee.api.ChatColor;

// this class is for big functions used in the main loop
public class FunctionsPlus {
	
	public static void createBoard(Player player) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective obj = board.registerNewObjective("Scoreboard", "dummy", ChatColor.translateAlternateColorCodes('&', "&cCommands&4+"));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		String format = "M/d/yyyy";
		Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(format);
		
		Score score1 = obj.getScore("");
		score1.setScore(6);
		
		Score score2 = obj.getScore("Players Online: " + ChatColor.AQUA + Bukkit.getOnlinePlayers().size());
		score2.setScore(5);
		

		String territoryLookup = "X: " + player.getLocation().getChunk().getX() + ", Z: " + player.getLocation().getChunk().getZ();
		String territoryName = null;
		for (int i = 0; i < Main.chunkLocationsRAM.size(); i++) {
			if (Main.chunkLocationsRAM.get(i).equals(territoryLookup)) {
				territoryName = Main.chunkNamesRAM.get(i);
			}
		}
		if (territoryName == null) {
			territoryName = "Wilderness";
		}

		if (player.getWorld().getEnvironment() == Environment.NETHER){
			territoryName = "Nether";
		}
		Score score3 = obj.getScore("Territory: " + ChatColor.AQUA + territoryName);
		
		score3.setScore(4);
		
		Score score4 = obj.getScore("Deaths: " + ChatColor.AQUA + Main.playerDeathsHashMap.get(player.getUniqueId().toString()));
		score4.setScore(3);
		
		Score score5 = obj.getScore("");
		score5.setScore(2);
		
		Score score6 = obj.getScore(ChatColor.DARK_GRAY + ft.format(date));
		score6.setScore(1);
		
		
//		obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
//		Score deathsCounter = obj.getScore(ChatColor.AQUA + "");
//		deathsCounter.setScore(1);
		
		
		player.setScoreboard(board);
	}
	
	
	public static void setTabList(Player online) {
		String rank = Main.playerRankHashMap.get(online.getUniqueId().toString());
		online.setPlayerListName(ChatColor.WHITE + "[" + getRankColor(online) + rank + ChatColor.WHITE + "] " + online.getName());
	}
	
	
	public static ChatColor getRankColor(Player p) {
		String rank = Main.playerRankHashMap.get(p.getUniqueId().toString());
		
		
		
		if (rank == null) {
			return ChatColor.WHITE;
		}
		else if (rank.equals("Member")) {
			return ChatColor.BLUE;
		}
		else if (rank.equals("Streamer")) {
			return ChatColor.LIGHT_PURPLE;
		}
		else if (rank.equals("Admin")) {
			return ChatColor.RED;
		}
		else if (rank.equals("Superuser")) {
			return ChatColor.DARK_RED;
		}
		else if (rank.equals("Extremely Talented Gamer")) {
			return ChatColor.AQUA;
		}
		else if (rank.equals("Sheriff")) {
			return ChatColor.GOLD;
		}
		else if (rank.equals("Cobble Man")) {
			return ChatColor.GRAY;
		}
		
		return ChatColor.GREEN;
	}
	
	public static void loadChunkArrays() {
		ConfigurationSection claimedChunks = Main.chunkClaimDataConfig.getConfigurationSection("ClaimedChunks");
		Bukkit.getLogger().log(Level.INFO, "Loading claimed chunks into ram: Location | Chunk Name");
		for (String s : claimedChunks.getKeys(false)) {
			if (claimedChunks.get(s) != null){
				String chunkName = claimedChunks.get(s + ".appearsAs").toString();
				Main.chunkNamesRAM.add(chunkName.toString());
				Main.chunkLocationsRAM.add(s.toString());
				Bukkit.getLogger().log(Level.INFO, "Loaded chunk into ram | " + s.toString() + " | " + chunkName.toString());
			}
		}
		try {
			Main.chunkClaimDataConfig.save(Main.chunkClaimData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void chunkMapDraw(Player p) {
		String playerFaction = Main.factionHashMap.get(p.getUniqueId().toString());
		int mapSize = 3;
		p.sendMessage("-");
		p.sendMessage("-");
		p.sendMessage("-");
		p.sendMessage("-");
		p.sendMessage("-");
		p.sendMessage("-");
		p.sendMessage("-");
		p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Chunk Map");
		p.sendMessage("--------------------------------");
		String firstLineMessage = " ";
		
		for (int k = 0; k < (mapSize + 1); k++) {
			firstLineMessage += "  ";
		}
		
		firstLineMessage += "N";
		
		p.sendMessage(ChatColor.GREEN + firstLineMessage);
		
		for (int i = -mapSize; i < (mapSize + 1); i++) {
			String lineMessage = " ";
			if (i == 0) {
				lineMessage += "&aW ";
			}
			else {
				lineMessage += "|  ";
			}
			
			
			for (int j = -mapSize; j < (mapSize + 1); j++) {
				int chunkNameLocation = Main.chunkLocationsRAM.indexOf("X: " + (p.getLocation().getChunk().getX() + j) + ", Z: " + (p.getLocation().getChunk().getZ() + i));
				String chunkName = Main.chunkNamesRAM.get(chunkNameLocation);
				if (chunkName == null) {
					lineMessage += "&f* ";
				}
				else if (chunkName.equals(playerFaction)) {
					lineMessage += "&b* ";
				}
				else {
					lineMessage += "&c* ";
				}
			}
			
			if (i == 0) {
				lineMessage += "&aE";
			}
			else {
				lineMessage += "&f |";
			}
			
			String coloredLineMessage = ChatColor.translateAlternateColorCodes('&', lineMessage);
			p.sendMessage(coloredLineMessage);
		}
		
		String lastLineMessage = " ";
		for (int k = 0; k < (mapSize + 1); k++) {
			lastLineMessage += "  ";
		}
		lastLineMessage += "S";
		p.sendMessage(ChatColor.GREEN + lastLineMessage);
	}
	
	public static void killPhantoms() {
		for (World w : Bukkit.getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e instanceof Phantom) {
					e.remove();
				}
			}
		}
	}
	
	
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
		// instantiate particle classes
		ParticleData particle = new ParticleData(online.getUniqueId());
		ParticleEffects trails = new ParticleEffects(online);
		
		// cancels particles if there were any previously
		if (particle.hasID()) {
			particle.endTask();
			particle.removeID();
		}
		
		// stop function if the player doesn't have a talent
		if (talent == null) {
			online.setMaxHealth(20);
			return;
		}
		
		
		// load health and particle effects for different talents
		if (talent.equals("Avian")) {
			online.setMaxHealth(16);
			online.getInventory().setChestplate(ItemsPlus.avianElytra);
		}
		else if (talent.equals("Pyrokinetic")) {
			online.setMaxHealth(20);
			trails.startPyrokineticParticles();
		}
		else if (talent.equals("Hydrokinetic")) {
			online.setMaxHealth(20);
		}
		else if (talent.equals("Frostbender")) {
			online.setMaxHealth(20);
		}
		else if (talent.equals("Terran")) {
			online.setMaxHealth(20);
		}
		else if (talent.equals("Biokinetic")) {
			online.setMaxHealth(20);
		}
		else if (talent.equals("Enderian")) {
			online.setMaxHealth(16);
			trails.startEnderianParticles();
		}
		else if (talent.equals("SHERIFF")) {
			online.setMaxHealth(40);
		}
		else if (talent.equals("COBBLE MAN")) {
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// plugin startup functions
	public static void loadAvianHeadroomBlocks() {
		Main.avianHeadroomBlocks.add(Material.LADDER);
		Main.avianHeadroomBlocks.add(Material.SUGAR_CANE);
		Main.avianHeadroomBlocks.add(Material.AIR);
		Main.avianHeadroomBlocks.add(Material.CAVE_AIR);
		Main.avianHeadroomBlocks.add(Material.WATER);
		Main.avianHeadroomBlocks.add(Material.LAVA);
		Main.avianHeadroomBlocks.add(Material.TORCH);
		Main.avianHeadroomBlocks.add(Material.VINE);
		Main.avianHeadroomBlocks.add(Material.WEEPING_VINES);
		Main.avianHeadroomBlocks.add(Material.WEEPING_VINES_PLANT);
		Main.avianHeadroomBlocks.add(Material.LANTERN);
		Main.avianHeadroomBlocks.add(Material.SOUL_TORCH);
		Main.avianHeadroomBlocks.add(Material.WALL_TORCH);
		Main.avianHeadroomBlocks.add(Material.SOUL_LANTERN);
		Main.avianHeadroomBlocks.add(Material.CHAIN);
		Main.avianHeadroomBlocks.add(Material.LILY_PAD);
		Main.avianHeadroomBlocks.add(Material.BELL);
		Main.avianHeadroomBlocks.add(Material.LEVER);
		Main.avianHeadroomBlocks.add(Material.ACACIA_BUTTON);
		Main.avianHeadroomBlocks.add(Material.BIRCH_BUTTON);
		Main.avianHeadroomBlocks.add(Material.CRIMSON_BUTTON);
		Main.avianHeadroomBlocks.add(Material.DARK_OAK_BUTTON);
		Main.avianHeadroomBlocks.add(Material.SPRUCE_BUTTON);
		Main.avianHeadroomBlocks.add(Material.JUNGLE_BUTTON);
		Main.avianHeadroomBlocks.add(Material.POLISHED_BLACKSTONE_BUTTON);
		Main.avianHeadroomBlocks.add(Material.WARPED_BUTTON);
		Main.avianHeadroomBlocks.add(Material.OAK_BUTTON);
		Main.avianHeadroomBlocks.add(Material.STONE_BUTTON);
		Main.avianHeadroomBlocks.add(Material.ACACIA_SIGN);
		Main.avianHeadroomBlocks.add(Material.BIRCH_SIGN);
		Main.avianHeadroomBlocks.add(Material.CRIMSON_SIGN);
		Main.avianHeadroomBlocks.add(Material.DARK_OAK_SIGN);
		Main.avianHeadroomBlocks.add(Material.SPRUCE_SIGN);
		Main.avianHeadroomBlocks.add(Material.JUNGLE_SIGN);
		Main.avianHeadroomBlocks.add(Material.OAK_SIGN);
		Main.avianHeadroomBlocks.add(Material.WARPED_SIGN);
		Main.avianHeadroomBlocks.add(Material.ACACIA_WALL_SIGN);
		Main.avianHeadroomBlocks.add(Material.BIRCH_WALL_SIGN);
		Main.avianHeadroomBlocks.add(Material.CRIMSON_WALL_SIGN);
		Main.avianHeadroomBlocks.add(Material.DARK_OAK_WALL_SIGN);
		Main.avianHeadroomBlocks.add(Material.SPRUCE_WALL_SIGN);
		Main.avianHeadroomBlocks.add(Material.JUNGLE_WALL_SIGN);
		Main.avianHeadroomBlocks.add(Material.OAK_WALL_SIGN);
		Main.avianHeadroomBlocks.add(Material.WARPED_WALL_SIGN);
		Main.avianHeadroomBlocks.add(Material.TRIPWIRE);
		Main.avianHeadroomBlocks.add(Material.PLAYER_HEAD);
		Main.avianHeadroomBlocks.add(Material.PLAYER_WALL_HEAD);
		Main.avianHeadroomBlocks.add(Material.ZOMBIE_HEAD);
		Main.avianHeadroomBlocks.add(Material.ZOMBIE_WALL_HEAD);
		Main.avianHeadroomBlocks.add(Material.CREEPER_HEAD);
		Main.avianHeadroomBlocks.add(Material.DRAGON_HEAD);
		Main.avianHeadroomBlocks.add(Material.DRAGON_WALL_HEAD);
	}
	
	public static void loadUnrenameableItems() {
		Main.unrenameableItems.add(ItemsPlus.arcaneCrystal);
		Main.unrenameableItems.add(ItemsPlus.stasisCrystal);
		Main.unrenameableItems.add(ItemsPlus.timberAxe);
		Main.unrenameableItems.add(ItemsPlus.replantingHoe);
		Main.unrenameableItems.add(ItemsPlus.trackingBow);
		Main.unrenameableItems.add(ItemsPlus.bonkStick);
		Main.unrenameableItems.add(ItemsPlus.dashSword);
		Main.unrenameableItems.add(ItemsPlus.thugnarsGlock);
		Main.unrenameableItems.add(ItemsPlus.telekinesisBook);
		Main.unrenameableItems.add(ItemsPlus.smeltingBook);
		Main.unrenameableItems.add(ItemsPlus.absorptionPotion);
		Main.unrenameableItems.add(ItemsPlus.hastePotion);
		
	}
	
	public static void loadBloodMoonLootTable() {
		// Blocks
		Main.bloodMoonLootTable.add(new ItemStack(Material.OBSIDIAN));
		
		// Mining
		Main.bloodMoonLootTable.add(new ItemStack(Material.GOLD_INGOT));
		Main.bloodMoonLootTable.add(new ItemStack(Material.EMERALD));
		Main.bloodMoonLootTable.add(new ItemStack(Material.IRON_INGOT));
		
		// Foodstuffs
		Main.bloodMoonLootTable.add(new ItemStack(Material.APPLE));
		Main.bloodMoonLootTable.add(new ItemStack(Material.PUMPKIN_PIE));
		
		
		
		// Potions
		PotionType[] effects = new PotionType[]{PotionType.WEAKNESS, PotionType.NIGHT_VISION, PotionType.FIRE_RESISTANCE, PotionType.INVISIBILITY, PotionType.JUMP, PotionType.INSTANT_HEAL};
		for (PotionType effect : effects) {
			ItemStack potion = new ItemStack(Material.POTION);
			PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
			potionMeta.setBasePotionData(new PotionData(effect));
			potion.setItemMeta(potionMeta);
			Main.bloodMoonLootTable.add(potion);
		}
		
		
		// Arrows
		PotionType[] arrowEffects = new PotionType[]{PotionType.NIGHT_VISION, PotionType.FIRE_RESISTANCE, PotionType.POISON, PotionType.JUMP, PotionType.INSTANT_HEAL};
		for (PotionType effect : arrowEffects) {
			ItemStack arrow = new ItemStack(Material.TIPPED_ARROW);
			PotionMeta potionMeta = (PotionMeta) arrow.getItemMeta();
			potionMeta.setBasePotionData(new PotionData(effect));
			arrow.setItemMeta(potionMeta);
			Main.bloodMoonLootTable.add(arrow);
		}
		
		
		// Brewing ingredients
		Main.bloodMoonLootTable.add(new ItemStack(Material.FERMENTED_SPIDER_EYE));
		Main.bloodMoonLootTable.add(new ItemStack(Material.SUGAR));
		Main.bloodMoonLootTable.add(new ItemStack(Material.RABBIT_FOOT));
		
		
		// Miscellaneous
		Main.bloodMoonLootTable.add(new ItemStack(Material.FLINT));
	}
	
	
	
	
	
	
	// HashMap loader function
	public static void loadHashMaps(Player online) {
		
		// player info
		Main.playerRankHashMap.put(online.getUniqueId().toString(), Main.playerDataConfig.getString("Users." + online.getUniqueId() + ".stats" + ".rank"));
		
		if (Main.playerRankHashMap.get(online.getUniqueId().toString()) == null) {
			Main.playerDataConfig.set("Users." + online.getUniqueId() + ".stats" + ".rank", "Member");
			Main.playerRankHashMap.put(online.getUniqueId().toString(), "Member");
		}
		
		Main.factionHashMap.put(online.getUniqueId().toString(), Main.playerDataConfig.getString("Users." + online.getUniqueId() + ".stats" + ".faction"));
		Main.playerDeathsHashMap.put(online.getUniqueId().toString(), 0 + Main.playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".deaths"));
		Main.playerYawHashMap.put(online.getUniqueId().toString(), 0f);
		Main.playerUnchangedLookDirHashMap.put(online.getUniqueId().toString(), 0);
		
		
		// player weapon data
		Main.trackingBowTarget.put(online.getUniqueId().toString(), null);
		
		
		// player skill data
		Main.miningPointsTracker.put(online.getUniqueId().toString(), 0 + Main.playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".miningPoints"));
		Main.combatPointsTracker.put(online.getUniqueId().toString(), 0 + Main.playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".combatPoints"));
		Main.farmingPointsTracker.put(online.getUniqueId().toString(), 0 + Main.playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".farmingPoints"));
		Main.enchantingPointsTracker.put(online.getUniqueId().toString(), 0 + Main.playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".enchantingPoints"));
		Main.alchemyPointsTracker.put(online.getUniqueId().toString(), 0 + Main.playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".alchemyPoints"));
		
		
		// player talent
		Main.talentHashMap.put(online.getUniqueId().toString(), Main.playerDataConfig.getString("Users." + online.getUniqueId() + ".stats" + ".talent"));
		
		
		// player talent cooldowns
		Main.stasisCrystalEnergy.put(online.getUniqueId().toString(), 0);
		Main.arcaneCrystalEnergy.put(online.getUniqueId().toString(), 0);
		
		
		// player status effects
		Main.playerFrozenHashMap.put(online.getUniqueId().toString(), 0);
		Main.fireWeaknessHashMap.put(online.getUniqueId().toString(), 0);
		
		
		// player cooldowns
		Main.canTpHashMap.put(online.getUniqueId().toString(), false);
		Main.canSaveDataHashMap.put(online.getUniqueId().toString(), false);
		
		
		// player settings
		Main.scoreboardHashMap.put(online.getUniqueId().toString(), Main.playerDataConfig.getBoolean("Users." + online.getUniqueId() + ".preferences" + ".scoreboard"));
		
	
	}
}
