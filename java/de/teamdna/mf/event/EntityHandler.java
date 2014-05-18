package de.teamdna.mf.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.EntityEvent;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.damagsource.DamageSourceFracking;

public class EntityHandler {
	
	public static final DamageSource fracing = new DamageSourceFracking();
	
	public void entityEnteringChunkEvent(EntityEvent.EnteringChunk event) {
		if(!(event.entity instanceof EntityLivingBase)) return;
		Chunk chunk = event.entity.worldObj.getChunkFromChunkCoords(event.newChunkX, event.newChunkZ);
		if(chunk == null) return;
		
		boolean infested = false;
		for(byte b : chunk.getBiomeArray()) if(b == (byte)(MineFracturing.INSTANCE.infestedBiome.biomeID & 0xFF)) { infested = true; break; }
		
		if(infested) {
        	((EntityLivingBase)event.entity).addPotionEffect(new PotionEffect(Potion.poison.id, 8000));
			((EntityLivingBase)event.entity).attackEntityFrom(fracing, 0.2F);
		}
	}
	
}
