package me.Joey.CommandsPlus.Events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.Joey.CommandsPlus.FunctionsPlus;
import me.Joey.CommandsPlus.Main;
import me.Joey.CommandsPlus.Particles.ParticleEffects;
import net.md_5.bungee.api.ChatColor;

public class TalentEvents implements Listener{
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.ENDER_PEARL && Main.talentHashMap.get(event.getPlayer().getUniqueId().toString()) != null && Main.talentHashMap.get(event.getPlayer().getUniqueId().toString()).equals("Enderian")) {
			event.getPlayer().getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
		}
	}
	
	// Item Rename Preventer
	
	
	
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

}
