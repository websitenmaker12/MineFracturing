package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import de.teamdna.mf.api.CoreRegistry;
import de.teamdna.mf.util.Util;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

public class TileEntityBore extends TileEntity {

	public final int maxBoreY = 8;
	
	public int state = -1; // Inactive: -1 ; Boring: 0 ; Scanning Chunks: 1; Injecting: 2
	public int boreY;
	public int radius;
	
	public List<String> oreBlocks = new ArrayList<String>();
	public List<ChunkCoordIntPair> chunkQueue = new ArrayList<ChunkCoordIntPair>();
	public Chunk currentScanningChunk;
	private int scanX = 0, scanY = 0, scanZ = 0;
	
	@Override
	public void updateEntity() {
		if(!this.worldObj.isRemote) {
			// Setups the bore
			if(this.state == -1) {
				this.state = 0;
				this.boreY = this.yCoord;
				this.scanY = this.yCoord - 1;
				this.changeChunkRadius(4);
			}
			
			// Bores to a hole until it reaches maxBoreY
			if(this.state == 0 && this.worldObj.getWorldTime() % 10L == 0L) {
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
				if(this.currentScanningChunk == null) {
					ChunkCoordIntPair coord = this.chunkQueue.get(0);
					this.currentScanningChunk = this.worldObj.getChunkFromChunkCoords(coord.chunkXPos, coord.chunkZPos);
					if(this.currentScanningChunk == null) return;
				}
				
				if(this.scanX == 15 && this.scanZ == 15) this.scanY--;
				if(this.scanY <= 0) {
					this.scanX = 0;
					this.scanZ = 0;
					this.chunkQueue.remove(0);
					this.currentScanningChunk = null;
				} else {
					Block block = this.currentScanningChunk.getBlock(this.scanX, this.scanY, this.scanZ);
					if(block != null && block != Blocks.air && CoreRegistry.isOre(block)) {
						String uid = Util.createUID(this.worldObj.provider.dimensionId, this.scanX, this.scanY, this.scanZ);
						if(!this.oreBlocks.contains(uid)) this.oreBlocks.add(uid);
					}
					
					if(++this.scanX == 16) {
						this.scanX = 0;
						this.scanZ++;
					}
				}
			}
			
			// Starts infecting the world and earning resources
			if(this.state == 2) {
			}
			
//			System.out.println(this.oreBlocks.size());
			System.out.println(this.currentScanningChunk);
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
