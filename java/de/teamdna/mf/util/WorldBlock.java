package de.teamdna.mf.util;

import net.minecraft.block.Block;
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
		this.world = DimensionManager.getWorld(Integer.parseInt(objs[0].toString()));
		this.x = Integer.parseInt(objs[1].toString());
		this.y = Integer.parseInt(objs[2].toString());
		this.z = Integer.parseInt(objs[3].toString());
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
	
	public Block getBlock() {
		return this.world.getBlock(this.x, this.y, this.z);
	}
	
}
