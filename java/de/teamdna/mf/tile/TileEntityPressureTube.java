package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityPressureTube extends TileEntity {
	
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
		if(this.canReceivePackets()) {
			for(ForgeDirection dir : this.adjacentExtractors) {
				IExtractor extractor = this.getByDirection(dir);
				if(extractor != null && extractor.canExtract(dir)) {
					this.packets.add(extractor.extract(dir));
				}
			}
		}

		// Export packets
		if(this.adjacentImporters.size() > 0 && this.packets.size() > 0) {
			ForgeDirection dir = this.adjacentImporters.get(this.worldObj.rand.nextInt(this.adjacentImporters.size()));
			IImporter importer = this.getByDirection(dir);
			if(importer != null) {
				List<NBTTagCompound> toRemove = new ArrayList<NBTTagCompound>();
				for(NBTTagCompound packet : this.packets) {
					if(importer.canImport(dir, packet)) {
						importer.doImport(dir, packet);
						toRemove.add(packet);
						break;
					}
				}
				for(NBTTagCompound packet : toRemove) this.packets.remove(packet);
				toRemove.clear();
			}
		}
		
		// Transfer packets
		if(this.adjacentPipes.size() > 0) {
			// TODO: avoid that packet can be send to the pipe it is coming from except there is no other pipe to send
			ForgeDirection dir = this.adjacentPipes.get(this.worldObj.rand.nextInt(this.adjacentPipes.size()));
			TileEntityPressureTube tile = this.getByDirection(dir);
			if(tile != null && this.packets.size() > 0) {
				this.sendPacket(this.packets.get(0), dir);
			}
		}
		
		if(this.needsUpdate) {
			this.adjacentExtractors = this.getAdjacentExtractors();
			this.adjacentImporters = this.getAdjacentImporters();
			this.adjacentPipes = this.getAdjacentPipes();
			this.needsUpdate = false;
		}
	}
	
	public List<ForgeDirection> getAdjacentExtractors() {
		List<ForgeDirection> output = new ArrayList<ForgeDirection>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
			if(tile != null && tile instanceof IExtractor) output.add(dir);
		}
		return output;
	}
	
	public List<ForgeDirection> getAdjacentImporters() {
		List<ForgeDirection> output = new ArrayList<ForgeDirection>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
			if(tile != null && tile instanceof IImporter) output.add(dir);
		}
		return output;
	}
	
	public List<ForgeDirection> getAdjacentPipes() {
		List<ForgeDirection> output = new ArrayList<ForgeDirection>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
			if(tile != null && tile instanceof TileEntityPressureTube) output.add(dir);
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
	
	public boolean isConnectedToPipe(ForgeDirection direction) {
		return this.adjacentPipes.contains(direction);
	}
	
	@SideOnly(Side.CLIENT)
	public void updatePipeConnections() {
		this.adjacentPipes = this.getAdjacentPipes();
	}
	
	public boolean sendPacket(NBTTagCompound packet, ForgeDirection direction) {
		if(this.isConnectedToPipe(direction)) {
			TileEntityPressureTube tile = this.getByDirection(direction);
			if(tile.canReceivePackets()) {
				tile.receivePacket(packet);
				this.packets.remove(packet);
				return true;
			}
		}
		return false;
	}
	
	public boolean canReceivePackets() {
		return this.packets.size() < maxPacketStorage;
	}
	
	public void receivePacket(NBTTagCompound packet) {
		this.packets.add(packet);
	}
	
}
