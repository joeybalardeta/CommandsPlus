package me.Joey.CommandsPlus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.Ageable;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import me.Joey.CommandsPlus.CustomEnchantments.EnchantmentsPlus;
import me.Joey.CommandsPlus.CustomInventories.FactionOptionsInventory;
import me.Joey.CommandsPlus.CustomInventories.InventoryManager;
import me.Joey.CommandsPlus.CustomInventories.RecipeInventoryCreator;
import me.Joey.CommandsPlus.CustomItems.ItemsPlus;
import me.Joey.CommandsPlus.Particles.ParticleData;
import me.Joey.CommandsPlus.Particles.ParticleEffects;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class Events implements Listener{
	
	
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player online = event.getPlayer();
		
		if (!online.hasPlayedBefore()) {
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "preferences" + ".hasPlayedBefore", true);
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "preferences" + ".scoreboard", true);
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "stats" + ".name", event.getPlayer().getName());
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "stats" + ".miningPoints", 0);
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "stats" + ".farmingPoints", 0);
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "stats" + ".combatPoints", 0);
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "stats" + ".enchantingPoints", 0);
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "stats" + ".alchemyPoints", 0);
			
			try {
				Main.playerDataConfig.save(Main.playerData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // this is important to have when editing server files, otherwise nothing gets changed
		}
		
		
		if (!Main.playerDataConfig.getBoolean("Users." + event.getPlayer().getUniqueId() + "." + "preferences" + ".hasPlayedBefore")){
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "preferences" + ".hasPlayedBefore", true);
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "preferences" + ".scoreboard", true);
			Main.playerDataConfig.set("Users." + event.getPlayer().getUniqueId() + "." + "stats" + ".name", event.getPlayer().getName());
			try {
				Main.playerDataConfig.save(Main.playerData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // this is important to have when editing server files, otherwise nothing gets changed
		}
		
		
		// load in player stats to respective HashMaps
		FunctionsPlus.loadHashMaps(online);
		
		
		if (Main.scoreboardHashMap.get(online.getUniqueId().toString())) {
			FunctionsPlus.createBoard(event.getPlayer());
		}
		
		FunctionsPlus.restoreTalentEffects(online, Main.talentHashMap.get(online.getUniqueId().toString()));
		
		// set custom tab presence
		FunctionsPlus.setTabList(online);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		FunctionsPlus.savePlayerData(event.getPlayer(), false);
		
		ParticleData particle = new ParticleData(event.getPlayer().getUniqueId());
		
		if (particle.hasID()) {
			particle.endTask();
			particle.removeID();
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = (Player) event.getEntity();
		ItemStack item = FunctionsPlus.getPlayerHead(p.getName().toString());
		p.getWorld().dropItem(p.getLocation(), item);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();

		FunctionsPlus.restoreTalentEffects(p, Main.talentHashMap.get(p.getUniqueId().toString()));
	}
	
	
	// Inventory Interaction Events
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		
		
		if (event.getInventory() instanceof CraftInventoryCustom) {
			
		}
		else {
			Main.currentOpenInventory.put(event.getPlayer().getUniqueId().toString(), "None");
		}
	}
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		// p.sendMessage(Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()));
		
		if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).equals("None")) {
			return;
		}
		
		if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).equals("Master Menu")) {
			if (event.getCurrentItem() == null) {
				return;
			}
			
			if (event.getCurrentItem().getItemMeta() == null) {
				return;		
			}
			
			if (event.getCurrentItem().getItemMeta().getDisplayName() == null) {
				return;
			}
			
			event.setCancelled(true);
			
			
			
			
			// Close Menu selected
			if (event.getSlot() == 49) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
				p.closeInventory();
				
				return;
			}
			
			// Commands+ Crafts Menu selected
			if (event.getSlot() == 20) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Master Crafts Menu");
				p.openInventory(InventoryManager.masterCraftsInventory);
				return;
			}
			
			// Talent Menu selected
			if (event.getSlot() == 21) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Talent Menu");
				p.openInventory(InventoryManager.talentInventory);
				return;
			}
			
			// Faction Options Menu Selected
			if (event.getSlot() == 24) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Faction Menu");
				FactionOptionsInventory.createFactionOptionsInventory(p);
				p.openInventory(InventoryManager.factionOptionsInventory);
				return;
			}
		}
		
		else if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).equals("Master Crafts Menu")) {
			if (event.getCurrentItem() == null) {
				return;
			}
			
			if (event.getCurrentItem().getItemMeta() == null) {
				return;
			}
			
			if (event.getCurrentItem().getItemMeta().getDisplayName() == null) {
				return;
			}
			
			event.setCancelled(true);
			
			
			
			if (event.getSlot() == 19) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Weapon Crafts Menu");
				p.openInventory(InventoryManager.weaponCraftsInventory);
				return;
			}
			
			
			// Go Back selected
			if (event.getSlot() == 48) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Master Menu");
				p.openInventory(InventoryManager.masterMenuInventory);
				return;
			}
			
			
			// Close Menu selected
			if (event.getSlot() == 49) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
				p.closeInventory();
				
				return;
			}
			
			
			
		}
		
		else if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).equals("Faction Menu")) {
			if (event.getCurrentItem() == null) {
				return;
			}
			
			if (event.getCurrentItem().getItemMeta() == null) {
				return;		
			}
			
			if (event.getCurrentItem().getItemMeta().getDisplayName() == null) {
				return;
			}
			
			event.setCancelled(true);
			
			
			
			// Go Back selected
			if (event.getSlot() == 48) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Master Menu");
				p.openInventory(InventoryManager.masterMenuInventory);
				return;
			}
			
			
			// Close Menu selected
			if (event.getSlot() == 49) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
				p.closeInventory();
				
				return;
			}
			
			
			
		}
		
		else if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).equals("Talent Menu")) {
			if (event.getCurrentItem() == null) {
				return;
			}
			
			if (event.getCurrentItem().getItemMeta() == null) {
				return;		
			}
			
			if (event.getCurrentItem().getItemMeta().getDisplayName() == null) {
				return;
			}
			
			event.setCancelled(true);
			
			// Instantiate particle data/trails objects for player
			ParticleData particle = new ParticleData(p.getUniqueId());
			ParticleEffects trails = new ParticleEffects(p);
			
			if (particle.hasID()) {
				particle.endTask();
				particle.removeID();
			}
			
			// Go Back selected
			if (event.getSlot() == 48) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Master Menu");
				p.openInventory(InventoryManager.masterMenuInventory);
				return;
			}
						
			// Close Menu selected
			if (event.getSlot() == 49) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
				p.closeInventory();
				return;
			}
			
			/*
			if (Main.playerDataConfig.contains("Users." + p.getUniqueId() + ".stats" + ".talent")) {
				p.closeInventory();
				FunctionsPlus.playSound(p, "actionDenied");
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You have already picked a talent!");
				return;
			}
			*/
			
			// Avian selected
			if (event.getSlot() == 18) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Avian talent!");
				Main.talentHashMap.put(p.getUniqueId().toString(), "Avian");
				Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Avian");
				p.closeInventory();
				
				// equip Avian Elytra and lower max health
				p.setMaxHealth(16);
				p.getInventory().setChestplate(ItemsPlus.avianElytra);
			}
			
			// Pyrokinetic selected
			if (event.getSlot() == 19) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Pyrokinetic talent!");
				Main.talentHashMap.put(p.getUniqueId().toString(), "Pyrokinetic");
				Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Pyrokinetic");
				p.closeInventory();
				
				// set health (really just for testers since the max health doesn't change back when swapping talents)
				p.setMaxHealth(20);
				
				// Start particle effects
				trails.startPyrokineticParticles();
			}
			
			// Hydrokinetic selected
			if (event.getSlot() == 20) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Hydrokinetic talent!");
				Main.talentHashMap.put(p.getUniqueId().toString(), "Hydrokinetic");
				Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Hydrokinetic");
				p.closeInventory();
				
				// set health (really just for testers since the max health doesn't change back when swapping talents)
				p.setMaxHealth(20);
			}
			
			// Frostbender selected
			if (event.getSlot() == 21) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Frostbender talent!");
				Main.talentHashMap.put(p.getUniqueId().toString(), "Frostbender");
				Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Frostbender");
				p.closeInventory();
				
				// set health (really just for testers since the max health doesn't change back when swapping talents)
				p.setMaxHealth(20);
				p.getInventory().addItem(ItemsPlus.stasisCrystal);
				
				// Start particle effects
				trails.startFrostbenderParticles();
			}
			
			// Terran selected
			if (event.getSlot() == 22) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Terran talent!");
				Main.talentHashMap.put(p.getUniqueId().toString(), "Terran");
				Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Terran");
				p.closeInventory();
				
				// set health (really just for testers since the max health doesn't change back when swapping talents)
				p.setMaxHealth(20);
			}
			
			// Biokinetic selected
			if (event.getSlot() == 23) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Biokinetic talent!");
				Main.talentHashMap.put(p.getUniqueId().toString(), "Biokinetic");
				Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Biokinetic");
				p.closeInventory();
				p.getInventory().addItem(ItemsPlus.arcaneCrystal);
				
				// set health (really just for testers since the max health doesn't change back when swapping talents)
				p.setMaxHealth(20);
			}
			
			// Enderian selected
			if (event.getSlot() == 24) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Enderian talent!");
				Main.talentHashMap.put(p.getUniqueId().toString(), "Enderian");
				Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Enderian");
				p.closeInventory();
				
				// set health (really just for testers since the max health doesn't change back when swapping talents)
				p.setMaxHealth(16);
				
				// Start particle effects
				trails.startEnderianParticles();
			}
			
			// Cobble Man selected
			if (event.getSlot() == 25) {
				if (!Main.playerRankHashMap.get(p.getUniqueId().toString()).equalsIgnoreCase("Cobble Man")) {
					FunctionsPlus.playSound(p, "actionDenied");
					return;
				}
				
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "OH LAWD HE COMIN!");
				for (Player online : Bukkit.getOnlinePlayers()) {
					online.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "COBBLE MAN HAS ARRIVED");
				}
				Main.talentHashMap.put(p.getUniqueId().toString(), "COBBLE MAN");
				Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "COBBLE MAN");
				p.closeInventory();
				
				// set health (really just for testers since the max health doesn't change back when swapping talents)
				p.setMaxHealth(40);
			}
			
			// Sheriff selected
			if (event.getSlot() == 26) {
				if (!Main.playerRankHashMap.get(p.getUniqueId().toString()).equalsIgnoreCase("Sheriff")) {
					FunctionsPlus.playSound(p, "actionDenied");
					return;
				}
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "OH LAWD HE COMIN!");
				for (Player online : Bukkit.getOnlinePlayers()) {
					online.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "THE SHERIFF IS IN TOWN");
				}
				Main.talentHashMap.put(p.getUniqueId().toString(), "SHERIFF");
				Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "SHERIFF");
				p.closeInventory();
				
				// set health (really just for testers since the max health doesn't change back when swapping talents)
				p.setMaxHealth(40);
			}
			
			for (PotionEffect effect : p.getActivePotionEffects()) {
		        p.removePotionEffect(effect.getType());
			}
			
			
			// save player data file
			try {
				Main.playerDataConfig.save(Main.playerData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // this is important to have when editing server files, otherwise nothing gets changed
		}
		
		else if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).equals("Weapon Crafts Menu")) {
			if (event.getCurrentItem() == null) {
				return;
			}
			
			if (event.getCurrentItem().getItemMeta() == null) {
				return;		
			}
			
			if (event.getCurrentItem().getItemMeta().getDisplayName() == null) {
				return;
			}
			
			event.setCancelled(true);
			
			
			// Go Back selected
			if (event.getSlot() == 10 || event.getSlot() == 11 || event.getSlot() == 12) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Weapon Recipe Menu");
				p.openInventory(RecipeInventoryCreator.createRecipeInventory("Weapons", event.getCurrentItem()));
				return;
			}
			
			// Go Back selected
			if (event.getSlot() == 48) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Master Crafts Menu");
				p.openInventory(InventoryManager.masterCraftsInventory);
				return;
			}
			
			
			// Close Menu selected
			if (event.getSlot() == 49) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
				p.closeInventory();
				
				return;
			}
		}
		
		else if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).equals("Weapon Recipe Menu")) {
			if (event.getCurrentItem() == null) {
				return;
			}
			
			if (event.getCurrentItem().getItemMeta() == null) {
				return;		
			}
			
			if (event.getCurrentItem().getItemMeta().getDisplayName() == null) {
				return;
			}
			
			event.setCancelled(true);
			
			
			// Go Back selected
			if (event.getSlot() == 48) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Weapon Crafts Menu");
				p.openInventory(InventoryManager.weaponCraftsInventory);
				return;
			}
			
			
			// Close Menu selected
			if (event.getSlot() == 49) {
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
				p.closeInventory();
				
				return;
			}
		}
		
		
	}
	
	
	// Minecraft World Events
	@EventHandler
	public void placeBlockEvent(BlockPlaceEvent event) {
		Player p = (Player) event.getPlayer();
		if (event.getBlock() == null || p.getInventory().getItemInMainHand() == null || !p.getInventory().getItemInMainHand().hasItemMeta() || !p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
			return;
		}
		
		String itemName = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
		itemName = ChatColor.stripColor(itemName);
		if ((event.getBlock().getType() == Material.PLAYER_HEAD || event.getBlock().getType() == Material.PLAYER_WALL_HEAD) && (itemName.equals("Arcane Crystal") || itemName.equals("Stasis Crystal"))) {
			event.setCancelled(true);
			return;
		}
		else if ((event.getBlock().getType() == Material.PLAYER_HEAD || event.getBlock().getType() == Material.PLAYER_WALL_HEAD) && !p.isSneaking()) {
			event.setCancelled(true);
			
			SkullMeta meta = (SkullMeta) p.getInventory().getItemInMainHand().getItemMeta();
			String playerName = meta.getOwner();
			Player playerTracked = Bukkit.getServer().getPlayer(playerName);
			
			if (playerTracked == null) {
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "The player this head belongs is not online. To get their coordinates, place their head while they are online. To place this head physically, crouch and place where desired.");
				return;
			}
			
			p.getInventory().setItem(p.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
			
			
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + playerTracked.getName() + "'s location is " + playerTracked.getLocation().getBlockX() + ", " + playerTracked.getLocation().getBlockY() + ", " + playerTracked.getLocation().getBlockZ());
			
		}
	}
	
	
	@EventHandler
	public void playerClick(PlayerInteractEvent event) {
		Player p = event.getPlayer();
        PlayerInventory inv = p.getInventory();
	    Action action = event.getAction();
	    String talent = Main.talentHashMap.get(p.getUniqueId().toString());
	    
	    if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
	    	if (inv.getItemInMainHand() != null && inv.getItemInMainHand().equals(ItemsPlus.bonkStick)) {
	    		int x = event.getClickedBlock().getLocation().getBlockX();
	    		int y = event.getClickedBlock().getLocation().getBlockY();
	    		int z = event.getClickedBlock().getLocation().getBlockZ();
	    		
	          	Main.masterConfig.set("System." + ".miscellaneousData" + ".bonkStickTPLocation" + ".X", x);
	          	Main.masterConfig.set("System." + ".miscellaneousData" + ".bonkStickTPLocation" + ".Y", y);
	          	Main.masterConfig.set("System." + ".miscellaneousData" + ".bonkStickTPLocation" + ".Z", z);
	          	
	          	p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "Set bonk stick teleport location to: " + x + ", " + y + ", " + z);
	          	
	          	try {
	          		Main.masterConfig.save(Main.master);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    	
	    	
	    	if (p.getWorld().getEnvironment() == Environment.NETHER && p.getInventory().getItemInMainHand().equals(ItemsPlus.obsidianInfusedWater)) {
	    		event.setCancelled(true);
	    		p.getInventory().setItem(p.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
	    		event.getClickedBlock().getRelative(event.getBlockFace()).setType(Material.WATER);
	    	}
	    }
	    
	    if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
	    	if (inv.getItemInMainHand() != null && inv.getItemInMainHand().equals(ItemsPlus.bonkStick)) {
	    		Entity entity = FunctionsPlus.getNearestEntityInSight(p, 20, 4);
		    	if (entity instanceof Player) {
		    		Player victim = (Player) entity;
		    		p.getWorld().strikeLightningEffect(victim.getLocation());
		    		for (Player online : Bukkit.getOnlinePlayers()) {
		    			online.playSound(online.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
		    			online.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.AQUA + victim.getName() + ChatColor.WHITE + " has been banished to " + ChatColor.RED + "Fart Castle");
		    		}
		    		int x = Main.masterConfig.getInt("System." + ".miscellaneousData" + ".bonkStickTPLocation" + ".X");
		          	int y = Main.masterConfig.getInt("System." + ".miscellaneousData" + ".bonkStickTPLocation" + ".Y");
		          	int z = Main.masterConfig.getInt("System." + ".miscellaneousData" + ".bonkStickTPLocation" + ".Z");
		          	
		          	Location loc = new Location(Bukkit.getWorld("world"), x, y + 1, z);
		    		
		    		victim.teleport(loc);
		    	}
	    	}
	    }
	    
	    
	    
	    
	    if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
	    	
	    	
	    	if (inv.getItemInMainHand() != null && inv.getItemInOffHand().getType() != Material.AIR) {
	    		if (inv.getItemInMainHand().equals(new ItemStack(Material.ENDER_PEARL)) && talent.equals("Enderian")){
	    			inv.addItem(new ItemStack(Material.ENDER_PEARL));
	    		}
	    		
	    		
	    		if (inv.getItemInMainHand().equals(ItemsPlus.telekinesisBook) && !inv.getItemInOffHand().containsEnchantment(EnchantmentsPlus.TELEKINESIS) && (inv.getItemInOffHand().getType().toString().contains("PICKAXE") || inv.getItemInOffHand().getType().toString().contains("AXE") || inv.getItemInOffHand().getType().toString().contains("SHOVEL") || inv.getItemInOffHand().getType().toString().contains("HOE"))) {
	        		 inv.getItemInMainHand().setAmount(0); 
		        	 inv.getItemInOffHand().addUnsafeEnchantment(EnchantmentsPlus.TELEKINESIS, 1);
		        	 ItemMeta meta = inv.getItemInOffHand().getItemMeta();
		        	 List<String> lore = new ArrayList<String>();
		        	 if (meta.hasLore()) {
		     			 for (String s : meta.getLore()) {
			     			 lore.add(s);
			     		 }
		     		 }
		     		 lore.add(ChatColor.GRAY + "Telekinesis I");
		     		 meta.setLore(lore);
		     		 inv.getItemInOffHand().setItemMeta(meta);
		     		 p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
	        	 }
	        	 
	    		else if (inv.getItemInMainHand().equals(ItemsPlus.smeltingBook) && !inv.getItemInOffHand().containsEnchantment(EnchantmentsPlus.SMELTING) && (inv.getItemInOffHand().getType().toString().contains("AXE") || inv.getItemInOffHand().getType().toString().contains("SHOVEL"))) {
	        		 inv.getItemInMainHand().setAmount(0); 
		        	 inv.getItemInOffHand().addUnsafeEnchantment(EnchantmentsPlus.SMELTING, 1);
		        	 ItemMeta meta = inv.getItemInOffHand().getItemMeta();
		     		 List<String> lore = new ArrayList<String>();
		     		 if (meta.hasLore()) {
		     			 for (String s : meta.getLore()) {
			     			 lore.add(s);
			     		 }
		     		 }
		     		
		     		 lore.add(ChatColor.GRAY + "Smelting I");
		     		 meta.setLore(lore);
		     		 inv.getItemInOffHand().setItemMeta(meta);
		     		 p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
	        	 }
	    		else if (inv.getItemInMainHand().equals(ItemsPlus.experienceBook) && !inv.getItemInOffHand().containsEnchantment(EnchantmentsPlus.EXPERIENCE) && (inv.getItemInOffHand().getType().toString().contains("BOW") ||inv.getItemInOffHand().getType().toString().contains("TRIDENT") || inv.getItemInOffHand().getType().toString().contains("SWORD") || inv.getItemInOffHand().getType().toString().contains("AXE") || inv.getItemInOffHand().getType().toString().contains("SHOVEL"))) {
	        		 inv.getItemInMainHand().setAmount(0); 
		        	 inv.getItemInOffHand().addUnsafeEnchantment(EnchantmentsPlus.EXPERIENCE, 3);
		        	 ItemMeta meta = inv.getItemInOffHand().getItemMeta();
		     		 List<String> lore = new ArrayList<String>();
		     		 if (meta.hasLore()) {
		     			 for (String s : meta.getLore()) {
			     			 lore.add(s);
			     		 }
		     		 }
		     		
		     		 lore.add(ChatColor.GRAY + "Experience III");
		     		 meta.setLore(lore);
		     		 inv.getItemInOffHand().setItemMeta(meta);
		     		 p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
	        	 }
	         }
	         
	         if (inv.getItemInMainHand() != null && inv.getItemInMainHand().equals(ItemsPlus.thugnarsGlock)) {
	        	 p.getWorld().createExplosion(p.getLocation(), 5, true);
	         }
	         
	         
	         
	         if (inv.getItemInMainHand() != null && inv.getItemInMainHand().equals(ItemsPlus.dashSword)) {
	        	 Location loc = p.getPlayer().getLocation();
	        	 loc.setY(loc.getY() - 1);
	        	  
	        	 Material block = loc.getBlock().getType();
	        	 if (block == Material.AIR || block == Material.WATER || block == Material.LAVA)
	        	 {
	        		 return;
	        	 }
	        	 
	        	 p.setVelocity(p.getLocation().getDirection().multiply(10));
	        	 Entity victim = null;
	        	 try {
	        		 victim = FunctionsPlus.getNearestEntityInSight(p, 10, 2);
	        	 } catch(Exception e) {
	        		 return;
	        	 }
	        	 
	        	 if (victim instanceof LivingEntity) {
	        		 LivingEntity le = null;
		        	 try {
		        		 le = (LivingEntity) victim;
		        		 le.damage(6);
		        	 } catch(Exception e) {
		        		 
		        	 }
		        	 return;
	        	 }
	        	 
	        	 
	        	 
	        	 
	         }
	    }
	}
	
	
	
	
	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Player Damage Events
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			String talent = Main.talentHashMap.get(p.getUniqueId().toString());
			if (talent.equals("Avian")) {
				if(event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.FLY_INTO_WALL || event.getCause() == DamageCause.CONTACT) {
					event.setCancelled(true);
				}
			}
			
			if (talent.equals("Pyrokinetic")) {
				Material m1 = p.getLocation().getBlock().getType();
				Material m2 = p.getLocation().add(0, 1, 0).getBlock().getType();
				if (m1 == Material.WATER || m2 == Material.WATER) {
					event.setDamage(event.getDamage() * 1.2);
				}
				else if (p.getWorld().hasStorm()) {
			    	if (p.getLocation().getBlock().getBiome() != Biome.DESERT) {
			    		Location loc = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getWorld().getHighestBlockYAt(p.getLocation()), p.getLocation().getBlockZ());
			    		loc.add(0, 1, 0);
			    		
    				    int blockLocation = p.getLocation().getWorld().getHighestBlockYAt(p.getLocation());
    				    if(blockLocation <= p.getLocation().getY()) {
    				    	event.setDamage(event.getDamage() * 1.2);
    				    }
			    	}
				}
				if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.HOT_FLOOR || event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.MELTING) {
					event.setCancelled(true);
				}
				
				if (event.getCause() == DamageCause.FALL) {
					if (p.getLocation().getBlock().getType() == Material.LAVA) {
						event.setCancelled(true);
					}
					
					if (event.getDamage() >= 4.0) {
						ParticleEffects trails = new ParticleEffects(p);
						trails.damagingFireBurst();
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.6f);
					}
					
				}
				
			}
			
			if (talent.equals("Hydrokinetic") || talent.equals("Frostbender")) {
				if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.HOT_FLOOR || event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.MELTING) {
					event.setDamage(event.getDamage() * 2);
				}
				else {
					if (p.getWorld().getEnvironment() == Environment.NETHER) {
						event.setDamage(event.getDamage() * 1.2);
					}
				}
			}
			
			
			if (talent.equals("Terran")) {
				if (event.getCause() == DamageCause.FALL) {
					event.setDamage(event.getDamage() * 1.5);
				}
						
				
			}
			
			if (talent.equals("Enderian")){
				if (event.getCause() == DamageCause.PROJECTILE) {
					event.setCancelled(true);
				}
			}
			
			if (p.getHealth() - event.getDamage() < 6) {
				if (talent.equals("Frostbender")) {
					Location currentLoc = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() - 1, p.getLocation().getBlockZ());
					Material m1 = p.getWorld().getBlockAt(currentLoc).getType();
					if (m1 == Material.AIR || m1 == Material.CAVE_AIR || m1 == Material.WATER || m1 == Material.LAVA) {
						p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() - 1, p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					}
					
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 2, p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ())).setType(Material.AIR);
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ())).setType(Material.AIR);
					
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() - 1, p.getLocation().getBlockY(), p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() + 1, p.getLocation().getBlockY(), p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ() - 1)).setType(Material.BLUE_ICE);
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ() + 1)).setType(Material.BLUE_ICE);
					
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() - 1, p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() + 1, p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ() - 1)).setType(Material.BLUE_ICE);
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ() + 1)).setType(Material.BLUE_ICE);
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 4));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 3));
				}
				
			}
			
			
		}
		
	}
	
	@EventHandler
	public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event){
		if (event.getDamager() instanceof Player) {
			Player p = (Player) event.getDamager();
			String talent = Main.talentHashMap.get(p.getUniqueId().toString());
			if (talent.equals("Avian")) {
				if (p.isGliding()) {
					event.setDamage(event.getDamage() * 1.6);
				}
			}
			
			if (talent.equals("Pyrokinetic")) {
				if (p.getFireTicks() !=  0 && !p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
					event.setDamage(event.getDamage() * 1.3);
				}
				if (event.getEntity() instanceof Player) {
					if (((int) (Math.random() * 20)) == 7) {
						LivingEntity victim = (LivingEntity) event.getEntity();
						victim.setFireTicks(100);
					}
				}
				else {
					LivingEntity victim = (LivingEntity) event.getEntity();
					victim.setFireTicks(100);
				}
				
			}
			
			if (talent.equals("Hydrokinetic")) {
				Material m1 = p.getLocation().getBlock().getType();
				Material m2 = p.getLocation().add(0, 1, 0).getBlock().getType();
				
			    if (m1 == Material.WATER || m2 == Material.WATER) {
			    	event.setDamage(event.getDamage() * 1.3);
			    }
			}
			
			
			
		}
		
		if (event.getEntity() instanceof Player) {
			Player victim = (Player) event.getEntity();
			String talent = Main.talentHashMap.get(victim.getUniqueId().toString());
			if (talent.equals("COBBLE MAN")) {
				if (event.getDamager() instanceof Player) {
					Player p = (Player) event.getDamager();
					if (p.getInventory().getItemInMainHand().getType().toString().contains("PICKAXE")) {
						event.setDamage(200);
					}
					else {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent event) {
		
		if (event.getPlayer() != null) {
			Main.miningPointsTracker.put(event.getPlayer().getUniqueId().toString(), Main.miningPointsTracker.get(event.getPlayer().getUniqueId().toString()) + 1);
			if (Main.canSaveDataHashMap.get(event.getPlayer().getUniqueId().toString())){
				FunctionsPlus.savePlayerData(event.getPlayer(), false);
			}
			
			Player p = event.getPlayer();
	        PlayerInventory inv = p.getInventory();
	 
	        ItemStack timberAxe = new ItemStack (ItemsPlus.timberAxe);
	        ItemStack redstonePickaxe = new ItemStack (ItemsPlus.redstonePickaxe);
	        
	 
	        if (inv.getItemInMainHand().equals(timberAxe) && Main.logMaterials.contains(event.getBlock().getType())) {
	            Location location = event.getBlock().getLocation();
	            LinkedList<Block> blocks = new LinkedList<>();
	            ItemStack handStack = p.getItemInHand();
	    		for (int i = location.getBlockY(); i < location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ());)
	    		{
	    			Location l = location.add(0.0D, 1.0D, 0.0D);
	    			Block block = l.getBlock();
	    			if (Main.logMaterials.contains(block.getType()))
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
	        
	        if (inv.getItemInMainHand().equals(ItemsPlus.replantingHoe)) {
	        	Block block = event.getBlock();
		    	Material material = block.getType();
		    	World world = block.getWorld();
		    	
	        	if (block.getType() == Material.WHEAT) {
	        		Ageable crop = (Ageable) block.getBlockData();
	        		if (crop.getAge() != crop.getMaximumAge()) {
						return;	
					}
	        		
	        		world.dropItemNaturally(block.getLocation(), new ItemStack(Material.WHEAT));
	        		world.dropItemNaturally(block.getLocation(), new ItemStack(Material.WHEAT_SEEDS));
	        		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Commands_Plus"), new Runnable() {
	                    @Override
	                    public void run() {
	                    	block.setType(material);
	                    }
	                }, 10L);
	        	}
	        	else if (event.getBlock().getType() == Material.POTATOES) {
	        		Ageable crop = (Ageable) block.getBlockData();
	        		if (crop.getAge() != crop.getMaximumAge()) {
						return;	
					}
	        		
	        		world.dropItemNaturally(block.getLocation(), new ItemStack(Material.POTATO));
	        		
	        		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Commands_Plus"), new Runnable() {
	                    @Override
	                    public void run() {
	                    	block.setType(material);
	                    }
	                }, 10L);
	        	}

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
		        	if (!(event.getBlock().getState() instanceof Container)) {
		        		Collection<ItemStack> drops = event.getBlock().getDrops(inv.getItemInMainHand());
		        		Collection<ItemStack> dropsEdited = event.getBlock().getDrops(inv.getItemInMainHand());
			        	if (inv.getItemInMainHand().getItemMeta().hasEnchant(EnchantmentsPlus.SMELTING)) {
		        			if (drops.isEmpty()) {
			        			return;
			        		}
		        			
		        			boolean smeltable = false;
		        			int expFromMining = 0;
		        			ItemStack[] dropsArray = drops.toArray(new ItemStack[drops.size()]);
		        			
			        		for (int i = 0; i < dropsArray.length; i++) {
			        			ItemStack item = dropsArray[i];
			        			if (item.getType().toString().equals("COBBLESTONE")) {
			        				ItemStack oldItem = new ItemStack(Material.COBBLESTONE);
			        				item = new ItemStack(Material.STONE);
			        				dropsEdited.remove(oldItem);
			        				dropsEdited.add(item);
			        				smeltable = true;
			        			}
			        			else if (item.getType().toString().equals("STONE")) {
			        				ItemStack oldItem = new ItemStack(Material.STONE);
			        				item = new ItemStack(Material.STONE);
			        				dropsEdited.remove(oldItem);
			        				dropsEdited.add(item);
			        				smeltable = true;
			        			}
			        			else if (item.getType().toString().equals("NETHERRACK")) {
			        				ItemStack oldItem = new ItemStack(item.getType());
			        				item = new ItemStack(Material.NETHER_BRICK);
			        				dropsEdited.remove(oldItem);
			        				dropsEdited.add(item);
			        				smeltable = true;
			        			}
			        			else if (item.getType().toString().equals("SAND")) {
			        				ItemStack oldItem = new ItemStack(Material.SAND);
			        				item = new ItemStack(Material.GLASS);
			        				dropsEdited.remove(oldItem);
			        				dropsEdited.add(item);
			        				smeltable = true;
			        			}
			        			else if (item.getType().toString().equals("CLAY_BALL")) {
			        				int amount = item.getAmount();
			        				ItemStack oldItem = new ItemStack(item.getType());
			        				oldItem.setAmount(amount);
			        				item = new ItemStack(Material.BRICK);
			        				item.setAmount(amount);
			        				dropsEdited.remove(oldItem);
			        				dropsEdited.add(item);
			        				smeltable = true;
			        			}
			        			else if (item.getType().toString().equals("IRON_ORE")) {
			        				ItemStack oldItem = new ItemStack(Material.IRON_ORE);
			        				item = new ItemStack(Material.IRON_INGOT);
			        				dropsEdited.remove(oldItem);
			        				dropsEdited.add(item);
			        				smeltable = true;
			        				expFromMining += 1;
			        			}
			        			else if (item.getType().toString().equals("GOLD_ORE")) {
			        				ItemStack oldItem = new ItemStack(Material.GOLD_ORE);
			        				item = new ItemStack(Material.GOLD_INGOT);
			        				dropsEdited.remove(oldItem);
			        				dropsEdited.add(item);
			        				smeltable = true;
			        				expFromMining += 2;
			        			}
			        			else if (item.getType().toString().contains("LOG")) {
			        				ItemStack oldItem = new ItemStack(item.getType());
			        				item = new ItemStack(Material.CHARCOAL);
			        				dropsEdited.remove(oldItem);
			        				dropsEdited.add(item);
			        				smeltable = true;
			        			}
			        			else if (item.getType().toString().equals("CACTUS")) {
			        				ItemStack oldItem = new ItemStack(item.getType());
			        				item = new ItemStack(Material.GREEN_DYE);
			        				dropsEdited.remove(oldItem);
			        				dropsEdited.add(item);
			        				smeltable = true;
			        			}
			        			
			        		}
			        		
			        		if (smeltable) {
			        			event.setDropItems(false);
			        			if (!inv.getItemInMainHand().getItemMeta().hasEnchant(EnchantmentsPlus.TELEKINESIS) || inv.firstEmpty() == -1) {
			        				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(dropsEdited.iterator().next()));
			        			}
			        			p.getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 12);
			        			event.setExpToDrop(expFromMining);
			        		}
			        	}
				        if (inv.getItemInMainHand().getItemMeta().hasEnchant(EnchantmentsPlus.TELEKINESIS)) {
				        	// checks if player's inventory is full and if the item being mined is a chest or other block that contains items
				        	if (inv.firstEmpty() != -1) {
				        		event.setDropItems(false);
				        		
				        		inv.addItem(dropsEdited.iterator().next());
				        	}
				        }
		        	}
		        	
		        	if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchants() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(EnchantmentsPlus.EXPERIENCE)) {
						int level = p.getInventory().getItemInMainHand().getItemMeta().getEnchants().get(EnchantmentsPlus.EXPERIENCE);
						event.setExpToDrop((int) ((event.getExpToDrop() * (1 + 0.5 * level)) + 0.5));
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
	public void onGrindstoneEvent(InventoryClickEvent event) {
		if (event.getCurrentItem() == null){
			return;
		}
		if (event.getClickedInventory().getType() == InventoryType.GRINDSTONE && event.getSlotType() == InventoryType.SlotType.RESULT) {
            ItemStack item = new ItemStack(event.getCurrentItem());
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {
            	List<String> lore = meta.getLore();
                lore.clear();
        		meta.setLore(lore);
        		item.setItemMeta(meta);
            }
            
    		
    		event.setCurrentItem(item);
         }
	}

	
	@EventHandler
	public void enchantEvent(EnchantItemEvent event) {
		int cost = event.getExpLevelCost();
		
		
		if (cost > 29) { // max enchant level
			Main.enchantingPointsTracker.put(event.getEnchanter().getUniqueId().toString(), Main.enchantingPointsTracker.get(event.getEnchanter().getUniqueId().toString()) + 1);
		}
		else if (cost > 9) { // middle enchant level
			Main.enchantingPointsTracker.put(event.getEnchanter().getUniqueId().toString(), Main.enchantingPointsTracker.get(event.getEnchanter().getUniqueId().toString()) + 1);
		}
		else { // lowest enchant level
			Main.enchantingPointsTracker.put(event.getEnchanter().getUniqueId().toString(), Main.enchantingPointsTracker.get(event.getEnchanter().getUniqueId().toString()) + 1);
		}
		
		
	}
	
	@EventHandler
	public void mobKillEvent(EntityDeathEvent event) {
		if (event.getEntity().getKiller() != null) {
			if (event.getEntity().getKiller() instanceof Player) {
				Player p = event.getEntity().getKiller();
				Main.combatPointsTracker.put(p.getUniqueId().toString(), Main.combatPointsTracker.get(event.getEntity().getKiller().getUniqueId().toString()) + 1);
				if (Main.canSaveDataHashMap.get(event.getEntity().getKiller().getUniqueId().toString())){
					FunctionsPlus.savePlayerData(event.getEntity().getKiller(), false);
				}
				
				if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchants() && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(EnchantmentsPlus.EXPERIENCE)) {
					int level = p.getInventory().getItemInMainHand().getItemMeta().getEnchants().get(EnchantmentsPlus.EXPERIENCE);
					event.setDroppedExp((int) ((event.getDroppedExp() * (1 + 0.5 * level)) + 0.5));
				}
			}
		}
		
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			Main.playerDeathsHashMap.put(p.getUniqueId().toString(), Main.playerDeathsHashMap.get(p.getUniqueId().toString()) + 1);
			
			if (Main.canSaveDataHashMap.get(p.getUniqueId().toString())){
				FunctionsPlus.savePlayerData(p, false);
			}
		}
	}
	
	@EventHandler
	public void fishEvent(PlayerFishEvent event) {
	    if(event.getCaught() instanceof Item){
	    	Main.farmingPointsTracker.put(event.getPlayer().getUniqueId().toString(), Main.farmingPointsTracker.get(event.getPlayer().getUniqueId().toString()) + 1);
	    	if (((int) (Math.random() * 20)) == 7) {
	    		Vector velocity = event.getCaught().getVelocity();
	    		Location itemLoc = event.getCaught().getLocation();
	    		Entity entity = event.getPlayer().getWorld().spawnEntity(itemLoc, EntityType.DROPPED_ITEM);
	    		entity.setVelocity(velocity);
	    		entity.teleport(itemLoc);
	    		Item item = (Item) entity;
	    		item.setItemStack(Main.betterFishingLootTable.get(0));
	    	}
	    }
		
	}
	
	@EventHandler
	public void shearEvent(PlayerShearEntityEvent event) {
		Main.farmingPointsTracker.put(event.getPlayer().getUniqueId().toString(), Main.farmingPointsTracker.get(event.getPlayer().getUniqueId().toString()) + 1);
	}
	
	@EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
		
		Player p = event.getPlayer();
       
        if (event.getItem() != null && event.getItem().hasItemMeta()) {
            if (event.getItem().getItemMeta() instanceof PotionMeta) {
           
            	if (event.getItem().equals(ItemsPlus.hastePotion)) {
            		p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 60 * 5, 1));
            	}
            	else if (event.getItem().equals(ItemsPlus.absorptionPotion)) {
            		p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 10, 1));
            	}
            	
            	
                final PotionMeta meta = (PotionMeta) event.getItem().getItemMeta();
                final PotionData data = meta.getBasePotionData();
                if(data.getType() == PotionType.MUNDANE || data.getType() == PotionType.AWKWARD) {
                	Main.alchemyPointsTracker.put(p.getUniqueId().toString(), Main.alchemyPointsTracker.get(p.getUniqueId().toString()) + 1);
                }
                else if (data.getType() == PotionType.UNCRAFTABLE) {
                	Main.alchemyPointsTracker.put(p.getUniqueId().toString(), Main.alchemyPointsTracker.get(p.getUniqueId().toString()) + 10);
                }
                else if (data.getType() == PotionType.INVISIBILITY || data.getType() == PotionType.JUMP) {
                	Main.alchemyPointsTracker.put(p.getUniqueId().toString(), Main.alchemyPointsTracker.get(p.getUniqueId().toString()) + 5);
                }
                else {
                	Main.alchemyPointsTracker.put(p.getUniqueId().toString(), Main.alchemyPointsTracker.get(p.getUniqueId().toString()) + 4);
                }            
                
                
                if (data.isUpgraded() || data.isExtended()) {
                	Main.alchemyPointsTracker.put(p.getUniqueId().toString(), Main.alchemyPointsTracker.get(p.getUniqueId().toString()) + 5);
                }
            }
        }
	}
	
	
	@EventHandler
	public void onPotionEffect(EntityPotionEffectEvent event) {
		if (event.getEntity() instanceof Player && (event.getCause().equals(EntityPotionEffectEvent.Cause.POTION_DRINK) || event.getCause().equals(EntityPotionEffectEvent.Cause.POTION_SPLASH)
				|| event.getCause().equals(EntityPotionEffectEvent.Cause.ARROW) || event.getCause().equals(EntityPotionEffectEvent.Cause.TOTEM)
				|| event.getCause().equals(EntityPotionEffectEvent.Cause.DOLPHIN) || event.getCause().equals(EntityPotionEffectEvent.Cause.BEACON))) {
			Player p = (Player) event.getEntity();
			if (Main.talentHashMap.get(p.getUniqueId().toString()).equals("Shaman") || Main.talentHashMap.get(p.getUniqueId().toString()).equals("Poison")) {
				PotionEffect potionEffect = event.getNewEffect();
				event.setCancelled(true);
				p.addPotionEffect(new PotionEffect(potionEffect.getType(), potionEffect.getDuration() * 2, 0));
			}
			
		}
		
	}
	
	
	
	
	
	
	@SuppressWarnings("unused")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (false) {
			// event.getPlayer().setVelocity(new Vector().zero());
			//event.getPlayer().sendMessage(event.getPlayer().getLocation().getBlock().getType().toString());
		}
		// snow walking
		// event.getPlayer().getWorld().getBlockAt(new Location(event.getPlayer().getWorld(), event.getPlayer().getLocation().getBlockX(), event.getPlayer().getLocation().getBlockY(), event.getPlayer().getLocation().getBlockZ())).setType(Material.SNOW);
	}
	
	
	// Talent Events
	
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.ENDER_PEARL && Main.talentHashMap.get(event.getPlayer().getUniqueId().toString()) != null && Main.talentHashMap.get(event.getPlayer().getUniqueId().toString()).equals("Enderian")) {
			event.getPlayer().getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
		}
	}
	
	// Item Rename Preventer
	@EventHandler
	public void renameCancel(InventoryClickEvent event) {
		if (event.getCurrentItem() == null){
			return;
		}
		
		
		if (event.getClickedInventory().getType() == InventoryType.ANVIL && event.getSlotType() == InventoryType.SlotType.RESULT) {
			if (event.getCurrentItem().equals(new ItemStack(Material.PLAYER_HEAD))){
				event.setCancelled(true);
            	FunctionsPlus.playSound((Player) event.getWhoClicked(), "actionDenied");
				event.getWhoClicked().sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You cannot rename that item!");
				return;
			}
			ItemStack[] contents = event.getInventory().getContents();
			for (ItemStack item : Main.unrenameableItems) {
				if (Arrays.stream(contents) != null && Arrays.stream(contents).anyMatch(item::equals)) {
					event.setCancelled(true);
	            	FunctionsPlus.playSound((Player) event.getWhoClicked(), "actionDenied");
					return;
				}
			}
		}
		
		if (event.getClickedInventory().getType() == InventoryType.GRINDSTONE && event.getSlotType() == InventoryType.SlotType.RESULT) {
			Player p = (Player) event.getWhoClicked();
			ItemStack[] contents = event.getInventory().getContents();
			for (ItemStack item : Main.unrenameableItems) {
				if (contents != null && Arrays.stream(contents).anyMatch(item::equals)) {
					event.setCancelled(true);
	            	FunctionsPlus.playSound(p, "actionDenied");
					return;
				}
			}
		}
		
		if (event.getClickedInventory().getType() == InventoryType.CRAFTING && event.getSlotType() == InventoryType.SlotType.RESULT) {
			// Player p = (Player) event.getWhoClicked();
			
		}
	}
	
	
	@EventHandler
	public void onEntityTarget(EntityTargetLivingEntityEvent event) {
		if (event.getTarget() instanceof Player) {
			Player p = (Player) event.getTarget();
			String talent = Main.talentHashMap.get(p.getUniqueId().toString());
			if (talent != null && talent.equals("Enderian")){
				event.setTarget(null);
			}
		}
	}
	
	
	@EventHandler
	public void talentActivate(PlayerInteractEvent event) {
		Player p = event.getPlayer();
        PlayerInventory inv = p.getInventory();
	    Action action = event.getAction();
	    if (inv.getItemInMainHand() == null || !inv.getItemInMainHand().hasItemMeta()) {
	    	return;
	    }
	    
	    
	    if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
	    	String itemName = ChatColor.stripColor(inv.getItemInMainHand().getItemMeta().getDisplayName());
    		if (inv.getItemInMainHand().getType() == Material.PLAYER_HEAD && inv.getItemInMainHand().getItemMeta().hasLore() && itemName.equals("Stasis Crystal")) {
	    		Entity tracked = FunctionsPlus.getNearestEntityInSight(p, 128, 1);
	    		if (tracked != null) {
	    			if (tracked instanceof Player) {
	    				Player victim = (Player) tracked;
	    				Main.playerFrozenHashMap.put(victim.getUniqueId().toString(), 60);
	    				victim.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.AQUA + "Frozen!");
	    			}
	    			else if (tracked instanceof LivingEntity) {
	    				LivingEntity victim = (LivingEntity) tracked;
	    				victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
	    			}
	    			
	    			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.AQUA + "Froze target!");
	    			Vector shotLine = tracked.getLocation().toVector().subtract(p.getLocation().toVector());
	    			p.sendMessage(tracked.getLocation().toVector().toString());
	    			p.sendMessage(p.getLocation().toVector().toString());
	    			p.sendMessage(shotLine.divide(new Vector(2, 2, 2)).toString());
	    			Location loc = p.getLocation();
	    			for (int i = 0; i < 100; i++) {
	    				Vector shotLineTemp = shotLine;
	    				shotLineTemp.divide(new Vector(2, 2, 2));
	    				Location particleSpawn = loc.clone().add(shotLineTemp.divide(new Vector(10.0, 10.0, 10.0)).multiply(new Vector(0.0 + i, 0.0 + i, 0.0 + i)));
	    				p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleSpawn, 0);
	    			}
	    			p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 0);
	    			p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.3f);
	    		}
	    	}
	    }
	    
	    
	    if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
    		String itemName = ChatColor.stripColor(inv.getItemInMainHand().getItemMeta().getDisplayName());
    		if (inv.getItemInMainHand().getType() == Material.PLAYER_HEAD && inv.getItemInMainHand().getItemMeta().hasLore() && itemName.equals("Arcane Crystal")) {
    			ParticleEffects trails = new ParticleEffects(p);
				trails.weakeningArcaneBurst();
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 1.0f, 1.0f);
    		}
	    }
	}
	
	
	
	// Tracking Bow
	@EventHandler
	public void onTrackingBowMark(PlayerInteractEvent event) {
		Player p = event.getPlayer();
	    Action action = event.getAction();
	    
	    
	    
	    if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
	        

			 
	        if (p.getInventory().getItemInMainHand().equals(ItemsPlus.trackingBow)) {
	        	Entity tracked = FunctionsPlus.getNearestEntityInSight(p, 256, 2);
	        	if (tracked != null) {
	        		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
	        		Main.trackingBowTarget.put(p.getUniqueId().toString(), tracked);
		        	p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.GREEN + "Tracking Bow" + ChatColor.WHITE + " - target marked!");
	        	}
	        	
	        }
	    }
	}
	
	@EventHandler
	public void onTrackingBowShoot(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			ItemStack trackingBow = new ItemStack (ItemsPlus.trackingBow);
	        

			 
	        if (p.getInventory().getItemInMainHand().equals(trackingBow)) {
	        	event.getProjectile().setCustomName("Tracking Arrow");
	        }
		}
	}
	
	
	
	
	// Blood Moon events
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (Main.isBloodMoon) {
			if (event.getEntityType() == EntityType.ZOMBIE) {
				Zombie zombie = (Zombie) event.getEntity();
				zombie.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
				zombie.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
				zombie.getEquipment().setBootsDropChance(0);
				if (((int) (Math.random() * 3)) == 2) {
					zombie.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_AXE));
				}
				
			}
			else if (event.getEntityType() == EntityType.SKELETON) {
				Skeleton skeleton = (Skeleton) event.getEntity();
				ItemStack bow = new ItemStack(Material.BOW);
				
				bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
				
				skeleton.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
				skeleton.getEquipment().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
				skeleton.getEquipment().setItemInMainHand(bow);
				skeleton.getEquipment().setItemInMainHandDropChance(0);
				
			}
			else if (event.getEntityType() == EntityType.SPIDER) {
				Spider spider = (Spider) event.getEntity();
				spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 0));
			}
			else if (event.getEntityType() == EntityType.DROWNED) {
				Zombie zombie = (Zombie) event.getEntity();
				zombie.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
				zombie.getEquipment().setChestplateDropChance(0);
				if (((int) (Math.random() * 10)) >= 7) {
					zombie.getEquipment().setItemInMainHand(new ItemStack(Material.TRIDENT));
				}	
			}
			
			if (event.getSpawnReason().equals(SpawnReason.SPAWNER)) {
				event.setCancelled(true);
			}	
		}
		
	}
	
	@EventHandler
	public void onBloodMoonMobDamage(EntityDamageByEntityEvent event) {
		if (Main.isBloodMoon) {
			if (event.getEntity() instanceof Player && (event.getDamager() instanceof Zombie || event.getDamager() instanceof Spider || event.getDamager() instanceof Drowned)) {
				Player p = (Player) event.getEntity();
				if (((int) (Math.random() * 10)) == 7) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0));
				}
			}
		}
		
	}
	
	@EventHandler
	public void onBloodMoonMobDeath(EntityDeathEvent event) {
		if (Main.isBloodMoon) {
			if (event.getEntity() instanceof Zombie || event.getEntity() instanceof Spider || event.getEntity() instanceof Skeleton || event.getEntity() instanceof Creeper || event.getEntity() instanceof Drowned) {
				Collection<ItemStack> drops = event.getDrops();
				if (((int) (Math.random() * 4)) == 1) {
					int range = Main.bloodMoonLootTable.size();
					drops.add(Main.bloodMoonLootTable.get((int) (Math.random() * range)));
				}
				if (((int) (Math.random() * 4)) == 1) {
					int range = Main.bloodMoonLootTable.size();
					drops.add(Main.bloodMoonLootTable.get((int) (Math.random() * range)));
				}
				
			}
		}
		
	}
	
	@EventHandler
	public void onPlayerSleep(PlayerBedEnterEvent event) {
		if (Main.isBloodMoon) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + ChatColor.RED + "You do not feel tired.");
		}
	}
	
	
	
	// Custom Messaging
	@EventHandler
	public void onPlayerPublicMessage(PlayerChatEvent event) {
		event.setCancelled(true);
		Player p = event.getPlayer();
		String rank = Main.playerRankHashMap.get(p.getUniqueId().toString());
		
		for (Player online : Bukkit.getOnlinePlayers()) {
		online.sendMessage(ChatColor.WHITE + "[" + FunctionsPlus.getRankColor(p) + rank + ChatColor.WHITE + "] " + p.getName() + ": " + event.getMessage());
		}
		
	}
	
}