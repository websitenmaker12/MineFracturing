package de.teamdna.mf.util;

import net.minecraft.world.World;

public class WorldUtil {
	
	public static void setBiomeFoCoords(World world, int x, int z, int biomeID) {
		Chunk chunk = world.getChunkFromBlockCoords(x, z);
	}
}
