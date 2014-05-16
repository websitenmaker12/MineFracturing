package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.api.CoreRegistry;
import de.teamdna.mf.util.Util;
import de.teamdna.mf.util.WorldUtil;

public class TileEntityBore extends TileEntity {

	public final int maxBoreY = 8;
	public final int structureHeight = 7;
	
	public int state = -1; // Inactive: -1 ; Boring: 0 ; Scanning Chunks: 1; Infesting: 2
	public int boreY;
	public int radius;
	
	public List<String> oreBlocks = new ArrayList<String>();
	public int totalOres = 0;
	public List<ChunkCoordIntPair> chunkQueue = new ArrayList<ChunkCoordIntPair>();
	public Chunk currentScanningChunk;
	private int scanY = 0;
	private int lastInfestRadius = -1;
	
	@Override
	public void updateEntity() {
		if(this.isMultiblockComplete()) {
			// Setups the bore
			if(this.state == -1) {
				this.state = 0;
				this.boreY = this.yCoord - this.structureHeight + 1;
				this.addChunksToQueue(8);
			}
			
			// Bores to a hole until it reaches maxBoreY
			if(this.state == 0 && this.worldObj.getWorldTime() % 1L == 0L) { // TODO: 40L
				if(--this.boreY < this.maxBoreY) {
					this.state = 1;
					return;
				}
				
				if(!this.worldObj.isRemote) {
					Block currentBlock = this.worldObj.getBlock(this.xCoord, this.boreY, this.zCoord);
					if(!currentBlock.getMaterial().isLiquid() && currentBlock.getBlockHardness(this.worldObj, this.xCoord, this.boreY, this.zCoord) > 0) {
						this.worldObj.func_147480_a(this.xCoord, this.boreY, this.zCoord, false);
					}
				}
			}
			
			// Scanning Chunks
			if(this.state == 1 && (this.chunkQueue.size() > 0 || this.currentScanningChunk != null)) {
				if(this.currentScanningChunk == null) {
					this.scanY = this.yCoord - 1;
					ChunkCoordIntPair coord = this.chunkQueue.get(0);
					this.currentScanningChunk = this.worldObj.getChunkFromChunkCoords(coord.chunkXPos, coord.chunkZPos);
					if(this.currentScanningChunk == null) return;
				}
				
				if(this.scanY <= 0) {
					this.chunkQueue.remove(0);
					this.currentScanningChunk = null;
					
					if(this.chunkQueue.size() == 0) {
						this.totalOres = this.oreBlocks.size();
						this.state = 2;
					}
				} else {
					for(int x = 0; x < 16; x++) {
						for(int z = 0; z < 16; z++) {
							Block block = this.currentScanningChunk.getBlock(x, this.scanY, z);
							if(block != null && block != Blocks.air && CoreRegistry.isOre(block)) {
								String uid = Util.createUID(this.worldObj.provider.dimensionId, x, this.scanY, z);
								if(!this.oreBlocks.contains(uid)) this.oreBlocks.add(uid);
							}
						}
					}
					this.scanY--;
				}
			}
			
			// Starts infesting the world and earning resources
			if(this.state == 2) {
				int r = (int)((double)this.oreBlocks.size() / (double)this.totalOres * (double)this.radius);
				int rSq = r * r;
				
				if(r != this.lastInfestRadius) {
					this.lastInfestRadius = r;
					
					// Infests the world
					for(int i = -r; i <= r; i++) {
						for(int j = -r; j <= r; j++) {
							int distance = i * i + j * j;
							if(distance <= rSq) {
								WorldUtil.setBiomeForCoords(this.worldObj, this.xCoord + i, this.zCoord + j, MineFracturing.INSTANCE.infestedBiome.biomeID);
							}
						}
					}
	
					// Update infested Chunks
					if(!this.worldObj.isRemote) {
						int chunkRadius = (int)(r / 16) + 1;
						for(int x = -chunkRadius; x <= chunkRadius; x++) {
							for(int z = -chunkRadius; z <= chunkRadius; z++) {
								Chunk chunk = this.worldObj.getChunkFromChunkCoords(this.xCoord >> 4 + x, this.zCoord >> 4 + z);
								this.worldObj.setBlock(chunk.xPosition * 16, 255, chunk.zPosition * 16, Blocks.bedrock);
								this.worldObj.setBlock(chunk.xPosition * 16, 255, chunk.zPosition * 16, Blocks.air);
							}
						}
					}
					
//					this.state = 3;
				}
			}
		}
	}
	
	public void addChunksToQueue(int radius) {
		this.radius = radius;
		int r = (int)(radius / 16) + 1;
		for(int x = -r; x <= r; x++) {
			for(int z = -r; z <= r; z++) {
				ChunkCoordIntPair coord = new ChunkCoordIntPair(this.xCoord >> 4 + x, this.zCoord >> 4 + z);
				if(!this.chunkQueue.contains(coord)) {
					this.chunkQueue.add(coord);
				}
			}
		}
	}
	
	public boolean isMultiblockComplete() {
		int i;
		for(i = 1; i <= this.structureHeight; i++) {
			int y = this.yCoord - i;
			if(y <= 0) break;
			if(this.worldObj.getBlock(this.xCoord, y, this.zCoord) != MineFracturing.INSTANCE.traverse) break;
		}
		return i == structureHeight;
	}
	
}
