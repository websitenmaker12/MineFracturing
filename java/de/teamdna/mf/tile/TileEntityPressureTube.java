package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityPressureTube extends TileEntity implements IPipe {
	
	public static final int maxPacketStorage = 10;
	private List<NBTTagCompound> packets = new ArrayList<NBTTagCompound>();
	
	private List<ForgeDirection> adjacentExtractors = new ArrayList<ForgeDirection>();
	private List<ForgeDirection> adjacentImporters = new ArrayList<ForgeDirection>();
	private List<ForgeDirection> adjacentPipes = new ArrayList<ForgeDirection>();
	
	private boolean needsUpdate = true;
	
	@Override
	public void updateEntity() {
		if(this.worldObj.isRemote) return;
		
		// Checking incoming packets
		if(this.packets.size() < maxPacketStorage) {
		}

		// Export packets
		if(this.adjacentImporters.size() > 0) {
			IImporter importer = this.getByDirection(this.adjacentImporters.get(this.worldObj.rand.nextInt(this.adjacentImporters.size())));
		}
		
		// Transfer packets
		if(this.adjacentPipes.size() > 0) {
		}
		
		if(this.needsUpdate) {
			this.adjacentExtractors = this.getAdjacentExtractors();
			this.adjacentImporters = this.getAdjacentImporters();
			this.adjacentPipes = this.getAdjacentPipes();
			this.needsUpdate = false;
		}
	}
	
	public List<ForgeDirection> getAdjacentExtractors() {
		return this.getAdjacents(IExtractor.class);
	}
	
	public List<ForgeDirection> getAdjacentImporters() {
		return this.getAdjacents(IImporter.class);
	}
	
	public List<ForgeDirection> getAdjacentPipes() {
		return this.getAdjacents(IPipe.class);
	}
	
	private List<ForgeDirection> getAdjacents(Class type) {
		List<ForgeDirection> output = new ArrayList<ForgeDirection>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
			if(tile != null && tile.getClass().isInstance(type)) output.add(dir);
		}
		return output;
	}
	
	public void neighborsHadChanged() {
		this.needsUpdate = true;
	}
	
	private <V> V getByDirection(ForgeDirection dir) {
		TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
		if(tile != null) return (V)tile;
		else return null;
	}
	
}
