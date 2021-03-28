package me.Joey.CommandsPlus.Events;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import me.Joey.CommandsPlus.FunctionsPlus;
import me.Joey.CommandsPlus.Main;
import me.Joey.CommandsPlus.CustomInventories.FactionOptionsInventory;
import me.Joey.CommandsPlus.CustomInventories.InventoryManager;
import me.Joey.CommandsPlus.CustomInventories.RecipeInventoryCreator;
import me.Joey.CommandsPlus.CustomItems.ItemsPlus;
import me.Joey.CommandsPlus.Particles.ParticleData;
import me.Joey.CommandsPlus.Particles.ParticleEffects;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class InventoryEvents implements Listener {
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
	public void onInventoryClose(InventoryCloseEvent event) {
		Main.currentOpenInventory.put(event.getPlayer().getUniqueId().toString(), "None");
	}
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		//p.sendMessage(Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()));
		//p.sendMessage(event.getInventory().getType().toString());
		
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
				p.closeInventory();
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
				return;
			}
			
			// Commands+ Crafts Menu selected
			if (event.getSlot() == 20) {
				p.openInventory(InventoryManager.masterCraftsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Master Crafts Menu");
				return;
			}
			
			// Talent Menu selected
			if (event.getSlot() == 21) {
				p.openInventory(InventoryManager.talentInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Talent Menu");
				return;
			}
			
			// Faction Options Menu Selected
			if (event.getSlot() == 24) {
				FactionOptionsInventory.createFactionOptionsInventory(p);
				p.openInventory(InventoryManager.factionOptionsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Faction Menu");
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
				p.openInventory(InventoryManager.weaponCraftsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Weapon Crafts Menu");
				return;
			}
			
			
			if (event.getSlot() == 20) {
				p.openInventory(InventoryManager.toolCraftsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Tool Crafts Menu");
				return;
			}
			
			
			if (event.getSlot() == 21) {
				p.openInventory(InventoryManager.armorCraftsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Armor Crafts Menu");
				return;
			}
			
			
			if (event.getSlot() == 22) {
				p.openInventory(InventoryManager.potionCraftsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Potion Crafts Menu");
				return;
			}
			
			
			if (event.getSlot() == 23) {
				p.openInventory(InventoryManager.miscellaneousCraftsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Miscellaneous Crafts Menu");
				return;
			}
			
			
			if (event.getSlot() == 24) {
				p.openInventory(InventoryManager.customCraftsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Existing Item Crafts Menu");
				return;
			}
			
			
			if (event.getSlot() == 25) {
				p.openInventory(InventoryManager.talentItemCraftsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Talent Item Crafts Menu");
				return;
			}
			
			
			if (event.getSlot() == 31) {
				p.openInventory(InventoryManager.enchantedBookCraftsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Enchanted Book Crafts Menu");
				return;
			}
			
			// Go Back selected
			if (event.getSlot() == 48) {
				p.openInventory(InventoryManager.masterMenuInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Master Menu");
				return;
			}
			
			
			// Close Menu selected
			if (event.getSlot() == 49) {
				p.closeInventory();
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
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
				p.openInventory(InventoryManager.masterMenuInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Master Menu");
				return;
			}
			
			
			// Close Menu selected
			if (event.getSlot() == 49) {
				p.closeInventory();
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
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
				p.openInventory(InventoryManager.masterMenuInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Master Menu");
				return;
			}
						
			// Close Menu selected
			if (event.getSlot() == 49) {
				p.closeInventory();
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
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
				if (p.getInventory().getChestplate() != null) {
					p.getInventory().addItem(p.getInventory().getChestplate());
				}
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
				
				// Start particle effects
				trails.startHydrokineticParticles();
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
		
		else if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Crafts Menu")) {
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
			
			
			// Item selected
			if (event.getSlot() != 48 && event.getSlot() != 49 && event.getSlot() != 50 && !event.getCurrentItem().getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
				
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Weapon Crafts Menu")) {
					p.openInventory(RecipeInventoryCreator.createRecipeInventory("Weapons", event.getCurrentItem()));
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Weapon Recipe Menu");
					return;
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Tool Crafts Menu")) {
					p.openInventory(RecipeInventoryCreator.createRecipeInventory("Tools", event.getCurrentItem()));
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Tool Recipe Menu");
					return;
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Armor Crafts Menu")) {
					p.openInventory(RecipeInventoryCreator.createRecipeInventory("Armor", event.getCurrentItem()));
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Armor Recipe Menu");
					return;				
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Potion Crafts Menu")) {
					p.openInventory(RecipeInventoryCreator.createRecipeInventory("Potions", event.getCurrentItem()));
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Potion Recipe Menu");
					return;
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Talent Crafts Menu")) {
					p.openInventory(RecipeInventoryCreator.createRecipeInventory("Talent Items", event.getCurrentItem()));
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Talent Item Recipe Menu");
					return;
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Miscellaneous Crafts Menu")) {
					p.openInventory(RecipeInventoryCreator.createRecipeInventory("Miscellaneous", event.getCurrentItem()));
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Miscellaneous Recipe Menu");
					return;			
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Enchanted Book Crafts Menu")) {
					p.openInventory(RecipeInventoryCreator.createRecipeInventory("Enchanted Books", event.getCurrentItem()));
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Enchanted Book Recipe Menu");
					return;
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Existing Item Crafts Menu")) {
					p.openInventory(RecipeInventoryCreator.createRecipeInventory("Existing Items", event.getCurrentItem()));
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Existing Item Recipe Menu");
					return;
				}
				
			}
			
			// Go Back selected
			if (event.getSlot() == 48) {
				p.openInventory(InventoryManager.masterCraftsInventory);
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "Master Crafts Menu");
				return;
			}
			
			
			// Close Menu selected
			if (event.getSlot() == 49) {
				p.closeInventory();
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
				return;
			}
		}
		
		else if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Recipe Menu")) {
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
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Weapon Recipe Menu")) {
					p.openInventory(InventoryManager.weaponCraftsInventory);
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Weapon Crafts Menu");
					return;
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Tool Recipe Menu")) {
					p.openInventory(InventoryManager.toolCraftsInventory);
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Tool Crafts Menu");
					return;
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Armor Recipe Menu")) {
					p.openInventory(InventoryManager.armorCraftsInventory);
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Armor Crafts Menu");
					return;				
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Potion Recipe Menu")) {
					p.openInventory(InventoryManager.potionCraftsInventory);
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Potion Crafts Menu");
					return;
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Talent Item Recipe Menu")) {
					p.openInventory(InventoryManager.talentItemCraftsInventory);
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Talent Crafts Menu");
					return;
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Miscellaneous Recipe Menu")) {
					p.openInventory(InventoryManager.miscellaneousCraftsInventory);
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Miscellaneous Crafts Menu");
					return;			
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Enchanted Book Recipe Menu")) {
					p.openInventory(InventoryManager.enchantedBookCraftsInventory);
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Enchanted Book Crafts Menu");
					return;
				}
				if (Main.currentOpenInventory.get(event.getWhoClicked().getUniqueId().toString()).contains("Existing Item Recipe Menu")) {
					p.openInventory(InventoryManager.customCraftsInventory);
					Main.currentOpenInventory.put(p.getUniqueId().toString(), "Existing Item Crafts Menu");
					return;
				}
			}
			
			
			// Close Menu selected
			if (event.getSlot() == 49) {
				p.closeInventory();
				Main.currentOpenInventory.put(p.getUniqueId().toString(), "None");
				return;
			}
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
}
