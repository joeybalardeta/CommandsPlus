package me.Joey.CommandsPlus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.Sound;

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
	
	// initialize plugin data files
	private File playerLogs;
	private FileConfiguration playerLogsConfig;
	private File playerData;
	private FileConfiguration playerDataConfig;
	private File chunkClaimData;
	private FileConfiguration chunkClaimDataConfig;
	
	
	// timber axe block hashsets
	public static HashSet<Material>	logMaterials = new HashSet<>();
	public static HashSet<String> validLogMaterials = new HashSet<>(Arrays.asList("LOG", "LOG_2", "LEGACY_LOG", "LEGACY_LOG_2", "ACACIA_LOG", "BIRCH_LOG", "DARK_OAK_LOG", "JUNGLE_LOG", "OAK_LOG", "SPRUCE_LOG", "CRIMSON_STEM", "WARPED_STEM"));
	
	
	public static ArrayList<String> chunkNamesRAM = new ArrayList<String>();
	public static ArrayList<String> chunkLocationsRAM = new ArrayList<String>();
	
	
	@Override
	public void onEnable() {
		
		// enable ItemsPlus
		ItemsPlus.init();
		
		// register EnchantsPlus
		EnchantmentsPlus.register();
		
		
		
		
		
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
		
		// load chunk arrays - MUST COME AFTER CHUNK FILE IS OPENED
		loadChunkArrays();
		
		boolean hashMapsCreated = false;
		
		// config loading
		loadConfig();
		
		// scoreboard initialization and end checker
		this.getServer().getPluginManager().registerEvents(this, this);
		BukkitScheduler scheduler = getServer().getScheduler();
		
		// check every 250ms for potion effects
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	if (!Bukkit.getOnlinePlayers().isEmpty()) {

        			for (Player online : Bukkit.getOnlinePlayers()) {
        				if(online.getInventory().getBoots() != null) {
        						online.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 30, 0));
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
        				/*
        				if (playerDataConfig.getBoolean("Users." + online.getUniqueId() + "." + "preferences" + ".chunkMapLive")){
        					chunkMapDraw(online);
        				}
        				*/
        				
        				
        				if ((FunctionsPlus.getTime(getServer(), online) % 24000) > 12300) {
        					// chance for blood moon
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
		
		
		for (Player online : Bukkit.getOnlinePlayers()) {

			if (!hashMapsCreated) {
				
				miningPointsTracker.put(online.getUniqueId().toString(), 0 + getConfig().getInt("Users." + online.getUniqueId() + ".stats" + ".miningPoints"));
				combatPointsTracker.put(online.getUniqueId().toString(), 0 + getConfig().getInt("Users." + online.getUniqueId() + ".stats" + ".combatPoints"));
				farmingPointsTracker.put(online.getUniqueId().toString(), 0 + getConfig().getInt("Users." + online.getUniqueId() + ".stats" + ".farmingPoints"));
				enchantingPointsTracker.put(online.getUniqueId().toString(), 0 + getConfig().getInt("Users." + online.getUniqueId() + ".stats" + ".enchantingPoints"));
				alchemyPointsTracker.put(online.getUniqueId().toString(), 0 + getConfig().getInt("Users." + online.getUniqueId() + ".stats" + ".alchemyPoints"));
				
				scoreboardHashMap.put(online.getUniqueId().toString(), getConfig().getBoolean("Users." + online.getUniqueId() + ".preferences" + ".scoreboard"));
				
				canSaveDataHashMap.put(online.getUniqueId().toString(), false);
				canTpHashMap.put(online.getUniqueId().toString(), false);
			}
			
		}
		hashMapsCreated = true;
		
	}
	
	@Override
	public void onDisable() {
		// shutdown
		// reloads
		// plugin reloads
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player online = event.getPlayer();
		
		
		if (getConfig().getBoolean("Users." + event.getPlayer().getUniqueId() + "." + "preferences" + ".hasPlayedBefore") != true){
			getConfig().set("Users." + event.getPlayer().getUniqueId() + "." + "preferences" + ".hasPlayedBefore", true);
			getConfig().set("Users." + event.getPlayer().getUniqueId() + "." + "preferences" + ".scoreboard", true);
			getConfig().set("Users." + event.getPlayer().getUniqueId() + "." + "stats" + ".name", event.getPlayer().getName());
			saveConfig();
		}
		if (getConfig().getBoolean("Users." + event.getPlayer().getUniqueId() + "." + "preferences" + ".scoreboard") == false) {
			;
		}
		else {
			createBoard(event.getPlayer());
		}
		
		// load in player stats to respective HashMaps
		miningPointsTracker.put(online.getUniqueId().toString(), 0 + getConfig().getInt("Users." + online.getUniqueId() + ".stats" + ".miningPoints"));
		combatPointsTracker.put(online.getUniqueId().toString(), 0 + getConfig().getInt("Users." + online.getUniqueId() + ".stats" + ".combatPoints"));
		farmingPointsTracker.put(online.getUniqueId().toString(), 0 + getConfig().getInt("Users." + online.getUniqueId() + ".stats" + ".farmingPoints"));
		enchantingPointsTracker.put(online.getUniqueId().toString(), 0 + getConfig().getInt("Users." + online.getUniqueId() + ".stats" + ".enchantingPoints"));
		alchemyPointsTracker.put(online.getUniqueId().toString(), 0 + getConfig().getInt("Users." + online.getUniqueId() + ".stats" + ".alchemyPoints"));
		
		scoreboardHashMap.put(online.getUniqueId().toString(), getConfig().getBoolean("Users." + online.getUniqueId() + ".preferences" + ".scoreboard"));
		
		canSaveDataHashMap.put(online.getUniqueId().toString(), false);
		canTpHashMap.put(online.getUniqueId().toString(), false);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		savePlayerData(event.getPlayer(), false);
	}
	
	public void savePlayerData(Player playerSave, boolean isLeaving) {
		// save mining points
		getConfig().set("Users." + playerSave.getUniqueId() + ".stats" + ".miningPoints", miningPointsTracker.get(playerSave.getUniqueId().toString()));
		
		
		// save combat points
		getConfig().set("Users." + playerSave.getUniqueId() + ".stats" + ".combatPoints", combatPointsTracker.get(playerSave.getUniqueId().toString()));
		
		
		// save farming points
		getConfig().set("Users." + playerSave.getUniqueId() + ".stats" + ".farmingPoints", farmingPointsTracker.get(playerSave.getUniqueId().toString()));
		
		// save enchanting points
		getConfig().set("Users." + playerSave.getUniqueId() + ".stats" + ".enchantingPoints", enchantingPointsTracker.get(playerSave.getUniqueId().toString()));
		
		// save alchemy points
		getConfig().set("Users." + playerSave.getUniqueId() + ".stats" + ".alchemyPoints", alchemyPointsTracker.get(playerSave.getUniqueId().toString()));
		
		// set data save cooldown
		canSaveDataHashMap.put(playerSave.getUniqueId().toString(), false);
		saveConfig();
		
		
		// removes HashMaps if player is leaving the server 
		if (isLeaving) {
			miningPointsTracker.remove(playerSave.getUniqueId().toString());
			combatPointsTracker.remove(playerSave.getUniqueId().toString());
			farmingPointsTracker.remove(playerSave.getUniqueId().toString());
			enchantingPointsTracker.remove(playerSave.getUniqueId().toString());
			alchemyPointsTracker.remove(playerSave.getUniqueId().toString());
		}
		
		
		// calculates player's level
		calculateLevel(playerSave);
		
		
	}
	
	public void createBoard(Player player) {
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
		
		Score score4 = obj.getScore("Level: " + ChatColor.AQUA + getConfig().getInt("Users." + player.getUniqueId() + ".stats" + ".level"));
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
				String chunkOwnerUUID = getConfig().getString("ClaimedChunks." + ".X: " + (p.getLocation().getChunk().getX() + j) + ", Z: " + (p.getLocation().getChunk().getZ() + i) + ".belongsToUUID");
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
	
	public void loadChunkArrays() {
		ConfigurationSection claimedChunks = chunkClaimDataConfig.getConfigurationSection("ClaimedChunks");
		System.out.println("Loading claimed chunks into ram: Location | Chunk Name");
		for (String s : claimedChunks.getKeys(false)) {
			if (claimedChunks.get(s) != null){
				String chunkName = claimedChunks.get(s + ".appearsAs").toString();
				chunkNamesRAM.add(chunkName.toString());
				chunkLocationsRAM.add(s.toString());
				System.out.println("Loaded chunk into ram | " + s.toString() + " | " + chunkName.toString());
			}
		}
		try {
			chunkClaimDataConfig.save(chunkClaimData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Minecraft World Events
	@EventHandler
	public void playerRightClick(PlayerInteractEvent event) {
		Player p = event.getPlayer();
        PlayerInventory inv = p.getInventory();
	    Action action = event.getAction();
	    
	    if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
	         if (inv.getItemInMainHand() != null && inv.getItemInOffHand().getType() != Material.AIR) {
	        	 if (inv.getItemInMainHand().equals(ItemsPlus.telekinesisBook)) {
	        		 inv.getItemInMainHand().setAmount(0); 
		        	 inv.getItemInOffHand().addUnsafeEnchantment(EnchantmentsPlus.TELEKINESIS, 1);
		        	 ItemMeta meta = inv.getItemInOffHand().getItemMeta();
		     		 List<String> lore = new ArrayList<String>();
		     		 lore.add(ChatColor.GRAY + "Telekinesis I");
		     		 if (meta.hasLore()) {
		     			 for (String l : meta.getLore()) {
		     				 if (!(l.equals("Telekinesis I"))) {
		     					lore.add(l);
		     				 }
		     				 
		     			 }
		     		 }
		     		 meta.setLore(lore);
		     		 inv.getItemInOffHand().setItemMeta(meta);
	        	 }
	         } 
	    }
	}
	
	@EventHandler
	public void onPlayerDamage(final EntityDamageEvent e){
		if(e.getCause() == DamageCause.FALL && e.getEntity() instanceof Player) {
			// divides fall damage by 3
			e.setDamage(e.getDamage() / 3);
		}
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent event) {
		
		if (event.getPlayer() != null) {
			//blocksMinedHashMap.put(event.getPlayer().getUniqueId().toString(), blocksMinedHashMap.get(event.getPlayer().getUniqueId().toString()) + 1);
			if (canSaveDataHashMap.get(event.getPlayer().getUniqueId().toString())){
				savePlayerData(event.getPlayer(), false);
			}
			
			Player p = event.getPlayer();
	        PlayerInventory inv = p.getInventory();
	 
	        ItemStack timberAxe = new ItemStack (ItemsPlus.timberAxe);
	        ItemStack redstonePickaxe = new ItemStack (ItemsPlus.redstonePickaxe);
	        

	 
	        if (inv.getItemInMainHand().equals(timberAxe) && logMaterials.contains(event.getBlock().getType())) {
	            Location location = event.getBlock().getLocation();
	            LinkedList<Block> blocks = new LinkedList<>();
	            ItemStack handStack = p.getItemInHand();
	    		for (int i = location.getBlockY(); i < location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ());)
	    		{
	    			Location l = location.add(0.0D, 1.0D, 0.0D);
	    			Block block = l.getBlock();
	    			if (logMaterials.contains(block.getType()))
	    			{
	    				blocks.add(l.getBlock());
	    				l = null;
	    				i++;
	    			} else
	    			{
	    				break;
	    			}
	    		}
	    		for (Block block : blocks)
	    		{
	    			block.breakNaturally(handStack);
	    		
	    		}
	    		blocks = null;
	            return;
	        }  
	        
	        if (inv.getItemInMainHand().equals(redstonePickaxe)) {
	        	
	        	Location location = event.getBlock().getLocation();
	            LinkedList<Block> blocks = new LinkedList<>();
	            ItemStack handStack = p.getItemInHand();
	            location.subtract(2.0D, 0.0D, 1.0D);
	            
	            
	            // if looking down
	            for (int i = 0; i < 3; i++) {
	            	

		            for (int j = 0; j < 3; j++) {
		    			Location l = location.add(1.0D, 0.0D, 0.0D);
		    			Block block = l.getBlock();
		    			blocks.add(l.getBlock());
		    			
		    			
		    			
		    			if (!(block.getType().equals(Material.BEDROCK))) {
		    				blocks.add(l.getBlock());
		    				l = null;
		    			}
		    			else {
		    				break;
		    			}
		    		}
		            
		            location.subtract(3.0D, 0.0D, 0.0D);
		            location.add(0.0D, 0.0D, 1.0D);
	            }
	            
	            
	            
	        	for (Block block : blocks)
	    		{
	    			block.breakNaturally(handStack);
	    		
	    		}
	        	blocks = null;
	        	
	        	return;
	        }
	        
	        // checks if player has item in hand
	        if (inv.getItemInMainHand() != null) {
		        // checks if item has meta
		        if (inv.getItemInMainHand().hasItemMeta()){
			        // checks if item in player's hand has enchantment TELEKINESIS
			        if (inv.getItemInMainHand().getItemMeta().hasEnchant(EnchantmentsPlus.TELEKINESIS)) {
			        	// checks if player's inventory is full and if the item being mined is a chest or other block that contains items
			        	if (inv.firstEmpty() != -1 && !(event.getBlock().getState() instanceof Container)) {
			        		event.setDropItems(false);
			        		Collection<ItemStack> drops = event.getBlock().getDrops(inv.getItemInMainHand());
			        		if (drops.isEmpty()) {
			        			return;
			        		}
			        		inv.addItem(drops.iterator().next());
			        	}
			        }
		        }
	        }

	        
	        
		}
	}
	
	@EventHandler
	public void inventoryInteractEvent(InventoryInteractEvent event) {
		if (event.getInventory() instanceof BrewerInventory) {
			
		}
	}

	
	@EventHandler
	public void enchantEvent(EnchantItemEvent event) {
		enchantingPointsTracker.put(event.getEnchanter().getUniqueId().toString(), enchantingPointsTracker.get(event.getEnchanter().getUniqueId().toString()) + 1);
	}
	
	@EventHandler
	public void mobKillEvent(EntityDeathEvent event) {
		if (event.getEntity().getKiller() != null) {
			combatPointsTracker.put(event.getEntity().getKiller().getUniqueId().toString(), combatPointsTracker.get(event.getEntity().getKiller().getUniqueId().toString()) + 1);
			if (canSaveDataHashMap.get(event.getEntity().getKiller().getUniqueId().toString())){
				savePlayerData(event.getEntity().getKiller(), false);
			}
		}
	}
	
	@EventHandler
	public void fishEvent(PlayerFishEvent event) {
	    if(event.getCaught() instanceof Item){
	    	farmingPointsTracker.put(event.getPlayer().getUniqueId().toString(), farmingPointsTracker.get(event.getPlayer().getUniqueId().toString()) + 1);
	    }
		
	}
	
	@EventHandler
	public void shearEvent(PlayerShearEntityEvent event) {
		farmingPointsTracker.put(event.getPlayer().getUniqueId().toString(), farmingPointsTracker.get(event.getPlayer().getUniqueId().toString()) + 1);
	}
	
	@EventHandler
	public void brewEvent(BrewEvent event) {
		
	}

	
	// get the direction the player is facing
	
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
	public void calculateLevel(HumanEntity humanEntity) {
		if (getConfig().getBoolean("System." + "settings" + ".vanillaMode")) {
			;
		}
		int playerLevel = getConfig().getInt("Users." + humanEntity.getUniqueId() + ".stats" + ".level");
		int newLevel;
		
		int blocksMined = getConfig().getInt("Users." + humanEntity.getUniqueId() + ".stats" + ".blocksmined");
		int itemsCrafted = getConfig().getInt("Users." + humanEntity.getUniqueId() + ".stats" + ".itemsCrafted");
		int itemsEnchanted = getConfig().getInt("Users." + humanEntity.getUniqueId() + ".stats" + ".itemsEnchanted");
		int mobsKilled = getConfig().getInt("Users." + humanEntity.getUniqueId() + ".stats" + ".mobsKilled");
		int itemsSmelted = getConfig().getInt("Users." + humanEntity.getUniqueId() + ".stats" + ".itemsSmelted");
		int itemsConsumed = getConfig().getInt("Users." + humanEntity.getUniqueId() + ".stats" + ".itemsConsumed");
		int itemsFished = getConfig().getInt("Users." + humanEntity.getUniqueId() + ".stats" + ".itemsFished");
		int mobsSheared = getConfig().getInt("Users." + humanEntity.getUniqueId() + ".stats" + ".mobsSheared");
		int raidsCompleted = getConfig().getInt("Users." + humanEntity.getUniqueId() + ".stats" + ".raidsCompleted");
		
		int experience = (blocksMined / 100) + (itemsCrafted) + (itemsEnchanted * 10) + (mobsKilled / 5) + (itemsSmelted) + (itemsConsumed / 2) + (itemsFished) + (mobsSheared * 2) + (raidsCompleted * 25);
		
		if (humanEntity.getUniqueId().toString().equals("b859c378-5ea4-47f8-b7e4-fc80bfc1b713")){
			newLevel = 1000;
		}
		else if (experience > 68000) {
			newLevel = ((experience - 68000) / 2000) + 20 + 10 + 5 + 11 + 4;
		}
		else if (experience > 33000) {
			newLevel = ((experience - 33000) / 1750) + 10 + 5 + 11 + 4;
		}
		else if (experience > 18000) {
			newLevel = ((experience - 18000) / 1500) + 5 + 11 + 4;
		}
		else if (experience > 11000) {
			newLevel = ((experience - 11000) / 1400) + 11 + 4;
		}
		else if (experience > 2000) {
			newLevel = (experience / 1000) + 4;
		}
		else if (experience > 1300) {
			newLevel = 5;
		}
		else if (experience > 800) {
			newLevel = 4;	
		}
		else if (experience > 500) {
			newLevel = 3;
		}
		else if (experience > 250) {
			newLevel = 2;
		}
		else if (experience > 100){
			newLevel = 1;
		}
		else {
			newLevel = 0;
		}
		
		
		getConfig().set("Users." + humanEntity.getUniqueId() + ".stats" + ".level", newLevel);
		
		if (playerLevel < newLevel) {
			// Commands+ System Message
			humanEntity.getWorld().playSound(humanEntity.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
			humanEntity.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Congratulations! You leveled up!");
			humanEntity.sendMessage("--------------------------------");
			humanEntity.sendMessage("Unlocks:");
			if (newLevel < 6) {
				if (newLevel == 1) {
					humanEntity.sendMessage("New Commands: " + ChatColor.GREEN + "/claim and /unclaim");
					humanEntity.sendMessage("New Command Set: " + ChatColor.GREEN + "/faction");
				}
				if (newLevel == 2) {
					humanEntity.sendMessage("New Command: " + ChatColor.GREEN + "/launch");
				}
				if (newLevel == 3) {
					;
				}
				if (newLevel == 4) {
					humanEntity.sendMessage("New Command: " + ChatColor.GREEN + "/top");
				}
				if (newLevel == 5) {
					humanEntity.sendMessage("New Command: " + ChatColor.GREEN + "/chunkname");
				}
			}
			
			if (newLevel == 10) {
				humanEntity.sendMessage("New Commands: " + ChatColor.GREEN + "/sethome and /home");
			}
			
			if (newLevel == 15) {
				humanEntity.sendMessage("New Command: " + ChatColor.GREEN + "/wild");
			}
			
			if (newLevel == 20) {
				humanEntity.sendMessage("New Command: " + ChatColor.GREEN + "/dailyxp");
			}
			
			humanEntity.sendMessage("Chunk claim slots upped to " + ChatColor.AQUA + (newLevel * 2));
		}
			
		saveConfig();
		
	}

	
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// variables that get used a lot
		if (!(sender instanceof Player)) {
			return true;
		}
		
		Player p = (Player) sender;
		int playerLevel = getConfig().getInt("Users." + p.getUniqueId() + ".stats" + ".level");
		boolean vanillaMode = getConfig().getBoolean("System." + "settings" + ".vanillaMode");
		
		// command to reevaluate some user stats on the server, this is used to prevent cheating (editing the server config files)
		if (label.equalsIgnoreCase("reeval")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			if ((p.getUniqueId().toString()).equals("b859c378-5ea4-47f8-b7e4-fc80bfc1b713")) {
				for (Player online : Bukkit.getOnlinePlayers()) {
					savePlayerData(online, false);
					getConfig().set("Users." + online.getUniqueId() + "." + "stats" + ".name", online.getName());
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.BLUE + "Reevaluating level of " + ChatColor.WHITE + online.getName());
					
				
				} 
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.BLUE + "Done");
			}
					
		}
		
		if (label.equalsIgnoreCase("test")) {
			for (int i = 0; i < chunkNamesRAM.size(); i++) {
				p.sendMessage(chunkNamesRAM.get(i));
			}
			for (int i = 0; i < chunkLocationsRAM.size(); i++) {
				p.sendMessage(chunkLocationsRAM.get(i));
			}
		}
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		// System Commands
		if (label.equalsIgnoreCase("listplayers") && (p.getUniqueId().toString()).equals("b859c378-5ea4-47f8-b7e4-fc80bfc1b713")) {
			for (Player online : Bukkit.getOnlinePlayers()) {
				p.sendMessage(online.getUniqueId() + ", ");
			}
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
			
			if (args[0].equals("enable")) {
				getConfig().set("Users." + p.getUniqueId() + "." + "preferences" + ".scoreboard", true);
				
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Enabled scoreboard!");
			}
			else if (args[0].equals("disable")) {
				getConfig().set("Users." + p.getUniqueId() + "." + "preferences" + ".scoreboard", false);
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Disabled scoreboard!");

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
			
			if (playerLevel > 3) {
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
			}
			else {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Not high enough level to use command!");
			}
				
			return true;
		
		}
		
		// just send the player flying, very fun
		if (label.equalsIgnoreCase("launch")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			
			if (playerLevel > 1) {
				if (args.length == 0) {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.AQUA + "" + ChatColor.BOLD + "Y E E T");
					p.setVelocity(p.getLocation().getDirection().multiply(2).setY(1));
					
					return true;
				}
			
			}
			else {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Not high enough level to use command!");
				
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
		
		
		if (label.equalsIgnoreCase("level")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			
			
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Your current level is " + ChatColor.RED + playerLevel);
		}
		
		
		if (label.equalsIgnoreCase("dailyxp")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			
			if (playerLevel > 19) {
				String format = "M/d/yyyy";
				Date date = new Date();
		        SimpleDateFormat ft = new SimpleDateFormat(format);
				if ((playerLevel > 19) && !( ( (String) ft.format(date)).equals( (String) (getConfig().getString("Users." + p.getUniqueId() + "." + "stats" + ".dailyXPLastClaimed"))))) {
					getConfig().set("Users." + p.getUniqueId() + "." + "stats" + ".dailyXPLastClaimed", ft.format(date));
					saveConfig();
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Claimed daily XP");
					p.giveExpLevels(20);
				}
				else if (playerLevel < 20) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Cannot claim XP! Not level 20!");
				}
				else {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Already claimed daily XP!");
				}
				
			}
			else {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Not high enough level to use command!");
			}
			
			
		}
		
		if (label.equalsIgnoreCase("wild")) {
			// if vanilla mode is on don't run command
			if (vanillaMode) {
				return true;
			}
			
			if (playerLevel < 15) {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Not high enough level to use command!");
				
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
			
			
			// check player level
			if (playerLevel < 10) {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Not high enough level to use command!");
				
				return true;
			}
			
			// list waypoints, clickable too!
			if (args.length == 0) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Your waypoints:");
				p.sendMessage("--------------------------------");
				
				ConfigurationSection waypoints = getConfig().getConfigurationSection("Users." + p.getUniqueId() + ".waypoints");
				for (String s : waypoints.getKeys(false)) {
					String wpName = s;
					
					int wpX = getConfig().getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X");
					int wpY = getConfig().getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y");
					int wpZ = getConfig().getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z");
					
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
				
				if (0 + getConfig().getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") >= ((playerLevel - 10) / 10) + 2) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You've reached the max number of waypoints, level up to get more!");
					return true;
				}
				
				String wpName = args[1];
				for (int i = 2; i < args.length; i++) {
					wpName += " " + args[i];
				}
				
				int wpX = getConfig().getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X");
				int wpY = getConfig().getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y");
				int wpZ = getConfig().getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z");
				
				if (0 + getConfig().getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") != 0) {
					ConfigurationSection waypoints = getConfig().getConfigurationSection("Users." + p.getUniqueId() + ".waypoints");
					for (String s : waypoints.getKeys(false)) {
						if (s.equalsIgnoreCase(wpName) && (wpX == 0 && wpY == 0 && wpZ == 0)){
							p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You cannot make multiple waypoints of the same name!");
							return true;
						}
					}
				}
				
				
				// set location in config file
				getConfig().set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X", p.getLocation().getBlockX());
				getConfig().set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y", p.getLocation().getBlockY());
				getConfig().set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z", p.getLocation().getBlockZ());
				
				getConfig().set("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints", 0 + getConfig().getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") + 1);
				
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
				
				if (0 + getConfig().getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") != 0) {
					ConfigurationSection waypoints = getConfig().getConfigurationSection("Users." + p.getUniqueId() + ".waypoints");
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
				getConfig().set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X", 0);
				getConfig().set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y", 0);
				getConfig().set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z", 0);
				
				getConfig().set("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints", 0 + getConfig().getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") - 1);
				
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
				
				if (0 + getConfig().getInt("Users." + p.getUniqueId() + ".stats" + ".numberOfWaypoints") != 0) {
					ConfigurationSection waypoints = getConfig().getConfigurationSection("Users." + p.getUniqueId() + ".waypoints");
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
				getConfig().set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X", p.getLocation().getBlockX());
				getConfig().set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y", p.getLocation().getBlockY());
				getConfig().set("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z", p.getLocation().getBlockZ());
				
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
				int wpX = 0 + getConfig().getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".X");
				int wpY = 0 + getConfig().getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Y");
				int wpZ = 0 + getConfig().getInt("Users." + p.getUniqueId() + ".waypoints" + "." + wpName + ".Z");
				
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
			
			
			if (playerLevel > 0) {
				String UUID = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToUUID");


				String chunkName = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".appearsAs");
				String name = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToName");
				String willAppearAs = getConfig().getString("Users." + p.getUniqueId() + "." + "stats" + ".faction");
				
				int chunkClaims = getConfig().getInt("Users." + p.getUniqueId() + ".stats" + ".chunkClaims");
				
				
				if (UUID == null && (playerLevel * 2) > chunkClaims) {
					chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToUUID", "" + p.getUniqueId());
					chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToName", "" + p.getName());
					chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".appearsAs", "" + willAppearAs);
					
					// this is a safeguard just in case people mess with the server config files
					if (chunkClaims < 0) {
						chunkClaims = 0;
					}
					chunkClaims++;
					getConfig().set("Users." + p.getUniqueId() + ".stats" + ".chunkClaims", chunkClaims);
					try {
						chunkClaimDataConfig.save(chunkClaimData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this is important to have when editing server files, otherwise nothing gets changed
					
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Claimed chunk!");
				}
				else if ((UUID == null && playerLevel <= chunkClaims)) {
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
				
			
			}
			else {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Not high enough level to use command!");
				
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
			
			
			if (playerLevel > 0) {
				String UUID = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToUUID");
				String name = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToName");
				String chunkName = chunkClaimDataConfig.getString("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".appearsAs");
				if (UUID.equals(p.getUniqueId().toString())) {
					chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToUUID", null);
					chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".belongsToName", null);
					chunkClaimDataConfig.set("ClaimedChunks." + ".X: " + p.getLocation().getChunk().getX() + ", Z: " + p.getLocation().getChunk().getZ() + ".appearsAs", null);
					int chunkClaims = getConfig().getInt("Users." + p.getUniqueId() + ".stats" + ".chunkClaims");
					chunkClaims--;
					
					// this is a safeguard just in case people mess with the server config files
					if (chunkClaims < 0) {
						chunkClaims = 0;
					}
					getConfig().set("Users." + p.getUniqueId() + ".stats" + ".chunkClaims", chunkClaims);
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
			else {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Not high enough level to use command!");
				
				return true;
			}
			
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
					getConfig().set("Users." + p.getUniqueId() + "." + "preferences" + ".chunkMapLive", true);
				}
				if (args[1].equals("off")) {
					getConfig().set("Users." + p.getUniqueId() + "." + "preferences" + ".chunkMapLive", false);
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
			
			if (playerLevel < 1) {
				// Commands+ System Message
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Not high enough level to use command!");
				return true;
			}
			
			// variable grabbing
			String playerFaction = getConfig().getString("Users." + p.getUniqueId() + ".stats" + ".faction");
			
			
			// BASE FACTION COMMANDS
			
			
			// create faction
			if (args[0].equals("create")) {
				String factionName = args[1];
				for (int i = 2; i < args.length; i++) {
					factionName += " " + args[i];
				}
				
				if (getConfig().getBoolean("Users." + p.getUniqueId() + ".stats" + ".inFaction")) {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You're already in a faction!");
					
					return true;
				}
				
				if (factionName.length() > 20) {
					factionName = factionName.substring(0, 20);
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Faction name cannot be longer than 20 characters!");
					
					return true;
				}
				
				if (getConfig().getString("Factions." + factionName + ".ownerUUID") == null) {
					getConfig().set("Factions." + factionName + ".stats" + ".ownerUUID", "" + p.getUniqueId());
					getConfig().set("Factions." + factionName + ".stats" + ".ownerName", "" + p.getName());
					getConfig().set("Users." + p.getUniqueId() + ".stats" + ".inFaction", true);
					getConfig().set("Users." + p.getUniqueId() + ".stats" + ".faction", factionName);
					getConfig().set("Factions." + factionName + ".stats" + ".memberCount", 1);
					getConfig().set("Factions." + factionName + ".members." + p.getUniqueId() + ".name", "" + p.getName());
					getConfig().set("Factions." + factionName + ".members." + p.getUniqueId() + ".rank", "Leader");
					getConfig().set("Users." + p.getUniqueId() + ".preferences" + ".chunkName", factionName);
					changeChunkName(p, factionName);
					saveConfig();
					
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

				if (getConfig().getString("Factions." + playerFaction + ".stats" + ".ownerUUID").equals(p.getUniqueId().toString())){
					String playerName = args[1];
					for (int i = 2; i < args.length; i++) {
						playerName += " " + args[i];
					}
					
					Player playerInvited = Bukkit.getServer().getPlayer(playerName);
					
					int playerInvitedLevel = getConfig().getInt("Users." + playerInvited.getUniqueId() + ".stats" + ".level");
					
					
					if (playerInvitedLevel > 2 && getConfig().getBoolean("Users." + playerInvited.getUniqueId() + ".stats" + ".inFaction") != true) {
						getConfig().set("Users." + playerInvited.getUniqueId() + ".stats" + ".invitedToFaction", true);
						getConfig().set("Factions." + playerFaction + ".members." + playerInvited.getUniqueId() + ".name", "" + playerInvited.getName());
						getConfig().set("Factions." + playerFaction + ".members." + playerInvited.getUniqueId() + ".rank", "invited");
						
						saveConfig();
						
						// Commands+ System Message
						playerInvited.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You've been invited to " + ChatColor.AQUA + playerFaction + ChatColor.WHITE + " by " + p.getName() + "! Type /faction accept to join!");
						// Commands+ System Message
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "] " + "Invited " + ChatColor.GREEN + playerInvited.getName() + ChatColor.WHITE + " to " + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "!");
					}
					else if (playerInvitedLevel < 3){
						// Commands+ System Message
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Player is not level 3 yet!");
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
				ConfigurationSection factions = getConfig().getConfigurationSection("Factions.");
				for (String s : factions.getKeys(false)) {
					if (factions.get(s + ".members." + p.getUniqueId() + ".rank") != null && factions.get(s + ".members." + p.getUniqueId() + ".rank").equals("invited")) {
						factionName = s;
					}
				}
				if (!factionName.equals("")) {
					getConfig().set("Factions." + factionName + ".stats" + ".memberCount", getConfig().getInt("Factions." + factionName + ".stats" + ".memberCount") + 1);
					
					
					getConfig().set("Users." + p.getUniqueId() + ".stats" + ".inFaction", true);
					getConfig().set("Users." + p.getUniqueId() + ".stats" + ".faction", factionName);
					getConfig().set("Users." + p.getUniqueId() + ".stats" + ".invitedToFaction", false);
					getConfig().set("Factions." + factionName + ".members." + p.getUniqueId() + ".rank", "member");
					saveConfig();
					
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Joined faction " + ChatColor.AQUA + factionName + ChatColor.WHITE + "!");
					for (Player online : Bukkit.getOnlinePlayers()) {
						if (getConfig().getBoolean("Users." + online.getUniqueId() + ".stats" + ".inFaction") == true && getConfig().getString("Users." + online.getUniqueId() + ".stats" + ".faction").equals(factionName)) {
							online.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + factionName + ChatColor.WHITE + "] " + ChatColor.GREEN + p.getName() + ChatColor.WHITE + " joined the faction!");
						}
					}
					
					changeChunkName(p, factionName);
					
					
				}
				else {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You haven't been invited to a faction yet!");
				}
				
				saveConfig();
				
				
			}
			
			// decline faction invite
			if (args[0].equals("decline")) {
				String factionName = "";
				ConfigurationSection factions = getConfig().getConfigurationSection("Factions.");
				for (String s : factions.getKeys(false)) {
					if (factions.get(s + ".members." + p.getUniqueId() + ".rank").equals("invited")) {
						factionName = s;
					}
				}
				
				if (!factionName.equals("")) {
					getConfig().set("Users." + p.getUniqueId() + ".stats" + ".invitedToFaction", false);
					getConfig().set("Factions." + factionName + ".members." + p.getUniqueId() + ".rank", "none");
					
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
				if (getConfig().getString("Factions." + playerFaction + ".stats" + ".ownerUUID").equals(p.getUniqueId().toString())) {
					String playerName = args[1];
					for (int i = 2; i < args.length; i++) {
						playerName += " " + args[i];
					}
					
					Player playerRemoved = Bukkit.getServer().getPlayer(playerName);
					
					
					// config stuff
					getConfig().set("Users." + playerRemoved.getUniqueId() + ".stats" + ".inFaction", false);
					getConfig().set("Users." + playerRemoved.getUniqueId() + ".stats" + ".invitedToFaction", false);
					getConfig().set("Users." + playerRemoved.getUniqueId() + ".stats" + ".faction", "");
					
					getConfig().set("Factions." + playerFaction + ".members." + playerRemoved.getUniqueId() + ".rank", "none");
					getConfig().set("Factions." + playerFaction + ".stats" + ".memberCount", getConfig().getInt("Factions." + playerFaction + ".stats" + ".memberCount") - 1);
					
					saveConfig();
					
					
					// Commands+ System Messages
					playerRemoved.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You've been removed from " + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "!");
					for (Player online : Bukkit.getOnlinePlayers()) {
						if (getConfig().getBoolean("Users." + online.getUniqueId() + ".stats" + ".inFaction") != false && getConfig().getString("Users." + online.getUniqueId() + ".stats" + ".faction").equals(playerFaction)) {
							online.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "] " + ChatColor.GREEN + playerName + ChatColor.WHITE + " was kicked from the faction!");
						}
					}
				}
				
			}
			
			// leave faction
			if (args[0].equals("leave")) {
				if (getConfig().getBoolean("Users." + p.getUniqueId() + ".stats" + ".inFaction")) {
					
					
					getConfig().set("Users." + p.getUniqueId() + ".stats" + ".inFaction", false);
					getConfig().set("Users." + p.getUniqueId() + ".stats" + ".faction", "");
					getConfig().set("Factions." + playerFaction + ".stats" + ".memberCount", getConfig().getInt("Factions." + playerFaction + ".stats" + ".memberCount") - 1);
					getConfig().set("Factions." + playerFaction + ".members." + p.getUniqueId() + ".rank", "none");
					getConfig().set("Factions." + playerFaction + ".stats" + ".ownerUUID", null);
					getConfig().set("Factions." + playerFaction + ".stats" + ".ownerName", null);
					
					saveConfig();
				
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You've left " + ChatColor.AQUA + playerFaction + ChatColor.WHITE + "!");
					
					for (Player online : Bukkit.getOnlinePlayers()) {
						if (getConfig().getBoolean("Users." + online.getUniqueId() + ".stats" + ".inFaction") != false && getConfig().getString("Users." + online.getUniqueId() + ".stats" + ".faction").equals(playerFaction)) {
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
				if (args.length == 1 && getConfig().getBoolean("Users." + p.getUniqueId() + ".stats" + ".inFaction") != true) {
					// Commands+ System Message
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "Type in a faction name to see stats!");
					return true;
				}
				if (args.length == 1) {
					factionName = getConfig().getString("Users." + p.getUniqueId() + ".stats" + ".faction");
				}
				else {
					factionName = args[1];
					for (int i = 2; i < args.length; i++) {
						factionName += " " + args[i];
					}
				}
				
				
				ConfigurationSection faction = getConfig().getConfigurationSection("Factions." + factionName + ".members");
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
			
			// ADMIN FACTION COMMANDS
			
			// promote faction member
			if (args[0].equals("promote")) {
				;
			}
			
			// demote faction member
			if (args[0].equals("demote")) {
				;
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
		// Custom Item Commands
		
		if (label.equalsIgnoreCase("bonkstick") && (p.getUniqueId().toString()).equals("b859c378-5ea4-47f8-b7e4-fc80bfc1b713")) {
			
			ItemStack item = new ItemStack(ItemsPlus.bonkStick);
			
			p.getInventory().addItem(item);

		}
		
		
		return false;
		
	}
	
}




// Commands+, made by Joey Balardeta