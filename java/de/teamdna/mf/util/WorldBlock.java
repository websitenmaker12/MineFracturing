package de.teamdna.mf.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WorldBlock {

	public World world;
	public int x, y, z;
	
	public WorldBlock() {
	}
	
	public WorldBlock(TileEntity tile) {
		this(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	public WorldBlock(String blockID) {
		Object[] objs = Util.splitUID(blockID);
	}
	
	public WorldBlock(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
}
