package me.Joey.CommandsPlus.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;

import me.Joey.CommandsPlus.Main;

public class EntityEvents implements Listener{
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
	
}
