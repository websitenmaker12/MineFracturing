package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import de.teamdna.core.chunkloading.ChunkLoaderEvent.ChunkLoaderInvalidateEvent;
import de.teamdna.core.chunkloading.ChunkLoaderEvent.ChunkLoaderValidateEvent;
import de.teamdna.core.chunkloading.IChunkLoader;
import de.teamdna.core.packethandling.Packets;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.api.CoreRegistry;
import de.teamdna.mf.block.BlockMaterialExtractor;
import de.teamdna.mf.block.IBoreBlock;
import de.teamdna.mf.packet.PacketBiomUpdate;
import de.teamdna.mf.util.PipeUtil;
import de.teamdna.util.CoreUtil;
import de.teamdna.util.StringUtil;
import de.teamdna.util.WorldBlock;

public class TileEntityBore extends TileEntityFluidCore implements ISidedInventory, IChunkLoader {

	public final int maxBoreY = 1;
	public final int structureHeight = 15;
	
	public int state = -1; // Inactive: -1 ; Boring: 0 ; Scanning Chunks: 1; Infesting: 2; Finished: 3
	public int boreY;
	public int radius;
	
	public List<String> oreBlocks = new ArrayList<String>();
	public int totalOres = 0;
	public List<ChunkCoordIntPair> chunkQueue = new ArrayList<ChunkCoordIntPair>();
	public int totalChunks = 0;
	public Chunk currentScanningChunk;
	private int scanY = 0;
	private int lastInfestRadius = -1;
	
	private ChunkCoordIntPair currentChunkForLoad = null;
	private boolean isFirstTick = true;
	
	public int burnTime = 0;
	public int maxBurnTime = 1;
	
