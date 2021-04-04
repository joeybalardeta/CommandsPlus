package me.Joey.CommandsPlus.Events;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;

import me.Joey.CommandsPlus.FunctionsPlus;
import me.Joey.CommandsPlus.Main;
import me.Joey.CommandsPlus.CustomItems.ItemsPlus;
import me.Joey.CommandsPlus.NPC.NPC;
import me.Joey.CommandsPlus.Particles.ParticleData;
import me.Joey.CommandsPlus.Particles.ParticleEffects;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class PlayerEvents implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player online = event.getPlayer();
		
		if (NPC.getNPCs() != null && !NPC.getNPCs().isEmpty()) {
			NPC.addJoinPacket(online);
		}

		
		
		// send welcome message
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Commands_Plus"), new Runnable() {
			public void run() {

				FunctionsPlus.welcomePlayer(online);
			}
		}, 20L);
		
		
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
			//FunctionsPlus.createBoard(event.getPlayer());
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

	
	@EventHandler
	public void onPlayerPublicMessage(PlayerChatEvent event) {
		event.setCancelled(true);
		Player p = event.getPlayer();
		String rank = Main.playerRankHashMap.get(p.getUniqueId().toString());
		
		for (Player online : Bukkit.getOnlinePlayers()) {
		online.sendMessage(ChatColor.WHITE + "[" + FunctionsPlus.getRankColor(p) + rank + ChatColor.WHITE + "] " + p.getName() + ": " + event.getMessage());
		}
		
	}
	
	
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
						trails.damagingFireBurst(p.getLocation(), 10);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.6f);
					}
					
				}
				
			}
			
			if (talent.equals("Hydrokinetic") || talent.equals("Frostbender")) {
				if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.HOT_FLOOR || event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.MELTING) {
					event.setDamage(event.getDamage() * 2);
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
					
					m1 = p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 2, p.getLocation().getBlockZ())).getType();
					if (m1 == Material.AIR || m1 == Material.CAVE_AIR || m1 == Material.WATER || m1 == Material.LAVA) {
						p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 2, p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					}
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ())).setType(Material.AIR);
					p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ())).setType(Material.AIR);
					
					
					
					m1 = p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() - 1, p.getLocation().getBlockY(), p.getLocation().getBlockZ())).getType();
					if (m1 == Material.AIR || m1 == Material.CAVE_AIR || m1 == Material.WATER || m1 == Material.LAVA) {
						p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() - 1, p.getLocation().getBlockY(), p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					}
					
					m1 = p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() + 1, p.getLocation().getBlockY(), p.getLocation().getBlockZ())).getType();
					if (m1 == Material.AIR || m1 == Material.CAVE_AIR || m1 == Material.WATER || m1 == Material.LAVA) {
						p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() + 1, p.getLocation().getBlockY(), p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					}
					
					m1 = p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ() - 1)).getType();
					
					if (m1 == Material.AIR || m1 == Material.CAVE_AIR || m1 == Material.WATER || m1 == Material.LAVA) {
						p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ() - 1)).setType(Material.BLUE_ICE);
					}
					
					m1 = p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ() + 1)).getType();
					
					if (m1 == Material.AIR || m1 == Material.CAVE_AIR || m1 == Material.WATER || m1 == Material.LAVA) {
						p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ() + 1)).setType(Material.BLUE_ICE);
					}
					


					
					
					
					m1 = p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() - 1, p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ())).getType();
					if (m1 == Material.AIR || m1 == Material.CAVE_AIR || m1 == Material.WATER || m1 == Material.LAVA) {
						p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() - 1, p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					}
					
					m1 = p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() + 1, p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ())).getType();
					if (m1 == Material.AIR || m1 == Material.CAVE_AIR || m1 == Material.WATER || m1 == Material.LAVA) {
						p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX() + 1, p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ())).setType(Material.BLUE_ICE);
					}
					
					m1 = p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ() - 1)).getType();
					
					if (m1 == Material.AIR || m1 == Material.CAVE_AIR || m1 == Material.WATER || m1 == Material.LAVA) {
						p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ() - 1)).setType(Material.BLUE_ICE);
					}
					
					m1 = p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ() + 1)).getType();
					
					if (m1 == Material.AIR || m1 == Material.CAVE_AIR || m1 == Material.WATER || m1 == Material.LAVA) {
						p.getWorld().getBlockAt(new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ() + 1)).setType(Material.BLUE_ICE);
					}
					
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 4));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 3));
				}
				
			}
			
			
			if (!talent.equals("Pyrokinetic")) {
				if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.HOT_FLOOR || event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.MELTING) {
					if (Main.fireWeaknessHashMap.get(p.getUniqueId().toString()) > 0) {
						event.setDamage(event.getDamage() * 1.5);
					}
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
		
				LivingEntity victim = (LivingEntity) event.getEntity();
				victim.setFireTicks(100);
				if (((int) (Math.random() * 10)) == 7) {
					ParticleEffects trails = new ParticleEffects(p);
					trails.damagingFireBurst(p.getLocation(), 5);
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
						event.setDamage(100);
					}
					else {
						event.setCancelled(true);
					}
				}
			}
		}
	}
		
	
	@EventHandler
	public void enchantEvent(EnchantItemEvent event) {
		int cost = event.getExpLevelCost();
		
		
		if (cost > 29) { // max enchant level
			Main.enchantingPointsTracker.put(event.getEnchanter().getUniqueId().toString(), Main.enchantingPointsTracker.get(event.getEnchanter().getUniqueId().toString()) + 1);
			
			if (((int) (Math.random() * 5)) == 3) {
				FunctionsPlus.addCustomEnchant(event.getEnchanter(), event.getItem(), "SMELTING", 1);
			}
			
			if (((int) (Math.random() * 5)) == 3) {
				FunctionsPlus.addCustomEnchant(event.getEnchanter(), event.getItem(), "TELEKINESIS", 1);
			}
			
			if (((int) (Math.random() * 5)) == 3) {
				FunctionsPlus.addCustomEnchant(event.getEnchanter(), event.getItem(), "EXPERIENCE", 3);
			}
		}
		else if (cost > 9) { // middle enchant level
			Main.enchantingPointsTracker.put(event.getEnchanter().getUniqueId().toString(), Main.enchantingPointsTracker.get(event.getEnchanter().getUniqueId().toString()) + 1);
			
			if (((int) (Math.random() * 10)) == 7) {
				FunctionsPlus.addCustomEnchant(event.getEnchanter(), event.getItem(), "TELEKINESIS", 1);
			}
			
			if (((int) (Math.random() * 10)) == 7) {
				FunctionsPlus.addCustomEnchant(event.getEnchanter(), event.getItem(), "EXPERIENCE", 2);
			}
		}
		else { // lowest enchant level
			Main.enchantingPointsTracker.put(event.getEnchanter().getUniqueId().toString(), Main.enchantingPointsTracker.get(event.getEnchanter().getUniqueId().toString()) + 1);
			
			if (((int) (Math.random() * 20)) == 7) {
				FunctionsPlus.addCustomEnchant(event.getEnchanter(), event.getItem(), "TELEKINESIS", 1);
			}
			
			if (((int) (Math.random() * 20)) == 7) {
				FunctionsPlus.addCustomEnchant(event.getEnchanter(), event.getItem(), "EXPERIENCE", 1);
			}
		}
		
		
	}
	
	
	
	@EventHandler
	public void fishEvent(PlayerFishEvent event) {
	    if(event.getCaught() instanceof Item){
	    	Main.farmingPointsTracker.put(event.getPlayer().getUniqueId().toString(), Main.farmingPointsTracker.get(event.getPlayer().getUniqueId().toString()) + 1);
	    	if (((int) (Math.random() * 10)) == 7) {
	    		Entity entity = event.getCaught();
	    		Item item = (Item) entity;
	    		item.setItemStack(Main.betterFishingLootTable.get(((int) (Math.random() * Main.betterFishingLootTable.size()))));
	    		
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
}
