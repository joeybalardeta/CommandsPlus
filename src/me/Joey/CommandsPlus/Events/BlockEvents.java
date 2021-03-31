package me.Joey.CommandsPlus.Events;

import java.util.Collection;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import me.Joey.CommandsPlus.FunctionsPlus;
import me.Joey.CommandsPlus.Main;
import me.Joey.CommandsPlus.CustomEnchantments.EnchantmentsPlus;
import me.Joey.CommandsPlus.CustomItems.ItemsPlus;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class BlockEvents implements Listener {
	@EventHandler
	public void blockBreak(BlockBreakEvent event) {
		if (event.getPlayer() != null) {
			Main.miningPointsTracker.put(event.getPlayer().getUniqueId().toString(), Main.miningPointsTracker.get(event.getPlayer().getUniqueId().toString()) + 1);
			if (Main.canSaveDataHashMap.get(event.getPlayer().getUniqueId().toString())){
				FunctionsPlus.savePlayerData(event.getPlayer(), false);
			}
			
			Player p = event.getPlayer();
	        PlayerInventory inv = p.getInventory();
	 
	        
	 
	        if (inv.getItemInMainHand().equals(ItemsPlus.timberAxe) && Main.logMaterials.contains(event.getBlock().getType())) {
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
	        
	        if (inv.getItemInMainHand().equals(ItemsPlus.redstonePickaxe)) {
	        	
	        	Location location = event.getBlock().getLocation();
	        	String directionYaw = FunctionsPlus.getPlayerYawDirectionCardinal(p);
	        	String directionPitch = FunctionsPlus.getBiasedPlayerPitchDirectionCardinal(p);
	            LinkedList<Block> blocks = new LinkedList<>();
	            ItemStack handStack = p.getItemInHand();
	            
	            // if vertical
	            if (directionPitch.equals("Horizontal")) {
	            	for (int x = -1; x <= 1; x++) {
		            	for (int z = -1; z <= 1; z++) {
		            		Location blockLoc = new Location(location.getWorld(), location.getX() + x, location.getY(), location.getZ() + z);
		            		blocks.add(blockLoc.getBlock());
		            	}
		            }
	            }
	            
	            if (directionPitch.equals("Vertical") && (directionYaw.equals("East") || directionYaw.equals("West"))) {
	            	for (int y = -1; y <= 1; y++) {
		            	for (int z = -1; z <= 1; z++) {
		            		Location blockLoc = new Location(location.getWorld(), location.getX(), location.getY() + y, location.getZ() + z);
		            		blocks.add(blockLoc.getBlock());
		            	}
		            }
	            }
	            
	            
	            if (directionPitch.equals("Vertical") && (directionYaw.equals("North") || directionYaw.equals("South"))) {
	            	for (int x = -1; x <= 1; x++) {
		            	for (int y = -1; y <= 1; y++) {
		            		Location blockLoc = new Location(location.getWorld(), location.getX() + x, location.getY() + y, location.getZ());
		            		blocks.add(blockLoc.getBlock());
		            	}
		            }
	            }
	            p.sendMessage(directionPitch + ", " + directionYaw);
	            
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
	

}