	public TileEntityBore() {
		super(8);
		this.inventory = new ItemStack[1];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("burnTime", this.burnTime);
		tag.setInteger("maxBurnTime", this.maxBurnTime);
		tag.setInteger("state", this.state);
		tag.setInteger("boreY", this.boreY);
		tag.setInteger("radius", this.radius);
		
		NBTTagList ores = new NBTTagList();
		for(String s : this.oreBlocks) {
			NBTTagCompound oreTag = new NBTTagCompound();
			oreTag.setString("pos", s);
			ores.appendTag(oreTag);
		}
		tag.setTag("oreBlocks", ores);
		
		NBTTagList chunks = new NBTTagList();
		for(ChunkCoordIntPair pair : this.chunkQueue) {
			NBTTagCompound chunk = new NBTTagCompound();
			chunk.setInteger("x", pair.chunkXPos);
			chunk.setInteger("z", pair.chunkZPos);
			chunks.appendTag(chunk);
		}
		tag.setTag("chunkQueue", chunks);
		
		tag.setInteger("totalOres", this.totalOres);
		tag.setInteger("totalChunks", this.totalChunks);
		
		if(this.currentScanningChunk == null) tag.setString("currentChunk", "null");
		else tag.setString("currentChunk", this.currentScanningChunk.xPosition + "/" + this.currentScanningChunk.zPosition);
		
		tag.setInteger("scanY", this.scanY);
		tag.setInteger("lastInfestRadius", this.lastInfestRadius);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.burnTime = tag.getInteger("burnTime");
		this.maxBurnTime = tag.getInteger("maxBurnTime");
		this.state = tag.getInteger("state");
		this.boreY = tag.getInteger("boreY");
		this.radius = tag.getInteger("radius");
		
		this.oreBlocks.clear();
		NBTTagList ores = tag.getTagList("oreBlocks", 10);
		for(int i = 0; i < ores.tagCount(); i++) {
			this.oreBlocks.add(ores.getCompoundTagAt(i).getString("pos"));
		}
		
		this.chunkQueue.clear();
		NBTTagList chunks = tag.getTagList("chunkQueue", 10);
		for(int i = 0; i < chunks.tagCount(); i++) {
			NBTTagCompound chunkTag = chunks.getCompoundTagAt(i);
			this.chunkQueue.add(new ChunkCoordIntPair(chunkTag.getInteger("x"), chunkTag.getInteger("z")));
		}
		
		this.totalOres = tag.getInteger("totalOres");
		this.totalChunks = tag.getInteger("totalChunks");
		
		String currChunk = tag.getString("currentChunk");
		if(currChunk.equals("null") || currChunk.equals("")) this.currentChunkForLoad = null;
		else {
			String[] parts = currChunk.split("/");
			this.currentChunkForLoad = new ChunkCoordIntPair(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
		}
		
		this.scanY = tag.getInteger("scanY");
		this.lastInfestRadius = tag.getInteger("lastInfestRadius");
	}
	
	@Override
	public void updateEntity() {
		if(this.isFirstTick) {
			this.isFirstTick = false;
			if(this.currentChunkForLoad != null) {
				this.currentScanningChunk = this.worldObj.getChunkFromChunkCoords(this.currentChunkForLoad.chunkXPos, this.currentChunkForLoad.chunkZPos);
			}
		}
		
		if(this.inventory[0] != null && this.burnTime == 0 && CoreUtil.getFuelValue(this.inventory[0]) > 0) {
			this.burnTime = this.maxBurnTime = CoreUtil.getFuelValue(this.inventory[0]);
			Item container = this.inventory[0].getItem().getContainerItem();
			if(--this.inventory[0].stackSize == 0) this.inventory[0] = null;
			if(this.inventory[0] == null && container != null) this.inventory[0] = new ItemStack(container);
		}
		
		if(this.isMultiblockCompleted() && this.burnTime > 0 && this.tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME * 4) {
			if(this.state != 3) this.burnTime = Math.max(this.burnTime - 5, 0);
			
			// Setups the bore
			if(this.state == -1) {
				this.state = 0;
				this.boreY = this.yCoord - this.structureHeight + 1;
				this.addChunksToQueue(MineFracturing.boreRadius);
				if(!this.worldObj.isRemote) MinecraftForge.EVENT_BUS.post(new ChunkLoaderValidateEvent(this));
			}
			
			// Bores to a hole until it reaches maxBoreY
			if(this.state == 0 && this.worldObj.getWorldTime() % 10L == 0L) {
				if(--this.boreY < this.maxBoreY) {
					this.state = 1;
					return;
				}
				
				if(!this.worldObj.isRemote) {
					Block currentBlock = this.worldObj.getBlock(this.xCoord, this.boreY, this.zCoord);
					if(!currentBlock.getMaterial().isLiquid() && currentBlock.getBlockHardness(this.worldObj, this.xCoord, this.boreY, this.zCoord) > 0) {
						this.worldObj.func_147480_a(this.xCoord, this.boreY, this.zCoord, false);
						this.worldObj.spawnEntityInWorld(new EntityTNTPrimed(worldObj));
					}
				}
			}
			
			// Scanning Chunks
			if(this.state == 1 && (this.chunkQueue.size() > 0 || this.currentScanningChunk != null)) {
				if(this.currentScanningChunk == null) {
					this.scanY = this.yCoord - 1;
					ChunkCoordIntPair coord = this.chunkQueue.get(0);
					this.chunkQueue.remove(0);
					this.currentScanningChunk = this.worldObj.getChunkFromChunkCoords(coord.chunkXPos, coord.chunkZPos);
				}
				
				if(this.scanY <= 0) {
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
								String uid = StringUtil.createUID(this.worldObj.provider.dimensionId, this.currentScanningChunk.xPosition * 16 + x, this.scanY, this.currentScanningChunk.zPosition * 16 + z);
								if(!this.oreBlocks.contains(uid)) this.oreBlocks.add(uid);
							}
						}
					}
					this.scanY--;
				}
			}
			
