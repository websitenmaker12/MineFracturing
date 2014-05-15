package de.teamdna.mf.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

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
		this.world = DimensionManager.getWorld((int)objs[0]);
		this.x = (int)objs[1];
		this.y = (int)objs[2];
		this.z = (int)objs[3];
	}
	
	public WorldBlock(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String getUID() {
		return Util.createUID(this.world.provider.dimensionId, this.x, this.y, this.z);
	}
	
}
