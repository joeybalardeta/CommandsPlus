package me.Joey.CommandsPlus.CustomEnchantments;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentsPlus{

	public static final Enchantment TELEKINESIS = new EnchantmentWrapper("telekinesis", "Telekinesis", 1);
	public static final Enchantment SMELTING = new EnchantmentWrapper("smelting", "Smelting", 1);
	public static final Enchantment EXPERIENCE = new EnchantmentWrapper("experience", "Experience", 3);
	
	public static void register() {
		boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(TELEKINESIS);
		
		if (!registered) {
			registerEnchantment(TELEKINESIS);
		}
		
		registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(SMELTING);
		
		if (!registered) {
			registerEnchantment(SMELTING);
		}
		
		registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(EXPERIENCE);
		
		if (!registered) {
			registerEnchantment(EXPERIENCE);
		}
	}
	
	public static void registerEnchantment(Enchantment enchantment) {
		boolean registered = true;
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
			Enchantment.registerEnchantment(enchantment);
		} catch(Exception e) {
			registered = false;
			e.printStackTrace();
		}
		
		if (registered) {
			// send message to console
		}
	}

}
