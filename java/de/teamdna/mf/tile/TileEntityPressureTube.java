package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityPressureTube extends TileEntityCore implements IConnectable {
	
	public static final int maxPacketStorage = 10;
	
	private List<NBTTagCompound> packets = new ArrayList<NBTTagCompound>();
	private List<ForgeDirection> adjacentExtractors = new ArrayList<ForgeDirection>();
	private List<ForgeDirection> adjacentImporters = new ArrayList<ForgeDirection>();
	private List<ForgeDirection> adjacentPipes = new ArrayList<ForgeDirection>();
	private Map<NBTTagCompound, ForgeDirection> pathTracker = new HashMap<NBTTagCompound, ForgeDirection>();
	
	private boolean needsUpdate = true;
	
	@Override
	public void updateEntity() {
		if(this.worldObj.isRemote) return;
		
		// Checking incoming packets
		if(this.canReceivePackets()) {
			for(ForgeDirection dir : this.adjacentExtractors) {
				IExtractor extractor = this.getByDirection(dir);
				if(extractor != null && extractor.canExtract(dir)) {
					NBTTagCompound packet = extractor.extract(dir);
					if(packet != null) this.packets.add(packet);
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
		if(this.adjacentPipes.size() > 0 && this.packets.size() > 0) {
			ForgeDirection dir = this.adjacentPipes.get(this.worldObj.rand.nextInt(this.adjacentPipes.size()));
			TileEntityPressureTube tile = this.getByDirection(dir);
			NBTTagCompound packet = this.packets.get(0);
			
			if(tile != null && packet != null && (dir != this.pathTracker.get(packet) || this.adjacentPipes.size() == 1)) {
				this.sendPacket(packet, dir);
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
	
	public boolean isConnectedToSide(ForgeDirection direction) {
		if(!this.hasWorldObj()) return false;
		TileEntity tile = this.worldObj.getTileEntity(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
		return tile != null && tile instanceof IConnectable && ((IConnectable)tile).canConnect(direction.getOpposite());
	}
	
	public boolean sendPacket(NBTTagCompound packet, ForgeDirection direction) {
		if(this.isConnectedToPipe(direction)) {
			TileEntityPressureTube tile = this.getByDirection(direction);
			if(tile.canReceivePackets()) {
				tile.receivePacket(packet, direction.getOpposite());
				this.packets.remove(packet);
				this.pathTracker.remove(packet);
				return true;
			}
		}
		return false;
	}
	
	public boolean canReceivePackets() {
		return this.packets.size() < maxPacketStorage;
	}
	
	public void receivePacket(NBTTagCompound packet, ForgeDirection direction) {
		this.packets.add(packet);
		this.pathTracker.put(packet, direction);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		// TODO: error by saving
		NBTTagList packetList = new NBTTagList();
		for(NBTTagCompound packet : this.packets) packetList.appendTag(packet);
		tag.setTag("packets", packetList);
		
		NBTTagList pathTrackMap = new NBTTagList();
		for(Map.Entry<NBTTagCompound, ForgeDirection> entry : this.pathTracker.entrySet()) {
			NBTTagCompound path = new NBTTagCompound();
			path.setTag("packet", entry.getKey());
			path.setInteger("direction", entry.getValue().ordinal());
		}
		tag.setTag("pathTracker", pathTrackMap);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		this.packets.clear();
		NBTTagList packetList = tag.getTagList("packets", 10);
		for(int i = 0; i < packetList.tagCount(); i++) this.packets.add(packetList.getCompoundTagAt(i));
		
		this.pathTracker.clear();
		NBTTagList pathTrackMap = tag.getTagList("pathTracker", 10);
		for(int i = 0; i < pathTrackMap.tagCount(); i++) {
			NBTTagCompound entry = pathTrackMap.getCompoundTagAt(i);
			this.pathTracker.put(entry.getCompoundTag("packet"), ForgeDirection.values()[entry.getInteger("direction")]);
		}
	}

	@Override
	public boolean canConnect(ForgeDirection direction) {
		return true;
	}
	
}
