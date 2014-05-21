package de.teamdna.mf.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.damagsource.DamageSourceFracking;
import de.teamdna.mf.util.Util;

public class EntityHandler {
	
	public static final DamageSource fracing = new DamageSourceFracking();
	
//	@SubscribeEvent
//	public void entityEnteringChunkEvent(EntityEvent.EnteringChunk event) {
//		System.out.println(event.entity);
//		if(!(event.entity instanceof EntityLivingBase)) return;
//		Chunk chunk = event.entity.worldObj.getChunkFromChunkCoords(event.newChunkX, event.newChunkZ);
//		if(chunk == null || !chunk.isChunkLoaded) return;
//		
//		boolean infested = false;
//		for(byte b : chunk.getBiomeArray()) if(b == (byte)(MineFracturing.INSTANCE.infestedBiome.biomeID & 0xFF)) { infested = true; break; }
//		
//		if(infested) {
//        	((EntityLivingBase)event.entity).addPotionEffect(new PotionEffect(Potion.poison.id, 8000));
//			((EntityLivingBase)event.entity).attackEntityFrom(fracing, 0.2F);
//		}
//	}
	
	@SubscribeEvent
	public void tickEvent(PlayerTickEvent event) {
		EntityPlayer p = event.player;

		ItemStack jetpack = p.inventory.armorInventory[2];
		if(jetpack != null && jetpack.getItem() == MineFracturing.INSTANCE.jetpack && jetpack.getItemDamage() < jetpack.getMaxDamage() - 2 && MineFracturing.keys.isJumpKeyDown(p)) {
			if(p.motionY > 0) p.motionY += 0.086999999105930327D;
            else p.motionY += 0.11699999910593033D;
			if(p.motionY < 0) p.motionY /= 1.1499999761581421D;
            if(!p.onGround) {
            	p.motionX *= 1.0399999618530273D;
            	p.motionZ *= 1.0399999618530273D;
            }

            Vec3 vec = p.getLookVec();
            p.worldObj.spawnParticle("explode", p.posX - vec.xCoord * 1.2D, p.posY - 1.4D, p.posZ - vec.zCoord * 1.2D, 0, -1, 0);
            p.fallDistance = 0F;
            p.distanceWalkedModified = 0F;
            Util.resetPlayerFlyTicks(p);
            
            jetpack.damageItem(1, p);
		}
	}
	
}