			// Starts infesting the world and earning resources
			if(this.state == 2) {
				// Ore replacing
				if(!this.worldObj.isRemote) {
					if(this.oreBlocks.size() == 0) this.state = 3;
					else if(this.worldObj.getWorldTime() % 1L == 0L) {
						WorldBlock block = new WorldBlock(this.oreBlocks.get(0));
						this.oreBlocks.remove(0);
						TileEntityExtractor extractor = this.getFirstExtractor();
						extractor.addFluid(new FluidStack(MineFracturing.INSTANCE.oil, 5));
						extractor.addOre(block.getBlock());
						Block replace = CoreRegistry.getContainer(block.getBlock());
						if(replace != null) this.worldObj.setBlock(block.x, block.y, block.z, replace);
					}
				}
				
				// Infesting
				int m = MineFracturing.infestionMultiplier;
				int r = (this.radius * m - (int)((double)(this.oreBlocks.size()) / (double)this.totalOres * ((double)this.radius) * m));
				int rSq = r * r;
				
				if(r != this.lastInfestRadius) {
					this.drain(ForgeDirection.UNKNOWN, 1, !this.worldObj.isRemote);
					
					// Infests the world
					for(int i = -r; i <= r; i++) {
						for(int j = -r; j <= r; j++) {
							int distance = i * i + j * j;
							if(distance <= rSq && this.worldObj.getBiomeGenForCoords(this.xCoord + i, this.zCoord + j).biomeID != MineFracturing.INSTANCE.infestedBiome.biomeID) {
								Packets.toDimension(new PacketBiomUpdate(this.xCoord + i, this.zCoord + j, MineFracturing.INSTANCE.infestedBiome.biomeID), this.worldObj.provider.dimensionId);
								CoreUtil.setBiomeForCoords(this.worldObj, this.xCoord + i, this.zCoord + j, MineFracturing.INSTANCE.infestedBiome.biomeID);
							}
						}
					}
				}
			}
		}
	}
	
	public void addChunksToQueue(int radius) {
		this.radius = radius;
		int r = (int)(radius / 16) + 1;
		for(int x = -r; x <= r; x++) {
			for(int z = -r; z <= r; z++) {
				Chunk chunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
				ChunkCoordIntPair coord = new ChunkCoordIntPair(chunk.xPosition + x, chunk.zPosition + z);
				if(!this.chunkQueue.contains(coord)) {
					this.chunkQueue.add(coord);
				}
			}
		}
		
		this.totalChunks = this.chunkQueue.size();
	}
	
	public boolean isMultiblockCompleted() {
		int i;
		int extractors = 0;
		for(i = 1; i <= this.structureHeight; i++) {
			int y = this.yCoord - i;
			if(y <= 0) break;
			if(!(this.worldObj.getBlock(this.xCoord, y, this.zCoord) instanceof IBoreBlock)) break;
			if(this.worldObj.getBlock(this.xCoord, y, this.zCoord) instanceof BlockMaterialExtractor) extractors++;
		}
		return i == structureHeight && extractors > 0;
	}
	
	public TileEntityExtractor getFirstExtractor() {
		for(int y = this.yCoord - this.structureHeight - 1; y < this.yCoord; y++) {
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord, y, this.zCoord);
			if(tile != null && tile instanceof TileEntityExtractor) return (TileEntityExtractor)tile;
		}
		return null;
	}
	
	public int getScaledAnalysingProgress(int pixels) {
		if(this.totalChunks == 0) return 0;
		return pixels - (int)((double)this.chunkQueue.size() * (double)pixels / (double)this.totalChunks);
	}
	
	public int getScaledFracturingProgress(int pixels) {
		if(this.totalOres == 0) return 0;
		return pixels - (int)((double)this.oreBlocks.size() * (double)pixels / (double)this.totalOres);
	}
	
	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3) {
		return CoreUtil.getFuelValue(var2) > 0;
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3) {
		return false;
	}

	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet) {
		return PipeUtil.canImportToTank(packet, this.tank);
	}

	@Override
	public boolean canExtract(ForgeDirection direction) {
		return false;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public TileEntity getTile() {
		return this;
	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}

	@Override
	public List<ChunkCoordIntPair> getChunksToLoad() {
		List<ChunkCoordIntPair> chunks = new ArrayList<ChunkCoordIntPair>();
		
		if(!this.isMultiblockCompleted()) return chunks;
		int r = (int)(MineFracturing.boreRadius / 16) + 1;
		for(int x = -r; x <= r; x++) {
			for(int z = -r; z <= r; z++) {
				chunks.add(new ChunkCoordIntPair(x, z));
			}
		}
		return chunks;
	}
	
	@Override
	public void invalidate() {
		MinecraftForge.EVENT_BUS.post(new ChunkLoaderInvalidateEvent(this));
		super.invalidate();
	}
	
}
