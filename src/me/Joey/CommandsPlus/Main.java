package me.Joey.CommandsPlus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

// TODO: New leveling rewards
// TODO: Faction messaging system
// TODO: Hashmap for event handlers

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener, GlobalHashMaps{
	
	// some variable declarations
	public static Inventory talentInventory;
	public static List<String> toolIDs = new ArrayList <>();
	
	
	// initialize plugin data files
	public static File playerLogs;
	public static FileConfiguration playerLogsConfig;
	public static File playerData;
	public static FileConfiguration playerDataConfig;
	public static File chunkClaimData;
	public static FileConfiguration chunkClaimDataConfig;
	public static File factionData;
	public static FileConfiguration factionDataConfig;
	
	
	// initialize plugin data variables
	public static boolean isBloodMoon = false;
	public static ArrayList<ItemStack> bloodMoonLootTable = new ArrayList<>();
	
	public static void loadBloodMoonLootTable() {
		bloodMoonLootTable.add(new ItemStack(Material.GOLD_INGOT));
		bloodMoonLootTable.add(new ItemStack(Material.IRON_INGOT));
		bloodMoonLootTable.add(new ItemStack(Material.GOLDEN_APPLE));
		
		
		PotionType[] effects = new PotionType[]{PotionType.STRENGTH, PotionType.FIRE_RESISTANCE, PotionType.INVISIBILITY, PotionType.JUMP, PotionType.INSTANT_HEAL};
		for (PotionType effect : effects) {
			ItemStack potion = new ItemStack(Material.POTION);
			PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
			potionMeta.setBasePotionData(new PotionData(effect));
			potion.setItemMeta(potionMeta);
			bloodMoonLootTable.add(potion);
		}
		
	}
	
	
	// HashMap loader function
	public static void loadHashMaps(Player online) {
		// player info
		factionHashMap.put(online.getUniqueId().toString(), "" + playerDataConfig.getString("Users." + online.getUniqueId() + ".stats" + ".faction"));
		
		// player skill data
		miningPointsTracker.put(online.getUniqueId().toString(), 0 + playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".miningPoints"));
		combatPointsTracker.put(online.getUniqueId().toString(), 0 + playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".combatPoints"));
		farmingPointsTracker.put(online.getUniqueId().toString(), 0 + playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".farmingPoints"));
		enchantingPointsTracker.put(online.getUniqueId().toString(), 0 + playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".enchantingPoints"));
		alchemyPointsTracker.put(online.getUniqueId().toString(), 0 + playerDataConfig.getInt("Users." + online.getUniqueId() + ".stats" + ".alchemyPoints"));
		
		// player talent
		talentHashMap.put(online.getUniqueId().toString(), playerDataConfig.getString("Users." + online.getUniqueId() + ".stats" + ".talent"));
		
		// player cooldowns
		canTpHashMap.put(online.getUniqueId().toString(), false);
		canSaveDataHashMap.put(online.getUniqueId().toString(), false);
		
		// player settings
		scoreboardHashMap.put(online.getUniqueId().toString(), playerDataConfig.getBoolean("Users." + online.getUniqueId() + ".preferences" + ".scoreboard"));		

	}
	
	
	// timber axe block hashsets
	public static HashSet<Material>	logMaterials = new HashSet<>();
	public static HashSet<String> validLogMaterials = new HashSet<>(Arrays.asList("LOG", "LOG_2", "LEGACY_LOG", "LEGACY_LOG_2", "ACACIA_LOG", "BIRCH_LOG", "DARK_OAK_LOG", "JUNGLE_LOG", "OAK_LOG", "SPRUCE_LOG", "CRIMSON_STEM", "WARPED_STEM"));
	
	
	public static ArrayList<String> chunkNamesRAM = new ArrayList<String>();
	public static ArrayList<String> chunkLocationsRAM = new ArrayList<String>();
	
	
	@Override
	public void onEnable() {
		
		// load config file
		loadConfig();
		
		
		// enable ItemsPlus
		ItemsPlus.init();
		
		
		// register EnchantsPlus
		EnchantmentsPlus.register();
		
		
		// construct talent inventory
		FunctionsPlus.createTalentSelectionInventory();
		
		
		// load blood moon loot table - very descriptive, I know, thank me later
		loadBloodMoonLootTable();
		
		
		// register valid log materials HashSet for Timber Axe
		for (Material material : Material.values())
		{
			if (validLogMaterials.contains(material.name()))
			{
				logMaterials.add(material);
			}
		}
		
		// create/open player log file
		playerLogs = new File(getDataFolder(), "playerLogs.yml");
		if (!playerLogs.exists()) { 
			playerLogs.getParentFile().mkdirs();
	      try {
	    	  playerLogs.createNewFile();
	      } catch (IOException ex) {
	         ex.printStackTrace();
	      }
	    }
		playerLogsConfig = YamlConfiguration.loadConfiguration(playerLogs);
		
		
		// create/open player data file
		playerData = new File(getDataFolder(), "playerData.yml");
		if (!playerData.exists()) { 
			playerData.getParentFile().mkdirs();
	      try {
	    	  playerData.createNewFile();
	      } catch (IOException ex) {
	         ex.printStackTrace();
	      }
	    }
		playerDataConfig = YamlConfiguration.loadConfiguration(playerData);
		
		
		// create/open chunk claim data file
		chunkClaimData = new File(getDataFolder(), "chunkClaimData.yml");
		if (!chunkClaimData.exists()) { 
			chunkClaimData.getParentFile().mkdirs();
	      try {
	    	  chunkClaimData.createNewFile();
	      } catch (IOException ex) {
	         ex.printStackTrace();
	      }
	    }
		chunkClaimDataConfig = YamlConfiguration.loadConfiguration(chunkClaimData);
		
		
		// create/open chunk claim data file
		factionData = new File(getDataFolder(), "factionData.yml");
		if (!factionData.exists()) { 
			factionData.getParentFile().mkdirs();
	      try {
	    	  factionData.createNewFile();
	      } catch (IOException ex) {
	         ex.printStackTrace();
	      }
	    }
		factionDataConfig = YamlConfiguration.loadConfiguration(factionData);
		
		// load chunk arrays - MUST COME AFTER CHUNK FILE IS OPENED
		try {
			loadChunkArrays();
		} catch (Exception e){
			getLogger().log(Level.INFO, "Failed to load chunk arrays, leaving blank");
		}
		
		
		boolean hashMapsCreated = false;
		
		for (Player online : Bukkit.getOnlinePlayers()) {

			if (!hashMapsCreated) {
				
				loadHashMaps(online);
			}
			
		}
		hashMapsCreated = true;

		
		// scoreboard initialization and End checker
		this.getServer().getPluginManager().registerEvents(new Events(), this);
		BukkitScheduler scheduler = getServer().getScheduler();
		
		// check every 50ms
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	if (!Bukkit.getOnlinePlayers().isEmpty()) {
            		
            		
            		
        			
            	}
            }
		}, 0L, 1L);
		
		// check every 250ms for potion effects
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	if (!Bukkit.getOnlinePlayers().isEmpty()) {

        			for (Player online : Bukkit.getOnlinePlayers()) {
        				
        				
        				
        				
        				
        				
        				
        				String talent = talentHashMap.get(online.getUniqueId().toString());
        				Location location = online.getLocation();
        				if (talent.equals("Avian")) {
        					for(int y = location.getBlockY() + 2; y < location.getBlockY() + 10; y++) {
        						Location blockCheck = new Location(online.getWorld(), location.getBlockX(), y, location.getBlockZ());
        						if (blockCheck.getBlock().getType() != Material.AIR && blockCheck.getBlock().getType() != Material.WATER && blockCheck.getBlock().getType() != Material.LAVA) {
        							online.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30, 0));
        							online.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1));
        							break;
        						}
        					}
        					
        					if (location.getBlockY() > 127) {
        						boolean regenFound = false;
            					for (PotionEffect potionEffect : online.getActivePotionEffects()) {
            						if (potionEffect.getType().equals(PotionEffectType.REGENERATION)) {
            							regenFound = true;
            							int duration = potionEffect.getDuration();
            							if (duration < 10) {
            								online.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
            							}
            						}
            						
            					}
            					if(!regenFound) {
        							online.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
        						}
        					}
        				}
        				
        				if (talent.equals("Hydrokinetic")) {
        					Material m1 = online.getPlayer().getLocation().getBlock().getType();
        					Material m2 = online.getPlayer().getLocation().add(0, 1, 0).getBlock().getType();
        				    if (m1 == Material.WATER || m2 == Material.WATER || m1 == Material.SEAGRASS || m2 == Material.SEAGRASS || m1 == Material.TALL_SEAGRASS || m2 == Material.TALL_SEAGRASS || m1 == Material.KELP || m2 == Material.KELP) {
            					online.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 30, 0));
            					online.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 30, 1));
            					boolean regenFound = false;
            					for (PotionEffect potionEffect : online.getActivePotionEffects()) {
            						if (potionEffect.getType().equals(PotionEffectType.REGENERATION)) {
            							regenFound = true;
            							int duration = potionEffect.getDuration();
            							if (duration < 10) {
            								online.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
            							}
            						}
            						
            					}
            					if(!regenFound) {
        							online.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
        						}
        				    }
        				}
        				
        				if (talent.equals("Frostbender")) {
        					Material m1 = online.getPlayer().getLocation().getBlock().getType();
        					Material m2 = online.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType();
        					if (m1 == Material.SNOW || m2 == Material.ICE || m2 == Material.PACKED_ICE || m2 == Material.BLUE_ICE || m2 == Material.SNOW_BLOCK) {
        						online.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 1));
        					}
        				}
        				
        				if (talent.equals("Rogue")) {
        					online.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 0));
        					online.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100000, 0));
        				}
        			}
            	}
            }
		}, 0L, 5L);
		
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	if (!Bukkit.getOnlinePlayers().isEmpty()) {
            		
            		killPhantoms();
            		
        			for (Player online : Bukkit.getOnlinePlayers()) {
        				if (scoreboardHashMap.get(online.getUniqueId().toString())) {
        					createBoard(online);
        				}
        				else {
        					online.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        				}
        				endCheck(online);
        				
        				
        				if (!isBloodMoon && (FunctionsPlus.getTime(getServer(), online) % 24000) >= 13000 && ((FunctionsPlus.getTime(getServer(), online) % 24000) < 13020)) {
        					if (((int) (Math.random() * 20)) == 7) {
        						isBloodMoon = true;
        						for (Player online_copy : Bukkit.getOnlinePlayers()) {
        							online_copy.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "A blood moon is rising!");
        						}
        					}
        				}
        				if (isBloodMoon && (FunctionsPlus.getTime(getServer(), online) % 24000) >= 23000 && ((FunctionsPlus.getTime(getServer(), online) % 24000) < 23200)) {
        					isBloodMoon = false;
    						for (Player online_copy : Bukkit.getOnlinePlayers()) {
    							online_copy.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "The blood moon sets, only to rise again soon.");
    						}
        				}
        				
        				
        			}
            	}
            }
        }, 0L, 20L);
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	if (!Bukkit.getOnlinePlayers().isEmpty()) {
            		killPhantoms();
            		
        			for (Player online : Bukkit.getOnlinePlayers()) {
        				canSaveDataHashMap.put(online.getPlayer().getUniqueId().toString(), true);
        			}
        			saveConfig();
            	}
            }
        }, 0L, 400L);
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	if (!Bukkit.getOnlinePlayers().isEmpty()) {
            		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm z");
            		Date date = new Date(System.currentTimeMillis());
            		playerLogsConfig.set("Online Players." + formatter.format(date), Bukkit.getOnlinePlayers().size());
            		try {
						playerLogsConfig.save(playerLogs);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	else {
            		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm z");
            		Date date = new Date(System.currentTimeMillis());
            		playerLogsConfig.set("Online Players." + formatter.format(date), 0);
            		try {
						playerLogsConfig.save(playerLogs);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
        }, 0L, 12000L);
		
		
	}
	
	@Override
	public void onDisable() {
		// shutdown
		// reloads
		// plugin reloads
	}
	

	
	
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
		
		Score score2 = obj.getScore("Online Players: " + ChatColor.AQUA + Bukkit.getOnlinePlayers().size());
		score2.setScore(5);
		

		String territoryLookup = "X: " + player.getLocation().getChunk().getX() + ", Z: " + player.getLocation().getChunk().getZ();
		String territoryName = null;
		for (int i = 0; i < chunkLocationsRAM.size(); i++) {
			if (chunkLocationsRAM.get(i).equals(territoryLookup)) {
				territoryName = chunkNamesRAM.get(i);
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
		
		Score score4 = obj.getScore("Level: " + ChatColor.AQUA + playerDataConfig.getInt("Users." + player.getUniqueId() + ".stats" + ".level"));
		score4.setScore(3);
		
		Score score5 = obj.getScore("");
		score5.setScore(2);
		
		Score score6 = obj.getScore(ChatColor.DARK_GRAY + ft.format(date));
		score6.setScore(1);
		
		player.setScoreboard(board);
	}
	
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void readInFiles() {
		
	}
	
	
	public void killPhantoms() {
		for (World w : Bukkit.getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e instanceof Phantom) {
					e.remove();
				}
			}
		}
	}
	
	// draws chunkmap
	public void chunkMapDraw(Player p) {
		String playerUUID = p.getUniqueId().toString();
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
				String chunkOwnerUUID = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + (p.getLocation().getChunk().getX() + j) + ", Z: " + (p.getLocation().getChunk().getZ() + i) + ".belongsToUUID");
				if (chunkOwnerUUID == null) {
					lineMessage += "&f* ";
				}
				else if (chunkOwnerUUID.equals(playerUUID)) {
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
	
	public void endCheck(Player p) {
		if (p.getWorld().getEnvironment() == Environment.THE_END){
			p.setHealth(0.0f);
		}
	}
	
	public void changeChunkName(Player p, String chunkName) {
		ConfigurationSection claimedChunks = chunkClaimDataConfig.getConfigurationSection("ClaimedChunks");
		for (String s : claimedChunks.getKeys(false)) {
			if (claimedChunks.get(s) != null){
				UUID chunkOwnerUUID = UUID.fromString(claimedChunks.get(s + ".belongsToUUID").toString());
				if (chunkOwnerUUID.equals(p.getUniqueId())) {
					claimedChunks.set(s + ".appearsAs", chunkName);
				}
			}
		}
		try {
			chunkClaimDataConfig.save(chunkClaimData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void unclaimAllChunks(Player p) {
		ConfigurationSection claimedChunks = chunkClaimDataConfig.getConfigurationSection("ClaimedChunks");
		for (String s : claimedChunks.getKeys(false)) {
			if (claimedChunks.get(s) != null){
				try {
					UUID chunkOwnerUUID = UUID.fromString(claimedChunks.get(s + ".belongsToUUID").toString());
					if (chunkOwnerUUID.equals(p.getUniqueId())) {
						claimedChunks.set(s + ".belongsToUUID", null);
						claimedChunks.set(s + ".belongsToName", null);
						claimedChunks.set(s + ".appearsAs", null);
						
						if (playerDataConfig.contains("Users." + p.getUniqueId() + ".stats" + ".chunkClaims")){
							playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".chunkClaims", playerDataConfig.getInt("Users." + p.getUniqueId() + ".stats" + ".chunkClaims") - 1);
						}
						
						
						try {
							int i = chunkLocationsRAM.indexOf(s);
							chunkLocationsRAM.remove(i);
							chunkNamesRAM.remove(i);
						} catch (Exception e){
							getLogger().log(Level.SEVERE, "Chunk to unclaim not found in ArrayList!");
						}
					}
				} catch (Exception e) {
				}
				
			}
		}
		try {
			chunkClaimDataConfig.save(chunkClaimData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadChunkArrays() {
		ConfigurationSection claimedChunks = chunkClaimDataConfig.getConfigurationSection("ClaimedChunks");
		getLogger().log(Level.INFO, "Loading claimed chunks into ram: Location | Chunk Name");
		for (String s : claimedChunks.getKeys(false)) {
			if (claimedChunks.get(s) != null){
				String chunkName = claimedChunks.get(s + ".appearsAs").toString();
				chunkNamesRAM.add(chunkName.toString());
				chunkLocationsRAM.add(s.toString());
				getLogger().log(Level.INFO, "Loaded chunk into ram | " + s.toString() + " | " + chunkName.toString());
			}
		}
		try {
			chunkClaimDataConfig.save(chunkClaimData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	


	
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// variables that get used a lot
		if (!(sender instanceof Player)) {
			return true;
		}
		
		Player p = (Player) sender;
		boolean vanillaMode = getConfig().getBoolean("System." + "settings" + ".vanillaMode");
		
		// command to reevaluate some user stats on the server, this is used to prevent cheating (editing the server config files)
		if (label.equalsIgnoreCase("reeval")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			if ((p.getUniqueId().toString()).equals("b859c378-5ea4-47f8-b7e4-fc80bfc1b713")) {
				for (Player online : Bukkit.getOnlinePlayers()) {
					FunctionsPlus.savePlayerData(online, false);
					playerDataConfig.set("Users." + online.getUniqueId() + "." + "stats" + ".name", online.getName());
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.BLUE + "Reevaluating level of " + ChatColor.WHITE + online.getName());
					
				
				} 
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.BLUE + "Done");
			}
					
		}
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// System Commands
		if (label.equalsIgnoreCase("listplayers") && (p.getUniqueId().toString()).equals("b859c378-5ea4-47f8-b7e4-fc80bfc1b713")) {
			int i = 0;
			for (Player online : Bukkit.getOnlinePlayers()) {
				p.sendMessage(online.getName() + ": " + online.getUniqueId());
				i++;
			}
			p.sendMessage("Total players online: " + i);
		}

		//-----------------------------------------------------------------------------------------------------------------------------------------------
		
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// commands to edit system and personal preferences
		if (label.equalsIgnoreCase("vanillamode") && (p.getUniqueId().toString()).equals("b859c378-5ea4-47f8-b7e4-fc80bfc1b713")) {
			
			if (getConfig().getBoolean("System." + "settings" + ".vanillaMode") == true) {
				getConfig().set("System." + "settings" + ".vanillaMode", false);
			}
			else {
				getConfig().set("System." + "settings" + ".vanillaMode", true);
			}
			
			return true;
		}
		
		
		// enable or disable scoreboard
		if (label.equalsIgnoreCase("scoreboard")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			
			if (playerDataConfig.getBoolean("Users." + p.getUniqueId() + "." + "preferences" + ".scoreboard")) {
				playerDataConfig.set("Users." + p.getUniqueId() + "." + "preferences" + ".scoreboard", false);
				scoreboardHashMap.put(p.getUniqueId().toString(), false);
				
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Disabled scoreboard!");
			}
			else {
				playerDataConfig.set("Users." + p.getUniqueId() + "." + "preferences" + ".scoreboard", true);
				scoreboardHashMap.put(p.getUniqueId().toString(), true);
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Enabled scoreboard!");

			}
			saveConfig();
			
			return true;
		}
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// main user commands
		if (label.equalsIgnoreCase("c+help")) {
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + " commands are:");
			p.sendMessage(ChatColor.YELLOW + "------------------------------");
			p.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "https://docs.google.com/document/d/1o8gLXXOj9hEkDjY5OD3lujR_yNdKC9mI8v1sNKTa5SY/edit?usp=sharing");

			
			
			return true;
		
		}
		
		// teleports you to the top block at your current location
		if (label.equalsIgnoreCase("top")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			
			if (p.getWorld().getEnvironment() == Environment.NETHER){
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You cannot use /top in the Nether!");
				return true;
			}
			
			// teleport player to top y-coordinate in their location(don't use in nether please)
			Location l = new Location(Bukkit.getWorld("world"), p.getLocation().getX(), p.getWorld().getHighestBlockYAt(p.getLocation()) + 1, p.getLocation().getZ());
			
			// safeguards so people don't get tp'ed into lava
			Location lCheck = new Location(Bukkit.getWorld("world"), p.getLocation().getX(), p.getWorld().getHighestBlockYAt(p.getLocation()) - 1, p.getLocation().getZ());
			int xPlus = 0;
			
			while(lCheck.getBlock().getType() == Material.LAVA) {
				xPlus += 10;
				lCheck = new Location(Bukkit.getWorld("world"), p.getLocation().getX() + xPlus, p.getWorld().getHighestBlockYAt(p.getLocation()) - 1, p.getLocation().getZ());
			}
			
			l = new Location(Bukkit.getWorld("world"), p.getLocation().getX() + xPlus, p.getWorld().getHighestBlockYAt(p.getLocation()) + 1, p.getLocation().getZ());
			
			p.teleport(l);
			
			// Commands+ System Message
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.GREEN + "Teleported you to the top!");

				
			return true;
		
		}
		
		// just send the player flying, very fun
		if (label.equalsIgnoreCase("launch")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			

			if (args.length == 0) {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.AQUA + "" + ChatColor.BOLD + "Y E E T");
				p.setVelocity(p.getLocation().getDirection().multiply(2).setY(1));
				
				return true;
			}
			

		}
		
		
		
		if (label.equalsIgnoreCase("cords")) {
			
			// get player's current coordinates
			if (args.length == 0) {
				Location loc = p.getLocation();
				int x = loc.getBlockX();
				int y = loc.getBlockY();
				int z = loc.getBlockZ();
				
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Your coordinates" + ": " + x + ", " + y + ", " + z);
				return true;

			}
			
			// send player's coordinates to everyone on the server
			else if (args[0].equals("send")) {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Sending your coordinates to everyone!");
				Location loc = p.getLocation();
				int x = loc.getBlockX();
				int y = loc.getBlockY();
				int z = loc.getBlockZ();
				for (Player online : Bukkit.getOnlinePlayers()) {
					online.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + p.getName() + "'s coordinates are: " + x + ", " + y + ", " + z);
				}
				
				return true;
			}
			
		}
			
		// just a command to flex a bit
		if (label.equalsIgnoreCase("creator")) {
			// Commands+ System Message
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "The creator of this plugin is Joey Balardeta, otherwise known as aclownsquad.");
			
			return true;
		}
		
		
		
		if (label.equalsIgnoreCase("dailyxp")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			
			String format = "M/d/yyyy";
			Date date = new Date();
	        SimpleDateFormat ft = new SimpleDateFormat(format);
			if (!( ( (String) ft.format(date)).equals( (String) (playerDataConfig.getString("Users." + p.getUniqueId() + "." + "stats" + ".dailyXPLastClaimed"))))) {
				playerDataConfig.set("Users." + p.getUniqueId() + "." + "stats" + ".dailyXPLastClaimed", ft.format(date));
				saveConfig();
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Claimed daily XP");
				p.giveExpLevels(20);
			}
			else {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Already claimed daily XP!");
			}
			
			
		}
		
		if (label.equalsIgnoreCase("wild")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			
			if (p.getWorld().getEnvironment() == Environment.NETHER){
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You cannot use /wild in the Nether!");
				return true;
			}
			
			if (!(p.getUniqueId().toString()).equals("b859c378-5ea4-47f8-b7e4-fc80bfc1b713") && !canTpHashMap.get(p.getUniqueId().toString())) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You can only use /wild every 20 seconds!");
				return true;
			}
			
			// create random number generator and range
			Random rand = new Random();
			int randRange = 30001;
			
			// random location creation
			int destX = -15000 + rand.nextInt(randRange);
		    int destZ = -15000 + rand.nextInt(randRange);
		    
		    Location dest = new Location(Bukkit.getWorld("world"), destX, p.getWorld().getHighestBlockYAt(destX, destZ) - 1, destZ);
		    
		    
		    while(dest.getBlock().getType() == Material.WATER || dest.getBlock().getType() == Material.LAVA) {
		    	destX = -15000 + rand.nextInt(randRange);
			    destZ = -15000 + rand.nextInt(randRange);
			    
			    dest = new Location(Bukkit.getWorld("world"), destX, p.getWorld().getHighestBlockYAt(destX, destZ) - 1, destZ);
		    }
		    int destXfinal = destX;	
		    int destZfinal = destZ;	
		    
		    Location destFinal = new Location(Bukkit.getWorld("world"), destX, p.getWorld().getHighestBlockYAt(destX, destZ) + 1, destZ);
		    
		    // get current player location
		    int currX = p.getLocation().getBlockX();
		    int currY = p.getLocation().getBlockY();
		    int currZ = p.getLocation().getBlockZ();
			
		    
		    // Commands+ System Messages
	        p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Starting Countdown");
	        p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.BLUE + "3");
	        

	        int chunkGenRange = 1;
	        
	        for (int i = -chunkGenRange; i < chunkGenRange + 1; i++) {
	        	for (int j = -chunkGenRange; j < chunkGenRange + 1; j++) {
	        		Location chunkLoad = new Location(Bukkit.getWorld("world"), destX + (j * 16), p.getWorld().getHighestBlockYAt(destX, destZ), destZ + (i * 16));
		        	chunkLoad.getChunk().load();
	        		
		        }
	        }
			
	        for (int i = -chunkGenRange; i < chunkGenRange + 1; i++) {
	        	for (int j = -chunkGenRange; j < chunkGenRange + 1; j++) {
	        		Location chunkLoad = new Location(Bukkit.getWorld("world"), destXfinal + (j * 16), p.getWorld().getHighestBlockYAt(destXfinal, destZfinal), destZfinal + (i * 16));
		        	chunkLoad.getChunk().unload();
	        		
		        }
	        }
			
			this.getServer().getPluginManager().registerEvents(this, this);
			BukkitScheduler scheduler = getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(this, new Runnable() {
	            public void run() {
	            	
	            	// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.BLUE + "2");
	            }
	        }, 20L);
			
			scheduler.scheduleSyncDelayedTask(this, new Runnable() {
	            public void run() {
	            	
	            	// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.BLUE + "1");
	            }
	        }, 40L);
			
			scheduler.scheduleSyncDelayedTask(this, new Runnable() {
	            public void run() {
	            	if (!(currX == p.getLocation().getBlockX() && currY == p.getLocation().getBlockY() && currZ == p.getLocation().getBlockZ())) {
	            		
	            		p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You moved! Cancelling teleport!");
	            	}
	            	else {
		            	p.teleport(destFinal);
		            	canTpHashMap.put(p.getUniqueId().toString(), false);
		            	
		            	// Commands+ System Message
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Teleported you into the wild!");
	            	}
	            }
	        }, 60L);
			
			
		}
		
		
		if (label.equalsIgnoreCase("waypoint")) {
			// if vanilla mode is on or in nether don't run command
			if (vanillaMode) {
				return true;
			}
			
			// list waypoints, clickable too!
			if (args.length == 0) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Your waypoints:");
				p.sendMessage("--------------------------------");
				
				ConfigurationSection waypoints = playerDataConfig.getConfigurationSection("Users." + p.getUniqueId() + ".waypoints");
				for (String s : waypoints.getKeys(false)) {
					String wpName = s;
					
					int wpX = playerDataConfig.getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X");
					int wpY = playerDataConfig.getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y");
					int wpZ = playerDataConfig.getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z");
					
					if (!(wpX == 0 && wpY == 0 && wpZ == 0)) {
						TextComponent wayPointsMessage = new TextComponent(ChatColor.WHITE + ">> " + ChatColor.AQUA + wpName);
						wayPointsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/waypoint " + wpName));
						wayPointsMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Teleport").color(ChatColor.GRAY).italic(true).create()));
						p.spigot().sendMessage(wayPointsMessage);
					}
					
				}
				
				
				return true;
			}
			
			// create waypoint
			if (args[0].equals("create")){
				if (args.length == 1) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You need to name your waypoint!");
					return true;
				}
				
				
				String wpName = args[1];
				for (int i = 2; i < args.length; i++) {
					wpName += " " + args[i];
				}
				
				int wpX = playerDataConfig.getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X");
				int wpY = playerDataConfig.getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y");
				int wpZ = playerDataConfig.getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z");
				
				if (0 + playerDataConfig.getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") != 0) {
					ConfigurationSection waypoints = playerDataConfig.getConfigurationSection("Users." + p.getUniqueId() + ".waypoints");
					for (String s : waypoints.getKeys(false)) {
						if (s.equalsIgnoreCase(wpName) && (wpX == 0 && wpY == 0 && wpZ == 0)){
							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You cannot make multiple waypoints of the same name!");
							return true;
						}
					}
				}
				
				
				// set location in config file
				playerDataConfig.set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X", p.getLocation().getBlockX());
				playerDataConfig.set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y", p.getLocation().getBlockY());
				playerDataConfig.set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z", p.getLocation().getBlockZ());
				
				playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints", 0 + playerDataConfig.getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") + 1);
				
				saveConfig();
				
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Created waypoint " + ChatColor.AQUA + wpName + ChatColor.WHITE +"!");
				
				return true;
			}
			
			// remove waypoint
			if (args[0].equals("remove")){
				if (args.length == 1) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You need to include the name of the waypoint that you want to remove!");
				}
				String wpName = args[1];
				for (int i = 2; i < args.length; i++) {
					wpName += " " + args[i];
				}
				
				boolean waypointExists = false;
				
				if (0 + playerDataConfig.getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") != 0) {
					ConfigurationSection waypoints = playerDataConfig.getConfigurationSection("Users." + p.getUniqueId() + ".waypoints");
					for (String s : waypoints.getKeys(false)) {
						if (s.equalsIgnoreCase(wpName)){
							waypointExists = true;
						}
					}
				}
				
				if (!waypointExists) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Waypoint does not exist!");
					return true;
				}
				
				// set location in config file
				playerDataConfig.set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X", 0);
				playerDataConfig.set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y", 0);
				playerDataConfig.set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z", 0);
				
				playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints", 0 + playerDataConfig.getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") - 1);
				
				saveConfig();
				
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Removed waypoint " + ChatColor.AQUA + wpName + ChatColor.WHITE +"!");
			}
			
			if (args[0].equals("update")) {
				if (args.length == 1) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You need to include the name of the waypoint that you want to remove!");
				}
				String wpName = args[1];
				for (int i = 2; i < args.length; i++) {
					wpName += " " + args[i];
				}
				
				boolean waypointExists = false;
				
				if (0 + playerDataConfig.getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") != 0) {
					ConfigurationSection waypoints = playerDataConfig.getConfigurationSection("Users." + p.getUniqueId() + ".waypoints");
					for (String s : waypoints.getKeys(false)) {
						if (s.equalsIgnoreCase(wpName)){
							waypointExists = true;
						}
					}
				}
				
				if (!waypointExists) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Waypoint does not exist!");
					return true;
				}
				
				// set location in config file
				playerDataConfig.set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X", p.getLocation().getBlockX());
				playerDataConfig.set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y", p.getLocation().getBlockY());
				playerDataConfig.set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z", p.getLocation().getBlockZ());
				
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Updated waypoint " + ChatColor.AQUA + wpName + ChatColor.WHITE +"!");
			}
			
			
			else {
				if (!(p.getUniqueId().toString()).equals("b859c378-5ea4-47f8-b7e4-fc80bfc1b713") && !canTpHashMap.get(p.getUniqueId().toString())) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You can only teleport with /waypoint every 20 seconds!");
					return true;
				}
				
				String wpName = args[0];
				for (int i = 1; i < args.length; i++) {
					wpName += " " + args[i];
				}
				
				// get location from config file
				int wpX = 0 + playerDataConfig.getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X");
				int wpY = 0 + playerDataConfig.getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y");
				int wpZ = 0 + playerDataConfig.getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z");
				
				if (wpX == 0 && wpY == 0 && wpZ == 0) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Waypoint does not exist!");
					return true;
				}
		
				
				// set final variables because Java is fun
				Location destFinal = new Location(Bukkit.getWorld("world"), wpX, wpY, wpZ);
				String wpNameFinal = wpName;

				// get current player location
			    int currX = p.getLocation().getBlockX();
			    int currY = p.getLocation().getBlockY();
			    int currZ = p.getLocation().getBlockZ();
				
			    
			    // Commands+ System Messages
		        p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Starting Countdown");
		        p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.BLUE + "3");
				
				this.getServer().getPluginManager().registerEvents(this, this);
				BukkitScheduler scheduler = getServer().getScheduler();
				scheduler.scheduleSyncDelayedTask(this, new Runnable() {
		            public void run() {
		            	
		            	// Commands+ System Message
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.BLUE + "2");
		            }
		        }, 20L);
				
				scheduler.scheduleSyncDelayedTask(this, new Runnable() {
		            public void run() {
		            	
		            	// Commands+ System Message
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.BLUE + "1");
		            }
		        }, 40L);
				
				scheduler.scheduleSyncDelayedTask(this, new Runnable() {
		            public void run() {
		            	if (!(currX == p.getLocation().getBlockX() && currY == p.getLocation().getBlockY() && currZ == p.getLocation().getBlockZ())) {
		            		p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You moved! Cancelling teleport!");
		            	}
		            	else {
			            	p.teleport(destFinal);
			            	canTpHashMap.put(p.getUniqueId().toString(), false);
			            	
			            	// Commands+ System Message
							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Teleported to " + ChatColor.AQUA + wpNameFinal + ChatColor.WHITE +  "!");
		            	}
		            }
		        }, 60L);
			}
			
		}
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// Chunk Commands
		// claim the chunk the player is currently in (if it is unclaimed)
		if (label.equalsIgnoreCase("claim")) {
			// if vanilla mode is on or in nether don't run command
			if (vanillaMode) {
				return true;
			}
			
			
			if (p.getWorld().getEnvironment() == Environment.NETHER){
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You cannot claim chunks in the Nether!");
				return true;
			}
			
			
			if (factionHashMap.get(p.getUniqueId().toString()).equals("")) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You're not in a faction! Join a faction to claim chunks!");
				return true;
			}
			
			
			String UUID = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToUUID");


			String chunkName = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".appearsAs");
			String name = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToName");
			String willAppearAs = playerDataConfig.getString("Users." + p.getUniqueId() + "." + "stats" + ".faction");
			
			int chunkClaims = playerDataConfig.getInt("Users." + p.getUniqueId() + ".stats" + ".chunkClaims");
			
			
			if (UUID == null && chunkClaims < 400) {
				chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToUUID", "" + p.getUniqueId());
				chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToName", "" + p.getName());
				chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".appearsAs", "" + willAppearAs);
				
				chunkLocationsRAM.add("X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ());
				chunkNamesRAM.add(willAppearAs);
				
				// this is a safeguard just in case people mess with the server config files
				if (chunkClaims < 0) {
					chunkClaims = 0;
				}
				chunkClaims++;
				playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".chunkClaims", chunkClaims);
				try {
					chunkClaimDataConfig.save(chunkClaimData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // this is important to have when editing server files, otherwise nothing gets changed
				
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Claimed chunk!");
			}
			else if ((UUID == null && chunkClaims >= 400)) {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You cannot claim any more chunks! Level up to get more chunk claims!");
			}
			else if (UUID.equals(p.getUniqueId().toString())) {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You already own this chunk!");
			}
			else{
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Cannot claim chunk! Chunk is owned by " + chunkName + " (" + name + ")");
			}
				
			
			
			
			return true;
		}
		
		// unclaim the chunk the player is currently in (if they own it)
		if (label.equalsIgnoreCase("unclaim")) {
			// if vanilla mode is on or in nether don't run command
			if (vanillaMode) {
				return true;
			}
			if (p.getWorld().getEnvironment() == Environment.NETHER){
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You cannot unclaim chunks in the Nether!");
				return true;
			}
			
			
			String UUID = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToUUID");
			String name = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToName");
			String chunkName = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".appearsAs");
			if (UUID.equals(p.getUniqueId().toString())) {
				chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToUUID", null);
				chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToName", null);
				chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".appearsAs", null);
				int chunkClaims = playerDataConfig.getInt("Users." + p.getUniqueId() + ".stats" + ".chunkClaims");
				chunkClaims--;
				
				try {
					int i = chunkLocationsRAM.indexOf("X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ());
					chunkLocationsRAM.remove(i);
					chunkNamesRAM.remove(i);
				} catch (Exception e){
					getLogger().log(Level.SEVERE, "Chunk to unclaim not found in ArrayList!");
				}
				
				
				// this is a safeguard just in case people mess with the server config files
				if (chunkClaims < 0) {
					chunkClaims = 0;
				}
				playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".chunkClaims", chunkClaims);
				try {
					chunkClaimDataConfig.save(chunkClaimData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // this is important to have when editing server files, otherwise nothing gets changed
				
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Unclaimed chunk!");
			}
			else if(UUID.equals(null)){
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "This chunk has not been claimed!");
			}
			else{
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Cannot unclaim chunk! Chunk is owned by " + chunkName + " (" + name + ")");
			}
			
			return true;
			
		}
		
		
		if (label.equalsIgnoreCase("whochunk")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			
			String name = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToName");
			String chunkName = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".appearsAs");
			if (name == null) {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Chunk is unclaimed!");
			}
			else {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Chunk is owned by " + chunkName + " (" + name + ")");
			}
			
		}
		
		if (label.equalsIgnoreCase("chunkmap")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			if (p.getWorld().getEnvironment() == Environment.NETHER){
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Chunkmap does not work in the Nether!");
				return true;
			}
			
			if (args.length == 0) {
				chunkMapDraw(p);
				return true;
			}
			
			if (args[0].equals("live")) {
				if (args[1].equals("on")) {
					playerDataConfig.set("Users." + p.getUniqueId() + "." + "preferences" + ".chunkMapLive", true);
				}
				if (args[1].equals("off")) {
					playerDataConfig.set("Users." + p.getUniqueId() + "." + "preferences" + ".chunkMapLive", false);
				}
				saveConfig();
			}
			
			return true;
			
			
		}
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// Faction Commands
		if (label.equalsIgnoreCase("faction")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			
			// variable grabbing
			String playerFaction = playerDataConfig.getString("Users." + p.getUniqueId() + ".stats" + ".faction");
			
			
			// BASIC FACTION COMMANDS
			
			
			// create faction
			if (args[0].equals("create")) {
				String factionName = args[1];
				for (int i = 2; i < args.length; i++) {
					factionName += " " + args[i];
				}
				
				if (playerDataConfig.getBoolean("Users." + p.getUniqueId() + ".stats" + ".inFaction")) {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You're already in a faction!");
					
					return true;
				}
				
				ConfigurationSection factions = factionDataConfig.getConfigurationSection("Factions.");
				for (String s : factions.getKeys(false)) {
					if (s.equals(factionName)) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Faction alrady exists!");
						return true;
					}
				}
				
				if (factionName.length() > 20) {
					factionName = factionName.substring(0, 20);
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Faction name cannot be longer than 20 characters!");
					
					return true;
				}
				
				if (factionDataConfig.getString("Factions." + factionName + ".ownerUUID") == null) {
					factionDataConfig.set("Factions." + factionName + ".stats" + ".ownerUUID", "" + p.getUniqueId());
					factionDataConfig.set("Factions." + factionName + ".stats" + ".ownerName", "" + p.getName());
					playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".inFaction", true);
					playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".faction", factionName);
					factionDataConfig.set("Factions." + factionName + ".stats" + ".memberCount", 1);
					factionDataConfig.set("Factions." + factionName + ".members." + p.getUniqueId() + ".name", "" + p.getName());
					factionDataConfig.set("Factions." + factionName + ".members." + p.getUniqueId() + ".rank", "Leader");
					factionHashMap.put(p.getUniqueId().toString(), factionName);

					// save files
					try {
						playerDataConfig.save(playerData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
					
					try {
						factionDataConfig.save(factionData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed

					
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.WHITE + "Created faction " + factionName);
					
					return true;
				}
				
				else {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Faction name is already in use!");
					
					return true;
				}
			}
			
			// invite player to faction
			if (args[0].equals("invite")) {

				if (factionDataConfig.getString("Factions." + playerFaction + ".stats" + ".ownerUUID").equals(p.getUniqueId().toString())){
					String playerName = args[1];
					for (int i = 2; i < args.length; i++) {
						playerName += " " + args[i];
					}
					
					Player playerInvited = Bukkit.getServer().getPlayer(playerName);
					
					
					if (playerDataConfig.getBoolean("Users." + playerInvited.getUniqueId() + ".stats" + ".inFaction") != true) {
						playerDataConfig.set("Users." + playerInvited.getUniqueId() + ".stats" + ".invitedToFaction", true);
						factionDataConfig.set("Factions." + playerFaction + ".members." + playerInvited.getUniqueId() + ".name", "" + playerInvited.getName());
						factionDataConfig.set("Factions." + playerFaction + ".members." + playerInvited.getUniqueId() + ".rank", "invited");
						
						// save files
						try {
							playerDataConfig.save(playerData);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // this is important to have when editing server files, otherwise nothing gets changed
						
						try {
							factionDataConfig.save(factionData);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // this is important to have when editing server files, otherwise nothing gets changed
						
						
						// Commands+ System Message
						playerInvited.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You've been invited to " + ChatColor.AQUA + playerFaction + ChatColor.WHITE + " by " + p.getName() + "! Type /faction accept to join!");
						// Commands+ System Message
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "] " + "Invited " + ChatColor.GREEN + playerInvited.getName() + ChatColor.WHITE + " to " + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "!");
					}
					else {
						// Commands+ System Message
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Player is in another faction!");
					}
					

				}
			}
			
			// accept faction invite
			if (args[0].equals("accept")) {
				String factionName = "";
				ConfigurationSection factions = factionDataConfig.getConfigurationSection("Factions.");
				for (String s : factions.getKeys(false)) {
					if (factions.get(s + ".members." + p.getUniqueId() + ".rank") != null && factions.get(s + ".members." + p.getUniqueId() + ".rank").equals("invited")) {
						factionName = s;
					}
				}
				if (!factionName.equals("")) {
					factionDataConfig.set("Factions." + factionName + ".stats" + ".memberCount", factionDataConfig.getInt("Factions." + factionName + ".stats" + ".memberCount") + 1);
					
					
					playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".inFaction", true);
					playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".faction", factionName);
					playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".invitedToFaction", false);
					factionDataConfig.set("Factions." + factionName + ".members." + p.getUniqueId() + ".rank", "Member");
					factionHashMap.put(p.getUniqueId().toString(), factionName);


					// save files
					try {
						playerDataConfig.save(playerData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
					
					try {
						factionDataConfig.save(factionData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
					
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Joined faction " + ChatColor.AQUA + factionName + ChatColor.WHITE + "!");
					for (Player online : Bukkit.getOnlinePlayers()) {
						if (playerDataConfig.getBoolean("Users." + online.getUniqueId() + ".stats" + ".inFaction") == true && playerDataConfig.getString("Users." + online.getUniqueId() + ".stats" + ".faction").equals(factionName)) {
							online.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + factionName + ChatColor.WHITE + "] " + ChatColor.GREEN + p.getName() + ChatColor.WHITE + " joined the faction!");
						}
					}
					
					
				}
				else {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You haven't been invited to a faction yet!");
				}
				
				// save files
				try {
					playerDataConfig.save(playerData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // this is important to have when editing server files, otherwise nothing gets changed
				
				try {
					factionDataConfig.save(factionData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // this is important to have when editing server files, otherwise nothing gets changed
				
				
			}
			
			// decline faction invite
			if (args[0].equals("decline")) {
				String factionName = "";
				ConfigurationSection factions = factionDataConfig.getConfigurationSection("Factions.");
				for (String s : factions.getKeys(false)) {
					if (factions.get(s + ".members." + p.getUniqueId() + ".rank").equals("invited")) {
						factionName = s;
					}
				}
				
				if (!factionName.equals("")) {
					playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".invitedToFaction", false);
					factionDataConfig.set("Factions." + factionName + ".members." + p.getUniqueId() + ".rank", "none");
					
					saveConfig();
					
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Declined invite from faction " + ChatColor.AQUA + factionName + ChatColor.WHITE + "!");
				}
				else {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You haven't been invited to a faction yet!");
				}
				

			}
			
			
			
			// remove player from faction
			if (args[0].equals("remove")) {
				if (factionDataConfig.getString("Factions." + playerFaction + ".stats" + ".ownerUUID").equals(p.getUniqueId().toString())) {
					String playerName = args[1];
					for (int i = 2; i < args.length; i++) {
						playerName += " " + args[i];
					}
					
					Player playerRemoved = Bukkit.getServer().getPlayer(playerName);
					
					
					// config stuff
					playerDataConfig.set("Users." + playerRemoved.getUniqueId() + ".stats" + ".inFaction", false);
					playerDataConfig.set("Users." + playerRemoved.getUniqueId() + ".stats" + ".invitedToFaction", false);
					playerDataConfig.set("Users." + playerRemoved.getUniqueId() + ".stats" + ".faction", "");
					
					factionDataConfig.set("Factions." + playerFaction + ".members." + playerRemoved.getUniqueId() + ".rank", "none");
					factionDataConfig.set("Factions." + playerFaction + ".stats" + ".memberCount", factionDataConfig.getInt("Factions." + playerFaction + ".stats" + ".memberCount") - 1);
					factionHashMap.put(playerRemoved.getUniqueId().toString(), "");
					unclaimAllChunks(p);
					
					// save files
					try {
						playerDataConfig.save(playerData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
					
					try {
						factionDataConfig.save(factionData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
					
					
					// Commands+ System Messages
					playerRemoved.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You've been removed from " + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "!");
					for (Player online : Bukkit.getOnlinePlayers()) {
						if (playerDataConfig.getBoolean("Users." + online.getUniqueId() + ".stats" + ".inFaction") != false && playerDataConfig.getString("Users." + online.getUniqueId() + ".stats" + ".faction").equals(playerFaction)) {
							online.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "] " + ChatColor.GREEN + playerName + ChatColor.WHITE + " was kicked from the faction!");
						}
					}
				}
				
			}
			
			// leave faction
			if (args[0].equals("leave")) {
				if (playerDataConfig.getBoolean("Users." + p.getUniqueId() + ".stats" + ".inFaction")) {
					
					if (factionDataConfig.get("Factions." + playerFaction + ".stats" + ".ownerUUID").equals(p.getUniqueId().toString())) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You must promote someone before leaving the faction! If you want to disband your faction you can do so by using the '/faction disband' command.");
						return true;
					}
					
					playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".inFaction", false);
					playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".faction", "");
					factionDataConfig.set("Factions." + playerFaction + ".stats" + ".memberCount", factionDataConfig.getInt("Factions." + playerFaction + ".stats" + ".memberCount") - 1);
					factionDataConfig.set("Factions." + playerFaction + ".members." + p.getUniqueId() + ".rank", "none");
					
					
					factionHashMap.put(p.getUniqueId().toString(), "");
					unclaimAllChunks(p);
					
					// save files
					try {
						playerDataConfig.save(playerData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
					
					try {
						factionDataConfig.save(factionData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
				
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You've left " + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "!");
					
					for (Player online : Bukkit.getOnlinePlayers()) {
						if (playerDataConfig.getBoolean("Users." + online.getUniqueId() + ".stats" + ".inFaction") != false && playerDataConfig.getString("Users." + online.getUniqueId() + ".stats" + ".faction").equals(playerFaction)) {
							online.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "] " + ChatColor.GREEN + p.getName() + ChatColor.WHITE + " has left the faction!");
						}
					}
				}
				else {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You're not in a faction!");
				}
				
			}
			
			if (args[0].equals("info")) {
				String factionName = "";
				if (args.length == 1 && playerDataConfig.getBoolean("Users." + p.getUniqueId() + ".stats" + ".inFaction") != true) {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Type in a faction name to see stats!");
					return true;
				}
				if (args.length == 1) {
					factionName = playerDataConfig.getString("Users." + p.getUniqueId() + ".stats" + ".faction");
				}
				else {
					factionName = args[1];
					for (int i = 2; i < args.length; i++) {
						factionName += " " + args[i];
					}
				}
				
				if (!factionDataConfig.contains("Factions." + factionName)) {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Faction does not exist!");
					return true;
				}
				
				
				ConfigurationSection faction = factionDataConfig.getConfigurationSection("Factions." + factionName + ".members");
				String factionMembers = "";
				
				for (String s : faction.getKeys(false)) {
					if (!(faction.get(s + ".rank")).equals(null) && !(faction.get(s + ".rank")).equals("invited") && !(faction.get(s + ".rank")).equals("none")) {
						factionMembers += faction.get(s + ".name") + ", ";
					}
				}
				factionMembers = factionMembers.substring(0, factionMembers.length() - 2);
				
				// Commands+ System Messages
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Faction Info");
				p.sendMessage("--------------------------------");
				p.sendMessage("Name: " + ChatColor.AQUA + factionName);
				p.sendMessage("Members: " + ChatColor.AQUA + factionMembers);

				
			}
			
			// MEMBER FACTION COMMANDS
			
			if (args[0].equals("msg")) {
				if (factionHashMap.get(p.getUniqueId().toString()).equals("")) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You must be in a faction to use messages!");
					return true;
				}
				
				String msg = "";
				if (args.length == 1) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You need to eneter a message!");
					return true;
				}
				else {
					msg = args[1];
					for (int i = 2; i < args.length; i++) {
						msg += " " + args[i];
					}
				}
				
				for (Player online : Bukkit.getOnlinePlayers()) {
					if (factionHashMap.get(p.getUniqueId().toString()).equals(factionHashMap.get(online.getUniqueId().toString()))) {
						online.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "] " + ChatColor.GREEN + p.getName() + ChatColor.WHITE + ": " + msg);
					}
				}
			}
			
			// ADMIN FACTION COMMANDS
			
			// promote faction member
			if (args[0].equals("promote")) {
				if (factionDataConfig.getString("Factions." + playerFaction + ".stats" + ".ownerUUID").equals(p.getUniqueId().toString())) {
					String playerName = args[1];
					for (int i = 2; i < args.length; i++) {
						playerName += " " + args[i];
					}
					
					Player playerPromoted = Bukkit.getServer().getPlayer(playerName);
					
					if (!factionHashMap.get(p.getUniqueId().toString()).equals(factionHashMap.get(playerPromoted.getUniqueId().toString()))) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Player is not in faction!");
						return true;
					}
					
					if (factionDataConfig.get("Factions." + playerFaction + ".members." + playerPromoted.getUniqueId() + ".rank").equals("Member")) {
						factionDataConfig.set("Factions." + playerFaction + ".members." + playerPromoted.getUniqueId() + ".rank", "Admin");
					}

					if (factionDataConfig.get("Factions." + playerFaction + ".members." + playerPromoted.getUniqueId() + ".rank").equals("Admin")) {
						factionDataConfig.set("Factions." + playerFaction + ".members." + playerPromoted.getUniqueId() + ".rank", "Leader");
						factionDataConfig.set("Factions." + playerFaction + ".members." + p.getUniqueId() + ".rank", "Admin");
						factionDataConfig.set("Factions." + playerFaction + ".stats" + ".ownerUUID", playerPromoted.getUniqueId());
						factionDataConfig.set("Factions." + playerFaction + ".stats" + ".ownerName", playerPromoted.getName());
					}
					
					
					// save files
					try {
						playerDataConfig.save(playerData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
					
					try {
						factionDataConfig.save(factionData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
				}
			}
			
			// demote faction member
			if (args[0].equals("demote")) {
				if (factionDataConfig.getString("Factions." + playerFaction + ".stats" + ".ownerUUID").equals(p.getUniqueId().toString())) {
					String playerName = args[1];
					for (int i = 2; i < args.length; i++) {
						playerName += " " + args[i];
					}
					
					Player playerDemoted = Bukkit.getServer().getPlayer(playerName);
					
					if (!factionHashMap.get(p.getUniqueId().toString()).equals(factionHashMap.get(playerDemoted.getUniqueId().toString()))) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Player is not in faction!");
						return true;
					}
					
					if (factionDataConfig.get("Factions." + playerFaction + ".members." + playerDemoted.getUniqueId() + ".rank").equals("Member")) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Cannot demote a player with the rank of member! If you want to remove this player from your faction, you can do so with the '/faction remove <player>' command!");
						return true;
					}

					if (factionDataConfig.get("Factions." + playerFaction + ".members." + playerDemoted.getUniqueId() + ".rank").equals("Admin")) {
						factionDataConfig.set("Factions." + playerFaction + ".members." + playerDemoted.getUniqueId() + ".rank", "Member");
					}
					
					
					// save files
					try {
						playerDataConfig.save(playerData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
					
					try {
						factionDataConfig.save(factionData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
				}
						
			}
			
			// disbands faction
			if (args[0].equals("disband")) {
				String factionName = factionHashMap.get(p.getUniqueId().toString());
				for (Player online : Bukkit.getOnlinePlayers()) {
					if (factionHashMap.get(p.getUniqueId().toString()).equals(factionHashMap.get(online.getUniqueId().toString()))) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Faction has been disbanded!");
						factionHashMap.put(online.getUniqueId().toString(), null);
						playerDataConfig.set("Users." + online.getUniqueId() + ".stats" + ".inFaction", false);
						playerDataConfig.set("Users." + online.getUniqueId() + ".stats" + ".faction", "");
						unclaimAllChunks(online);
					}
				}
				factionDataConfig.set("Factions." + factionName, null);
				
				// save files
				try {
					playerDataConfig.save(playerData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // this is important to have when editing server files, otherwise nothing gets changed
				
				try {
					factionDataConfig.save(factionData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // this is important to have when editing server files, otherwise nothing gets changed
			}
			
			// get interactive list of faction members
			if (args[0].equals("memberslist")) {
				;
			}
			
			// get interactive UI for a specified faction member
			if (args[0].equals("getmember")) {
				;
			}
			
			
			

			
			return true;
			
		}
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// Talent Commands
		if (label.equalsIgnoreCase("talents")){
			p.openInventory(talentInventory);
		}
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// Custom Item Commands
		
		if (label.equalsIgnoreCase("bonkstick") && (p.getUniqueId().toString()).equals("b859c378-5ea4-47f8-b7e4-fc80bfc1b713")) {
			
			ItemStack item = new ItemStack(ItemsPlus.bonkStick);
			
			p.getInventory().addItem(item);

		}
		
		if (label.equalsIgnoreCase("yeehaw")) {
			
			Horse horse = (Horse) p.getWorld().spawn(p.getLocation().add(Math.cos(FunctionsPlus.getPlayerDirectionFloat(p)) * 2, 0, Math.sin(FunctionsPlus.getPlayerDirectionFloat(p)) * 2), Horse.class);
			
			horse.setTamed(true);
			horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_HORSE_ARMOR));
			horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
			horse.setAdult();
			horse.setColor(Color.BLACK);
			
			horse.setJumpStrength(2.0);
			horse.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 255));
			horse.setHealth(horse.getMaxHealth());
			horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.6);
			
			

		}
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// Miscellaneous Commands
		if (label.equalsIgnoreCase("credits")){
			p.sendMessage(ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+ " + ChatColor.WHITE + "Credits:");
			p.sendMessage(ChatColor.WHITE + "Base plugin - huge thanks to everyone who worked");
			p.sendMessage(ChatColor.WHITE + "in the beta stages of the plugin to work out the kinks!");
			p.sendMessage(ChatColor.WHITE + "");
			p.sendMessage(ChatColor.WHITE + "Factions Update - Warlocck, I cannot thank you enough");
			p.sendMessage(ChatColor.WHITE + "for your patience during the testing of this system.");
			p.sendMessage(ChatColor.WHITE + "");
			p.sendMessage(ChatColor.WHITE + "Items+, Enchantments+, and Skills Updates - the biggest");
			p.sendMessage(ChatColor.WHITE + "inspiration was Hypixel Skyblock, if any of those");
			p.sendMessage(ChatColor.WHITE + "developers see this, huge thanks to you guys!");
			p.sendMessage(ChatColor.WHITE + "");
			p.sendMessage(ChatColor.WHITE + "Talents Update - the Origins mod was a huge inspiration");
			p.sendMessage(ChatColor.WHITE + "for this project, Apace100, thank you for that awesome mod!");
		}
		
		return false;
		
	}
	
}




// Commands+, made by Joey Balardeta