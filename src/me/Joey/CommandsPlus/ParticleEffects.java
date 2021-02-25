package me.Joey.CommandsPlus;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleEffects {
	
	private int taskID;
	private final Player p;
	
	
	public ParticleEffects(Player p) {
		this.p = p;
	}
	
	
	// basic particle art functions
	public void startFlameParticles() {
		
		
		// check every 250ms for potion effects
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double var = 0;
			Location loc, first, second;
			ParticleData particle = new ParticleData(p.getUniqueId());
			
            @Override
            public void run() {
            	if (!particle.hasID()) {
            		particle.setID(taskID);
            	}
            	
            	var += Math.PI / 16;
            	
            	loc = p.getLocation();
            	first = loc.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
            	second = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));
            	
            	p.getWorld().spawnParticle(Particle.FLAME, first, 0);
            	p.getWorld().spawnParticle(Particle.FLAME, second, 0);
            }
		}, 0, 1L);
	}
	
	
	// Ambient particle effects (repeating)
	
	// talent particle functions
	
	// Pyrokinetic particles
	public void startPyrokineticParticles() {
		
		
		// check every 250ms for potion effects
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double var = 0;
			Location loc, first; //, second, third;
			ParticleData particle = new ParticleData(p.getUniqueId());
			
			
            @Override
            public void run() {
            	if (!particle.hasID()) {
            		particle.setID(taskID);
            	}
            	
            	var += Math.PI / 16;
            	
            	
            	
            	loc = p.getLocation();
            	first = loc.clone().add(Math.cos(var), 0.0, Math.sin(var));
            	//second = loc.clone().add(Math.cos(var + (Math.PI) * (2.0 / 3)), 1.0, Math.sin(var + (Math.PI) * (2.0 / 3)));
            	//third = loc.clone().add(Math.cos(var + (Math.PI) * (4.0 / 3)), 2.0, Math.sin(var + (Math.PI) * (4.0 / 3)));
            	
            	p.getWorld().spawnParticle(Particle.FLAME, first, 0);
            	//p.getWorld().spawnParticle(Particle.FLAME, second, 0);
            	//p.getWorld().spawnParticle(Particle.FLAME, third, 0);
            }
		}, 0, 1L);
	}
	
	// Frostbender particles
	public void startFrostbenderParticles() {
		
		
		// check every 250ms for potion effects
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double var = 0;
			Location loc, first; //, second, third;
			ParticleData particle = new ParticleData(p.getUniqueId());
			int r = 0;
			int g = 153;
			int b = 255;
			
			
            @Override
            public void run() {
            	if (!particle.hasID()) {
            		particle.setID(taskID);
            	}
            	
            	var += Math.PI / 16;
            	
            	
            	
            	loc = p.getLocation();
            	first = loc.clone().add(Math.cos(var), 0.0, Math.sin(var));
            	//second = loc.clone().add(Math.cos(var + (Math.PI) * (2.0 / 3)), 1.0, Math.sin(var + (Math.PI) * (2.0 / 3)));
            	//third = loc.clone().add(Math.cos(var + (Math.PI) * (4.0 / 3)), 2.0, Math.sin(var + (Math.PI) * (4.0 / 3)));
            	
            	// create particle color variable
            	Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(r, g, b), 1);
            	
            	
            	p.getWorld().spawnParticle(Particle.REDSTONE, first, 0, 0, 0, 0, dust);
            	//p.getWorld().spawnParticle(Particle.FLAME, second, 0);
            	//p.getWorld().spawnParticle(Particle.FLAME, third, 0);
            }
		}, 0, 1L);
	}
	
	
	// Active particle effects (usually one time, not repeating)
	
	// Level Up
	public void levelUp() {
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double var = 0;
			Location loc, first, second;
			int i = 0;
			
			@Override
	        public void run() {
				var += Math.PI / 16;
				loc = p.getLocation();
		    	first = loc.clone().add(Math.cos(var), 0.0, Math.sin(var));
		    	second = loc.clone().add(Math.cos(var + (Math.PI)), 1.0, Math.sin(var + (Math.PI)));
		    	
				p.getWorld().spawnParticle(Particle.TOTEM, first, 0);
		    	p.getWorld().spawnParticle(Particle.TOTEM, second, 0);
		    	i++;
		    	
		    	if (i > 32) {
		    		Bukkit.getScheduler().cancelTask(taskID);
		    	}
			}
		}, 0, 1L);
		
		
	}
	
}
