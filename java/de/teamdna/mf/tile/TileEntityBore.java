package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;

public class TileEntityBore extends TileEntity {

	public final int maxBoreY = 8;
	
	public int state = -1; // Inactive: -1 ; Boring: 0 ; Scanning Chunks: 1; Injecting: 2
	public int boreY;
	public int radius;
	
	public List<String> oreBlocks = new ArrayList<String>();
	public List<ChunkCoordIntPair> chunkQueue = new ArrayList<ChunkCoordIntPair>();
	public ChunkCoordIntPair currentScanningChunk;
	
	@Override
	public void updateEntity() {
		if(!this.worldObj.isRemote) {
			// Setups the bore
			if(this.state == -1) {
				this.state = 0;
				this.boreY = this.yCoord;
				this.changeChunkRadius(4);
			}
			
			// Bores to a hole until it reaches maxBoreY
			if(this.state == 0 && this.worldObj.getWorldTime() % 40L == 0L) {
				if(--this.boreY < this.maxBoreY) {
					this.state = 1;
					return;
				}
				
				Block currentBlock = this.worldObj.getBlock(this.xCoord, this.boreY, this.zCoord);
				if(!currentBlock.getMaterial().isLiquid() && currentBlock.getBlockHardness(this.worldObj, this.xCoord, this.boreY, this.zCoord) > 0) {
					this.worldObj.func_147480_a(this.xCoord, this.boreY, this.zCoord, false);
				}
			}
			
			// Scanning Chunks
			if(this.state == 1 && (this.chunkQueue.size() > 0 || this.currentScanningChunk != null)) {
				if(this.currentScanningChunk == null) this.currentScanningChunk = this.chunkQueue.get(0);
			}
			
			// Starts infecting the world and earning resources
			if(this.state == 2) {
			}
		}
	}
	
	public void changeChunkRadius(int radius) {
		this.radius = radius;
		for(int x = -radius; x <= radius; x++) {
			for(int z = -radius; z <= radius; z++) {
				ChunkCoordIntPair coord = new ChunkCoordIntPair(this.xCoord >> 4 + x, this.zCoord >> 4 + z);
				if(this.chunkQueue.contains(coord)) {
					this.chunkQueue.add(coord);
				}
			}
		}
	}
	
}
