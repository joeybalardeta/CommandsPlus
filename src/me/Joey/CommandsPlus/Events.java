package me.Joey.CommandsPlus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

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
		
		if (event.getSlot() == 26) {
			p.closeInventory();
			return;
		}
		
		if (Main.playerDataConfig.contains("Users." + p.getUniqueId() + ".stats" + ".talent") && !p.hasPermission("commandsPlus.tester")) {
			p.closeInventory();
			p.sendMessage("You have already picked a talent!");
			return;
		}
		
		if (event.getSlot() == 0) {
			p.sendMessage("You chose the Avian talent!");
			Main.talentHashMap.put(p.getUniqueId().toString(), "Avian");
			Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Avian");
			p.closeInventory();
		}
		
		if (event.getSlot() == 1) {
			p.sendMessage("You chose the Pyrokinetic talent!");
			Main.talentHashMap.put(p.getUniqueId().toString(), "Pyrokinetic");
			Main.playerDataConfig.set("Users." + p.getUniqueId() + ".stats" + ".talent", "Pyrokinetic");
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
			Player p = (Player) e.getEntity();
			Main.talentHashMap.get(p.getUniqueId().toString()).replace("\\d", "");
			if (Main.talentHashMap.get(p.getUniqueId().toString()).equals("Avian")) {
				e.setCancelled(true);
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
    public void explosionEvent(ExplosionPrimeEvent e) {
		if (e.getEntity() instanceof TNTPrimed) {
			e.setRadius(e.getRadius() * 20);
		}
	}

}