package me.Joey.CommandsPlus;

import java.util.HashMap;

import org.bukkit.entity.Entity;


public interface GlobalHashMaps{
	// creates global interface to hashmaps for player/plugin data tracking
	
	
	// ----------------------------------------------------------------------------------------
	// player info
	HashMap<String, String> playerRankHashMap = new HashMap<String, String>();
	HashMap<String, String> factionHashMap = new HashMap<String, String>();
	HashMap<String, Integer> playerDeathsHashMap = new HashMap<String, Integer>();
	HashMap<String, Float> playerYawHashMap = new HashMap<String, Float>();
	HashMap<String, Integer> playerUnchangedLookDirHashMap = new HashMap<String, Integer>();
	HashMap<String, String> currentOpenInventory = new HashMap<String, String>();
	
	
	// ----------------------------------------------------------------------------------------
	// player weapon data
	HashMap<String, Entity> trackingBowTarget = new HashMap<String, Entity>();
	
	
	// ----------------------------------------------------------------------------------------
	// player weapon cooldowns
	
	
	// ----------------------------------------------------------------------------------------
	// player skill data
	HashMap<String, Integer> miningPointsTracker = new HashMap<String, Integer>();
	HashMap<String, Integer> enchantingPointsTracker = new HashMap<String, Integer>();
	HashMap<String, Integer> combatPointsTracker = new HashMap<String, Integer>();
	HashMap<String, Integer> farmingPointsTracker = new HashMap<String, Integer>();
	HashMap<String, Integer> alchemyPointsTracker = new HashMap<String, Integer>();
	
	
	// ----------------------------------------------------------------------------------------
	// player talent
	HashMap<String, String> talentHashMap = new HashMap<String, String>();
	
	
	// ----------------------------------------------------------------------------------------
	// player talent cooldowns
	
	// Pyrokinetic
	HashMap<String, Integer> fireBurstCooldown = new HashMap<String, Integer>();
	
	// Frostbender
	HashMap<String, Integer> stasisCrystalEnergy = new HashMap<String, Integer>();
	HashMap<String, Integer> cryoCooldown = new HashMap<String, Integer>();
	
	
	// Biokinetic
	HashMap<String, Integer> arcaneCrystalEnergy = new HashMap<String, Integer>();
	
	
	// ----------------------------------------------------------------------------------------
	// player status effects
	HashMap<String, Integer> playerFrozenHashMap = new HashMap<String, Integer>(); // severely slows player movement (from Stasis Crystal)
	HashMap<String, Integer> fireWeaknessHashMap = new HashMap<String, Integer>(); // makes player take more fire tick damage (from Pyrokinetic flaming melee)

	
	// ----------------------------------------------------------------------------------------
	// player command cooldowns
	HashMap<String, Boolean> canTpHashMap = new HashMap<String, Boolean>();
	HashMap<String, Boolean> canSaveDataHashMap = new HashMap<String, Boolean>();
	
	
	// ----------------------------------------------------------------------------------------
	// player settings
	HashMap<String, Boolean> scoreboardHashMap = new HashMap<String, Boolean>();
}