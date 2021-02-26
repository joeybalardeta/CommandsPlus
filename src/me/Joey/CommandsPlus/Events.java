package me.Joey.CommandsPlus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

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
		Main.loadHashMaps(online);
		
		
		if (Main.scoreboardHashMap.get(online.getUniqueId().toString())) {
			Main.createBoard(event.getPlayer());
		}
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
		String talent = Main.talentHashMap.get(p.getUniqueId().toString());
		if (talent.equals("Avian")) {
			p.setMaxHealth(16);
			p.getInventory().setChestplate(ItemsPlus.avianElytra);
		}
		else {
			p.setMaxHealth(20);
		}
	}
	
	
	// Interaction Events
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!event.getInventory().equals(Main.talentInventory)) {
			return;
		}
		
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
		
		
		Player p = (Player) event.getWhoClicked();
		
		// Instantiate particle data/trails objects for player
		ParticleData particle = new ParticleData(p.getUniqueId());
		ParticleEffects trails = new ParticleEffects(p);
		
		if (particle.hasID()) {
			particle.endTask();
			particle.removeID();
		}
		
		
		// Close Menu selected
		if (event.getSlot() == 26) {
			p.closeInventory();
			return;
		}
		
		if (Main.playerDataConfig.contains("Users." + p.getUniqueId() + ".stats" + ".talent") && !p.hasPermission("commandsPlus.tester")) {
			p.closeInventory();
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You have already picked a talent!");
			return;
		}
		
		// Avian selected
		if (event.getSlot() == 0) {
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Avian talent!");
			Main.talentHashMap.put(p.getUniqueId().toString(), "Avian");
			Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Avian");
			p.closeInventory();
			
			// equip Avian Elytra and set lower health
			p.setMaxHealth(16);
			p.getInventory().setChestplate(ItemsPlus.avianElytra);
		}
		
		// Pyrokinetic selected
		if (event.getSlot() == 1) {
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Pyrokinetic talent!");
			Main.talentHashMap.put(p.getUniqueId().toString(), "Pyrokinetic");
			Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Pyrokinetic");
			p.closeInventory();
			
			// Start particle effects
			trails.startPyrokineticParticles();
		}
		
		// Hydrokinetic selected
		if (event.getSlot() == 2) {
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Hydrokinetic talent!");
			Main.talentHashMap.put(p.getUniqueId().toString(), "Hydrokinetic");
			Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Hydrokinetic");
			p.closeInventory();
		}
		
		// Frostbender selected
		if (event.getSlot() == 3) {
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "You chose the Frostbender talent!");
			Main.talentHashMap.put(p.getUniqueId().toString(), "Frostbender");
			Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Frostbender");
			p.closeInventory();
			
			// Start particle effects
			trails.startFrostbenderParticles();
		}
		
		// Cobble Man selected
		if (event.getSlot() == 18) {
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "OH LAWD HE COMIN!");
			for (Player online : Bukkit.getOnlinePlayers()) {
				online.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + "COBBLE MAN HAS ARRIVED");
			}
			Main.talentHashMap.put(p.getUniqueId().toString(), "COBBLE MAN");
			Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "COBBLE MAN");
			p.closeInventory();
		}
		
		
		// save player data file
		try {
			Main.playerDataConfig.save(Main.playerData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // this is important to have when editing server files, otherwise nothing gets changed
		
	}
	
	
	// Minecraft World Events
	@EventHandler
	public void placeBlockEvent(BlockPlaceEvent event) {
		Player p = (Player) event.getPlayer();
		if (event.getBlock().getType() == Material.PLAYER_HEAD && !p.isSneaking()) {
			event.setCancelled(true);
			
			String playerName = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
			p.getInventory().setItem(p.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
			playerName = playerName.replace("head", "");
			playerName = playerName.replace("'s", "");
			playerName = ChatColor.stripColor(playerName);
			playerName = playerName.replace(" ", "");
			
			Player playerTracked = Bukkit.getServer().getPlayer(playerName);
			p.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Commands" + ChatColor.DARK_RED + "+" + ChatColor.WHITE + "] " + playerTracked.getName() + "'s location is " + playerTracked.getLocation().getBlockX() + ", " + playerTracked.getLocation().getBlockY() + ", " + playerTracked.getLocation().getBlockZ());
			
		}
	}
	
	
	
	@EventHandler
	public void playerRightClick(PlayerInteractEvent event) {
		Player p = event.getPlayer();
        PlayerInventory inv = p.getInventory();
	    Action action = event.getAction();
	    
	    if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
	         if (inv.getItemInMainHand() != null && inv.getItemInOffHand().getType() != Material.AIR) {
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
	        	 }
	        	 
	        	 if (inv.getItemInMainHand().equals(ItemsPlus.smeltingBook) && !inv.getItemInOffHand().containsEnchantment(EnchantmentsPlus.SMELTING) && (inv.getItemInOffHand().getType().toString().contains("PICKAXE"))) {
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
	        		 victim = FunctionsPlus.getNearestEntityInSight(p, 10);
	        	 } catch(Exception e) {
	        		 return;
	        	 }
	        	 
	        	 if (victim instanceof Animals) {
	        		 Animals a = null;
		        	 try {
		        		 a = (Animals) victim;
		        		 a.damage(20);
		        	 } catch(Exception e) {
		        		 
		        	 }
		        	 return;
	        	 }
	        	 
	        	 if (victim instanceof Mob) {
	        		 Mob m = null;
		        	 try {
		        		 m = (Animals) victim;
		        		 m.damage(20);
		        	 } catch(Exception e) {
		        		 
		        	 }
		        	 return;
	        	 }
	        	 
	        	 
	        	 
	        	 
	         }
	    }
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			String talent = Main.talentHashMap.get(p.getUniqueId().toString());
			if (talent.equals("Avian")) {
				if(event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.FLY_INTO_WALL) {
					event.setCancelled(true);
				}
			}
			
			if (talent.equals("Pyrokinetic")) {
				if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.HOT_FLOOR || event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.MELTING) {
					event.setCancelled(true);
				}
				if (p.getWorld().getEnvironment() == Environment.NETHER) {
					event.setDamage(event.getDamage() * 0.7);
				}
			}
			
			if (talent.equals("Hydrokinetic") || talent.equals("Frostbender")) {
				if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.HOT_FLOOR || event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.MELTING) {
					event.setDamage(event.getDamage() * 2);
				}
				else {
					if (p.getWorld().getEnvironment() == Environment.NETHER) {
						event.setDamage(event.getDamage() * 1.3);
					}
				}
			}
			
			if (p.getHealth() - event.getDamage() < 6) {
				if (talent.equals("Frostbender")) {
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() - 1, p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
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
					event.setDamage(event.getDamage() * 2);
				}
			}
			
			if (talent.equals("Pyrokinetic")) {
				if (event.getDamager().getFireTicks() != 0) {
					event.setDamage(event.getDamage() * 1.5);
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
			        	if (inv.getItemInMainHand().getItemMeta().hasEnchant(EnchantmentsPlus.SMELTING)) {
		        			if (drops.isEmpty()) {
			        			return;
			        		}
			        		for (ItemStack item : drops) {
			        			if (item.getType().toString().equals("IRON_ORE")) {
			        				ItemStack oldItem = new ItemStack(Material.IRON_ORE);
			        				item = new ItemStack(Material.IRON_INGOT);
			        				drops.remove(oldItem);
			        				drops.add(item);
			        			}
			        			if (item.getType().toString().equals("GOLD_ORE")) {
			        				ItemStack oldItem = new ItemStack(Material.GOLD_ORE);
			        				item = new ItemStack(Material.GOLD_INGOT);
			        				drops.remove(oldItem);
			        				drops.add(item);
			        			}
			        		}
			        	}
				        if (inv.getItemInMainHand().getItemMeta().hasEnchant(EnchantmentsPlus.TELEKINESIS)) {
				        	// checks if player's inventory is full and if the item being mined is a chest or other block that contains items
				        	if (inv.firstEmpty() != -1) {
				        		event.setDropItems(false);
				        		
				        		inv.addItem(drops.iterator().next());
				        	}
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
	public void onGrindstoneEvent(InventoryClickEvent event) {
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
			Main.combatPointsTracker.put(event.getEntity().getKiller().getUniqueId().toString(), Main.combatPointsTracker.get(event.getEntity().getKiller().getUniqueId().toString()) + 1);
			if (Main.canSaveDataHashMap.get(event.getEntity().getKiller().getUniqueId().toString())){
				FunctionsPlus.savePlayerData(event.getEntity().getKiller(), false);
			}
		}
	}
	
	@EventHandler
	public void fishEvent(PlayerFishEvent event) {
	    if(event.getCaught() instanceof Item){
	    	Main.farmingPointsTracker.put(event.getPlayer().getUniqueId().toString(), Main.farmingPointsTracker.get(event.getPlayer().getUniqueId().toString()) + 1);
	    }
		
	}
	
	@EventHandler
	public void shearEvent(PlayerShearEntityEvent event) {
		Main.farmingPointsTracker.put(event.getPlayer().getUniqueId().toString(), Main.farmingPointsTracker.get(event.getPlayer().getUniqueId().toString()) + 1);
	}
	
	@EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
		
		Player p = e.getPlayer();
       
        if (e.getItem() != null && e.getItem().hasItemMeta()) {
            if (e.getItem().getItemMeta() instanceof PotionMeta) {
           
       
                final PotionMeta meta = (PotionMeta) e.getItem().getItemMeta();
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
                	
                }
                
                
                if (data.isUpgraded()) {
                	
                }
            }
        }
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetLivingEntityEvent event) {
		event.setTarget(null);
	}
	
	
	
	
	
	@SuppressWarnings("unused")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (false) {
			event.getPlayer().setVelocity(new Vector().zero());
		}
		// snow walking
		// event.getPlayer().getWorld().getBlockAt(new Location(event.getPlayer().getWorld(), event.getPlayer().getLocation().getBlockX(), event.getPlayer().getLocation().getBlockY(), event.getPlayer().getLocation().getBlockZ())).setType(Material.SNOW);
	}
	
}