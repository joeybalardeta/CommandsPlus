package me.Joey.CommandsPlus.NPC;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.Material;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.Joey.CommandsPlus.FunctionsPlus;
import net.minecraft.server.v1_16_R3.*;

public class NPC {
	private static List<EntityPlayer> NPC = new ArrayList<EntityPlayer>();
	
	
	public static void createNPC(Player p, String skin) {
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) Bukkit.getWorld(p.getWorld().getName())).getHandle();
		GameProfile gameProfile =  new GameProfile(UUID.randomUUID(), ChatColor.AQUA + "Xegis");
		
		EntityPlayer npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
		npc.setLocation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
		
		String[] name = getSkin(p, skin);
		gameProfile.getProperties().put("textures", new Property("textures", name[0], name[1]));
		
		
		addNPCPacket(npc);
		NPC.add(npc);
	}
	
	
	private static String[] getSkin(Player p, String name) {
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			InputStreamReader reader = new InputStreamReader(url.openStream());
			String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
			
			URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader2 = new InputStreamReader(url2.openStream());
			JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
			String texture = property.get("value").getAsString();
			String signature = property.get("signature").getAsString();
			
			return new String[] {texture, signature};
			
		} catch (Exception e) {
			EntityPlayer player = ((CraftPlayer) p).getHandle();
			GameProfile profile =  player.getProfile();
			Property property = profile.getProperties().get("textures").iterator().next();
			String texture = property.getValue();
			String signature = property.getSignature();
			
			return new String[] {texture, signature};
		}
	}
	
	public static void addNPCPacket(EntityPlayer npc) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			PlayerConnection connection = ((CraftPlayer) online).getHandle().playerConnection;
			
			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
			connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
		}
	}
	
	
	public static void addJoinPacket(Player p) {
		for (EntityPlayer npc : NPC) {
			PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
			
			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
			connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
		}
	}
	
	public static void updateNPCPacket() {
		
		for (EntityPlayer npc : NPC) {
			for (Player online : Bukkit.getOnlinePlayers()) {
				PlayerConnection connection = ((CraftPlayer) online).getHandle().playerConnection;
				connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
			}
			
			npcMoveToPlayer(FunctionsPlus.getPlayer("aclownsquad"), npc);
			npcFallCheck(npc);
		}
	}
	
	public static void updateNPCServer() {
		

	}
	
	
	public static List<EntityPlayer> getNPCs(){
		return NPC;
	}
	
	
	
	public static float lookPointYawDegrees(double x1, double z1, double x2, double z2) {

		double x = x2 - x1;
		double z = z2 - z1;
		
		

		return (float) -(Math.atan2(x, z) * 180 / Math.PI);


		

	}
	
	
	public static float lookPointPitchDegrees(double x1, double y1, double z1, double x2, double y2, double z2) {
		double x = x2 - x1;
		double y = y2 - y1;
		double z = z2 - z1;
		double horizontalTotal = Math.hypot(x, z);
		
		
		return (float) (Math.atan2(horizontalTotal, y) * 180 / Math.PI) - 90;
		

	}
	
	
	
	
	
	// NPC General Methods
	public static void npcRelMoveAbsLook(EntityPlayer npc, double x, double y, double z, float yaw, float pitch) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			PlayerConnection connection = ((CraftPlayer) online).getHandle().playerConnection;
			npc.setLocation(npc.lastX + x, npc.lastY + y, npc.lastZ + z, yaw, pitch);
			connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) (npc.yaw * 256 / 360), (byte) (npc.pitch * 256 / 360), true));
			connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(npc.getId(), (short) (x * 4096), (short) (y * 4096), (short) (z * 4096), true));
		}
	}
	
	public static Location getNpcLocation(EntityPlayer npc) {
		return new Location(Bukkit.getWorld("world"), npc.lastX, npc.lastY, npc.lastZ);
	}
	
	public static void npcFallCheck(EntityPlayer npc) {
		boolean isFalling = false;
		for (int i = 0; i < 1; i++) {
			if (getNpcLocation(npc).subtract(0, i, 0).getBlock().getType() == Material.AIR || getNpcLocation(npc).subtract(0, i, 0).getBlock().getType() == Material.CAVE_AIR) {
				isFalling = true;
			}
			
		}
		
		if (isFalling) {
			npcRelMoveAbsLook(npc, 0, -0.5, 0, npc.yaw, npc.pitch);
		}
	}
	
	
	
	public static void npcTakeDamage(EntityPlayer npc) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			PlayerConnection connection = ((CraftPlayer) online).getHandle().playerConnection;
			connection.sendPacket(new PacketPlayOutAnimation(npc, 1));
		}
		
	}
	
	
	public static void npcMoveToPlayer(Player p, EntityPlayer npc) {
		float npcYawDegrees = lookPointYawDegrees(npc.lastX, npc.lastZ, FunctionsPlus.getPlayer("aclownsquad").getLocation().getX(), FunctionsPlus.getPlayer("aclownsquad").getLocation().getZ());
		float npcPitchDegrees = lookPointPitchDegrees(npc.lastX, npc.lastY, npc.lastZ, FunctionsPlus.getPlayer("aclownsquad").getLocation().getX(), FunctionsPlus.getPlayer("aclownsquad").getLocation().getY(), FunctionsPlus.getPlayer("aclownsquad").getLocation().getZ());
		
		float npcYawRadians = (float) Math.toRadians(npcYawDegrees);
		
		double distanceToPlayer = FunctionsPlus.distanceToLocation(npc.lastX, npc.lastY, npc.lastZ, FunctionsPlus.getPlayer("aclownsquad").getLocation().getX(), FunctionsPlus.getPlayer("aclownsquad").getLocation().getY(), FunctionsPlus.getPlayer("aclownsquad").getLocation().getZ());
		double moveX = 0;
		double moveZ = 0;
		
		if (distanceToPlayer > 6) {
			moveX = -Math.sin(npcYawRadians) * 0.2;
			moveZ = Math.cos(npcYawRadians) * 0.2;
		}

		
		npcRelMoveAbsLook(npc, moveX, 0, moveZ, npcYawDegrees, npcPitchDegrees);
	}
}
