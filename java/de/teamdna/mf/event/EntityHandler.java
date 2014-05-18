package de.teamdna.mf.event;

import de.teamdna.mf.MineFracturing;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.EntityEvent;

public class EntityHandler {
	
	public void entityEnteringChunkEvent(EntityEvent.EnteringChunk event) {
		if(!(event.entity instanceof EntityLivingBase)) return;
		Chunk chunk = event.entity.worldObj.getChunkFromChunkCoords(event.newChunkX, event.newChunkZ);
		if(chunk == null) return;
		
		boolean infested = false;
		for(byte b : chunk.getBiomeArray()) if(b == (byte)(MineFracturing.INSTANCE.infestedBiome.biomeID & 0xFF)) { infested = true; break; }
		
		if(infested) {
        	((EntityLivingBase)event.entity).addPotionEffect(new PotionEffect(Potion.poison.id, 8000));
			((EntityLivingBase)event.entity).attackEntityFrom(null, 0.2F);
		}
	}
	
}
