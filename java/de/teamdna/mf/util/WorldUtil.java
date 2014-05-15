package de.teamdna.mf.util;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldUtil {
<<<<<<< HEAD

=======
	
	/**
	 * Sets the given biome at the given coords.
	 */
>>>>>>> c0323ea1c7ab45c5b27c1365f0c0a4aeafdf7b48
	public static void setBiomeForCoords(World world, int x, int z, int biomeID) {
		Chunk chunk = world.getChunkFromBlockCoords(x, z);
		byte[] chunkArray = chunk.getBiomeArray();
		chunkArray[((z & 0xF) << 4 | x & 0xF)] = ((byte)(biomeID & 0xFF));
		chunk.setBiomeArray(chunkArray);
	}
}
