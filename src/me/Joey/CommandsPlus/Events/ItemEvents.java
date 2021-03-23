package me.Joey.CommandsPlus.Events;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Joey.CommandsPlus.FunctionsPlus;
import me.Joey.CommandsPlus.Main;
import me.Joey.CommandsPlus.CustomItems.ItemsPlus;
import net.md_5.bungee.api.ChatColor;

public class ItemEvents implements Listener{
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

}
