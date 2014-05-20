package de.teamdna.mf.util;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class WorldBlock {

	public World world;
	public int x, y, z, meta;
	public TileEntity tile;
	
	public WorldBlock(String id) {
		Object[] uid = Util.splitUID(id);
		
		this.world = DimensionManager.getWorld(Integer.parseInt(String.valueOf(uid[0])));
		this.x = Integer.parseInt(String.valueOf(uid[1]));
		this.y = Integer.parseInt(String.valueOf(uid[2]));
		this.z = Integer.parseInt(String.valueOf(uid[3]));
		
		this.meta = this.world.getBlockMetadata(this.x, this.y, this.z);
		this.tile = this.world.getTileEntity(this.x, this.y, this.z);
	}
	
	public WorldBlock(TileEntity tile) {
		this(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	public WorldBlock(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.meta = world.getBlockMetadata(x, y, z);
		this.tile = world.getTileEntity(x, y, z);
	}
	
	public WorldBlock(World world, String id) {
		this.world = world;

		Object[] uid = Util.splitUID(id);
		this.x = Integer.parseInt(String.valueOf(uid[0]));
		this.y = Integer.parseInt(String.valueOf(uid[1]));
		this.z = Integer.parseInt(String.valueOf(uid[2]));
		
		this.meta = this.world.getBlockMetadata(this.x, this.y, this.z);
		this.tile = this.world.getTileEntity(this.x, this.y, this.z);
	}
	
	public World updateWorld() {
		return this.world = DimensionManager.getWorld(this.world.provider.dimensionId);
	}
	
	public TileEntity updateTileEntity() {
		return this.tile = this.world.getTileEntity(this.x, this.y, this.z);
	}
	
	public String getBlockUID() {
		return Util.createUID(this.world.provider.dimensionId, this.x, this.y, this.z);
	}
	
	public boolean exists() {
		return !this.world.isAirBlock(this.x, this.y, this.z);
	}
	
	public boolean hasTile() {
		return this.updateTileEntity() != null;
	}
	
	public int getChunkX() {
		return this.x >> 4;
	}
	
	public int getChunkZ() {
		return this.z >> 4;
	}
	
	public boolean isContainerChunkLoaded() {
		this.updateWorld();
		return this.world.getPersistentChunks().containsKey(new ChunkCoordIntPair(this.getChunkX(), this.getChunkZ()));
	}
	
	public Block getBlock() {
		return this.updateWorld().getBlock(this.x, this.y, this.z);
	}
	
}
