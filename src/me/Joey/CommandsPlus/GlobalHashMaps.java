package me.Joey.CommandsPlus;

import java.util.HashMap;

public interface GlobalHashMaps{
	// creates global interface to hashmaps for player stat tracking	
	HashMap<String, Integer> blocksMinedHashMap = new HashMap<String, Integer>();
	HashMap<String, Integer> itemsCraftedHashMap = new HashMap<String, Integer>();
	HashMap<String, Integer> itemsEnchantedHashMap = new HashMap<String, Integer>();
	HashMap<String, Integer> mobsKilledHashMap = new HashMap<String, Integer>();
	HashMap<String, Integer> itemsSmeltedHashMap = new HashMap<String, Integer>();
	HashMap<String, Integer> itemsConsumedHashMap = new HashMap<String, Integer>();
	HashMap<String, Integer> itemsFishedHashMap = new HashMap<String, Integer>();
	HashMap<String, Integer> mobsShearedHashMap = new HashMap<String, Integer>();
	HashMap<String, Boolean> canSaveDataHashMap = new HashMap<String, Boolean>();
	HashMap<String, Boolean> canTpHashMap = new HashMap<String, Boolean>();
}