package me.Joey.CommandsPlus.Events;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Joey.CommandsPlus.FunctionsPlus;
import me.Joey.CommandsPlus.Main;
import me.Joey.CommandsPlus.CustomEnchantments.EnchantmentsPlus;
import net.md_5.bungee.api.ChatColor;

public class WorldEvents implements Listener{
	
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
}
