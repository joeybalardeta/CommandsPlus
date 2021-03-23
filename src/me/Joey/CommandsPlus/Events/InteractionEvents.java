package me.Joey.CommandsPlus.Events;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.Joey.CommandsPlus.FunctionsPlus;
import me.Joey.CommandsPlus.Main;
import me.Joey.CommandsPlus.CustomEnchantments.EnchantmentsPlus;
import me.Joey.CommandsPlus.CustomItems.ItemsPlus;
import net.md_5.bungee.api.ChatColor;


public class InteractionEvents implements Listener {
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
	        		 FunctionsPlus.addCustomEnchant(p, inv.getItemInOffHand(), "TELEKINESIS", 1);
		     		 p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
	        	 }
	        	 
	    		else if (inv.getItemInMainHand().equals(ItemsPlus.smeltingBook) && !inv.getItemInOffHand().containsEnchantment(EnchantmentsPlus.SMELTING) && (inv.getItemInOffHand().getType().toString().contains("AXE") || inv.getItemInOffHand().getType().toString().contains("SHOVEL"))) {
	        		 inv.getItemInMainHand().setAmount(0); 
		        	 FunctionsPlus.addCustomEnchant(p, inv.getItemInOffHand(), "SMELTING", 1);
		     		 p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
	        	 }
	    		else if (inv.getItemInMainHand().equals(ItemsPlus.experienceBook) && !inv.getItemInOffHand().containsEnchantment(EnchantmentsPlus.EXPERIENCE) && (inv.getItemInOffHand().getType().toString().contains("BOW") || inv.getItemInOffHand().getType().toString().contains("TRIDENT") || inv.getItemInOffHand().getType().toString().contains("SWORD") || inv.getItemInOffHand().getType().toString().contains("AXE") || inv.getItemInOffHand().getType().toString().contains("SHOVEL"))) {
	        		 inv.getItemInMainHand().setAmount(0); 
	        		 FunctionsPlus.addCustomEnchant(p, inv.getItemInOffHand(), "EXPERIENCE", inv.getItemInMainHand().getEnchantmentLevel(EnchantmentsPlus.EXPERIENCE));
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
}
