package de.teamdna.mf.util;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldUtil {
	
	/**
	 * Sets the given biome at the given coords.
	 */
	public static void setBiomeForCoords(World world, int x, int z, int biomeID) {
		Chunk chunk = world.getChunkFromBlockCoords(x, z);
		byte[] chunkArray  = chunk.getBiomeArray();
		chunkArray[((z & 0xF) << 4 | x & 0xF)] = ((byte)(biomeID & 0xFF));
		chunk.setBiomeArray(chunkArray);
	}
}
