package me.Joey.CommandsPlus;

import java.util.HashMap;

public interface GlobalHashMaps{
	// creates global interface to hashmaps for player/plugin data tracking
	
	// player skill data
	HashMap<String, Integer> miningPointsTracker = new HashMap<String, Integer>();
	HashMap<String, Integer> enchantingPointsTracker = new HashMap<String, Integer>();
	HashMap<String, Integer> combatPointsTracker = new HashMap<String, Integer>();
	HashMap<String, Integer> farmingPointsTracker = new HashMap<String, Integer>();
	HashMap<String, Integer> alchemyPointsTracker = new HashMap<String, Integer>();
	
	
	// player talent
	HashMap<String, String> talentHashMap = new HashMap<String, String>();

	
	// player cooldowns
	HashMap<String, Boolean> canTpHashMap = new HashMap<String, Boolean>();
	HashMap<String, Boolean> canSaveDataHashMap = new HashMap<String, Boolean>();
	
	
	// player settings
	HashMap<String, Boolean> scoreboardHashMap = new HashMap<String, Boolean>();
}