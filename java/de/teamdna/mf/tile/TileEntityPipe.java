package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import de.teamdna.mf.api.PipeRegistry;
import de.teamdna.mf.util.Util;
import de.teamdna.mf.util.WorldBlock;

public class TileEntityPipe extends TileEntityCore implements IConnectable {
	
	private PipeNetworkController network = PipeNetworkController.INSTNACE;
	private List<String> blocks = new ArrayList<String>();
	private List<String> checkedBlocks = new ArrayList<String>();
	private List<ForgeDirection> adjacentExtractors = new ArrayList<ForgeDirection>();
	private List<ForgeDirection> adjacentCustoms = new ArrayList<ForgeDirection>();
	
	public long networkID = -1L;
	private boolean needsUpdate = true;
	
	@Override
	public void updateEntity() {
		if(this.worldObj.isRemote) return;
		if(this.networkID != -1L && !this.network.doesNetworkExists(this.networkID)) this.networkID = -1L;
		
		// Triggers Network creation
		if(this.networkID == -1L && !this.network.isScanning) {
			this.networkID = this.network.createNetwork();
			this.addNeighbors(Util.createUID(this.xCoord, this.yCoord, this.zCoord));
			this.network.isScanning = this.blocks.size() > 0;
		}
		
		// Handles queued blocks
		if(this.blocks.size() > 0) {
			String uid = this.blocks.get(0);
			this.blocks.remove(0);
			this.addNeighbors(uid);
			WorldBlock block = new WorldBlock(this.worldObj, uid);
			TileEntityPipe pipe = (TileEntityPipe)block.tile;
			
			if(pipe != null && !pipe.isInvalid()) {
				pipe.networkID = this.networkID;
				this.network.addPipe(pipe, this.networkID);
			}

			if(this.blocks.size() == 0) this.network.isScanning = false;
		}
		
		// Handles incoming packets
		for(ForgeDirection dir : this.adjacentExtractors) {
			IExtractor extractor = this.getByDirection(dir);
			if(extractor != null && extractor.canExtract(dir.getOpposite())) {
				NBTTagCompound packet = extractor.extract(dir.getOpposite(), false);
				if(packet == null) continue;
				if(this.network.canNetworkProcessPacket(this.networkID, packet, dir, extractor)) {
					extractor.extract(dir.getOpposite(), true);
					this.network.processPacket(this.networkID, packet, dir, extractor);
				}
			}
		}
		
		if(this.needsUpdate) {
			this.adjacentExtractors = this.getAdjacentExtractors();
			this.needsUpdate = false;
		}
	}

	private void addNeighbors(String uid) {
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			WorldBlock block = new WorldBlock(this.worldObj, uid);
			block.x += dir.offsetX;
			block.y += dir.offsetY;
			block.z += dir.offsetZ;
			if(block.hasTile() && block.tile instanceof TileEntityPipe) {
				String id = Util.createUID(block.x, block.y, block.z);
				if(!this.checkedBlocks.contains(id)) {
					this.blocks.add(id);
					this.checkedBlocks.add(id);
				}
			}
		}
	}
	
	private List<ForgeDirection> getAdjacentExtractors() {
		List<ForgeDirection> output = new ArrayList<ForgeDirection>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
			if(tile != null && tile instanceof IExtractor) output.add(dir);
		}
		return output;
	}
	
	public void neighborsHadChanged() {
		this.needsUpdate = true;
		
		List<ForgeDirection> copy = new ArrayList<ForgeDirection>(this.adjacentCustoms);
		for(ForgeDirection dir : copy) {
			if(this.getByDirection(dir) == null) this.adjacentCustoms.remove(dir);
		}
		
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
			if(tile != null && PipeRegistry.isCustomTile(tile.getClass()) && !this.adjacentCustoms.contains(dir) && !tile.isInvalid()) {
				try {
					this.network.handleBlockAdd(tile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.adjacentCustoms.add(dir);
			}
		}
	}
	
	private <V> V getByDirection(ForgeDirection dir) {
		TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
		if(tile != null) return (V)tile;
		else return null;
	}
	
	@Override
	public boolean canConnect(ForgeDirection direction) {
		return true;
	}

	public boolean isConnectedToSide(ForgeDirection dir) {
		if(this.worldObj == null) return dir == ForgeDirection.UP || dir == ForgeDirection.DOWN;
		TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
		if(tile == null) return false;
		return (tile instanceof IConnectable && ((IConnectable)tile).canConnect(dir)) || PipeRegistry.isCustomTile(tile.getClass());
	}
	
}
