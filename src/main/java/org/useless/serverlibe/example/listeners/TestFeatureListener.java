package org.useless.serverlibe.example.listeners;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLightningBolt;
import net.minecraft.core.entity.projectile.EntityFireball;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet63SpawnParticleEffect;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.annotations.EventListener;
import org.useless.serverlibe.api.enums.Priority;
import org.useless.serverlibe.api.event.player.PlayerChatEvent;
import org.useless.serverlibe.api.event.player.PlayerDigEvent;
import org.useless.serverlibe.api.event.player.PlayerEntityInteractEvent;
import org.useless.serverlibe.api.event.player.PlayerItemUseEvent;
import org.useless.serverlibe.api.event.player.PlayerMovementEvent;

import java.util.List;

public class TestFeatureListener implements Listener {
	@EventListener
	public void playerTrail(PlayerMovementEvent movementEvent){
		if (movementEvent.distanceMoved < 0.05) return;
		// Spawns particles at a player's feet as they move
		final EntityPlayerMP playerMP = (EntityPlayerMP)movementEvent.player;
		final boolean movingQuick = movementEvent.distanceMoved > 0.7;
		final String particleKey = movingQuick ? "blueflame" : "flame";
		playerMP.playerNetServerHandler.sendPacket(new Packet63SpawnParticleEffect(particleKey, playerMP.x, playerMP.y, playerMP.z, 0, 0, 0));
	}
	@EventListener(priority = Priority.HIGH)
	public void disableBreak(PlayerDigEvent digEvent){
		// Disable breaking any block if a player is holding a named item
		if (digEvent.player.getHeldItem() != null && digEvent.player.getHeldItem().hasCustomName()) {
			digEvent.setCancelled(true);
		}
	}
	@EventListener(priority = Priority.LOWEST, ignoreCancelled = true)
	public void blockBreakEffect(PlayerDigEvent digEvent){
		// When a block is successfully broken make some explosion particles
		final EntityPlayerMP playerMP = (EntityPlayerMP)digEvent.player;
		playerMP.playerNetServerHandler.sendPacket(new Packet63SpawnParticleEffect
			(
				"explode",
				digEvent.x + 0.5, digEvent.y + 0.5, digEvent.z + 0.5,
				0, 0, 0, 16, (byte) 8,
				0, 0, 0,
				0, 0, 0)
		);
	}
	@EventListener
	public void modifyChat(PlayerChatEvent chatEvent){
		// Modifies chat messages from the player to make them "melon" themed
		// Messages start and end with lime color with red in the center
		final String seperator = "> " + TextFormatting.WHITE;
		final float greenPercent = 0.5f;
		String[] vals = chatEvent.getMessage().split(seperator);
		if (vals.length < 2) return;
		final int greenChars = (int) (vals[1].length() * greenPercent);
		String payload = TextFormatting.LIME + vals[1].substring(0, greenChars/2) + TextFormatting.RED + vals[1].substring(greenChars/2, vals[1].length() - (greenChars/2)) + TextFormatting.LIME + vals[1].substring(vals[1].length() - (greenChars/2));
		chatEvent.setMessage(vals[0] + "> " + payload);
	}
	@EventListener(priority = Priority.HIGH, ignoreCancelled = true)
	public void useSaddleSpecial(PlayerItemUseEvent useEvent){
		// If player holding saddle named "wrangler"
		ItemStack heldItem = useEvent.itemstack;
		if (heldItem.hasCustomName() && heldItem.getCustomName().equalsIgnoreCase("wrangler")){
			List<Entity> entities = useEvent.world.getEntitiesWithinAABBExcludingEntity(useEvent.player, useEvent.player.bb.expand(4, 2, 4));
			if (entities.isEmpty()) return;

			// Find the closest entity to player
			Entity closestEnt = null;
			double closestDist = Double.MAX_VALUE;
			for (Entity e : entities){
				double d = e.distanceTo(useEvent.player);
				if (d < closestDist){
					closestDist = d;
					closestEnt = e;
				}
			}

			// Make player ride the closest entity
			useEvent.player.startRiding(closestEnt);

			// Cancel vanilla use action
			useEvent.setCancelled(true);
		}
	}
	@EventListener(priority = Priority.HIGH, ignoreCancelled = true)
	public void useStickSpecial(PlayerItemUseEvent useEvent){
		// When right-clicking a stick launch a fireball
		if (useEvent.itemstack.getItem() == Item.stick){
			Vec3d look = useEvent.player.getLookAngle();
			double vX = look.xCoord;
			double vY = look.yCoord;
			double vZ = look.zCoord;
			EntityFireball fireball = new EntityFireball(useEvent.world, useEvent.player, vX, vY, vZ);
			fireball.y += 0.9f;
			double velocity = MathHelper.sqrt_double(vX * vX + vY * vY + vZ * vZ);
			if (velocity != 0.0) {
				fireball.accelX = vX / velocity * 0.1;
				fireball.accelY = vY / velocity * 0.1;
				fireball.accelZ = vZ / velocity * 0.1;
			} else {
				fireball.accelX = 0.0;
				fireball.accelY = 0.0;
				fireball.accelZ = 0.0;
			}
			useEvent.world.entityJoinedWorld(fireball);
			useEvent.setCancelled(true);
		}
	}
	@EventListener
	public void onAttack(PlayerEntityInteractEvent interactEvent){
		// When punching an entity with a stick summon lightning
		if (interactEvent.itemstack != null && interactEvent.itemstack.getItem() == Item.stick && interactEvent.mouseButton == PlayerEntityInteractEvent.LEFT_MOUSE_BUTTON){
			EntityLightningBolt lightningBolt = new EntityLightningBolt(interactEvent.world, interactEvent.targetEntity.x, interactEvent.targetEntity.y, interactEvent.targetEntity.z);
			interactEvent.world.addWeatherEffect(lightningBolt);
			interactEvent.setCancelled(true);
		}
	}
}
