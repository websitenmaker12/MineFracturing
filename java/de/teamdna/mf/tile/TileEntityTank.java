package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityTank extends TileEntityCore implements IExtractor, IImporter {

	public final int type;
	private boolean isConnected = false;
	private boolean needsUpdate = true;
	
	// Not for controller
	public TileEntityTank controllerTile;
	
	public TileEntityTank() {
		this.type = 1;
	}
	
	public TileEntityTank(int type) {
		this.type = type;
	}

	@Override
	public void updateEntity() {
		if(this.type == 1) {
			if(this.needsUpdate || !this.isConnected) {
				this.needsUpdate = false;
				this.isConnected = this.isStructureComplete();
				for(int x = -1; x <= 1; x++) {
					for(int y = -2; y <= 1; y++) {
						for(int z = -1; z <= 1; z++) {
							TileEntity tile = this.worldObj.getTileEntity(this.xCoord + x, this.yCoord + y, this.zCoord + z);
							if(tile != null && tile instanceof TileEntityTank && ((TileEntityTank)tile).type != 1) {
								((TileEntityTank)tile).controllerTile = this.isConnected ? this : null;
								((TileEntityTank)tile).isConnected = this.isConnected;
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
	}

	@Override
	public boolean canConnect(ForgeDirection direction) {
		return this.isConnected;
	}

	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet) {
		return false;
	}

	@Override
	public void doImport(ForgeDirection direction, NBTTagCompound packet) {
	}

	@Override
	public boolean canExtract(ForgeDirection direction) {
		return false;
	}

	@Override
	public NBTTagCompound extract(ForgeDirection direction) {
		return null;
	}
	
	public boolean isStructureComplete() {
		if(this.type != 1) return this.isConnected;
		else {
			int wallCount = 0;
			for(int x = -1; x <= 1; x++) {
				for(int y = -1; y <= 1; y++) {
					for(int z = -1; z <= 1; z++) {
						TileEntity tile = this.worldObj.getTileEntity(this.xCoord + x, this.yCoord + y, this.zCoord + z);
						if(tile != null && tile instanceof TileEntityTank && ((TileEntityTank)tile).type == 0) wallCount++;
					}
				}
			}
			
			boolean foundBases = true;
			TileEntity tile = this.worldObj.getTileEntity(this.xCoord - 1, this.yCoord - 2, this.zCoord - 1);
			if(!(tile != null && tile instanceof TileEntityTank && ((TileEntityTank)tile).type == 2)) foundBases = false;
			tile = this.worldObj.getTileEntity(this.xCoord + 1, this.yCoord - 2, this.zCoord - 1);
			if(!(tile != null && tile instanceof TileEntityTank && ((TileEntityTank)tile).type == 2)) foundBases = false;
			tile = this.worldObj.getTileEntity(this.xCoord - 1, this.yCoord - 2, this.zCoord + 1);
			if(!(tile != null && tile instanceof TileEntityTank && ((TileEntityTank)tile).type == 2)) foundBases = false;
			tile = this.worldObj.getTileEntity(this.xCoord + 1, this.yCoord - 2, this.zCoord + 1);
			if(!(tile != null && tile instanceof TileEntityTank && ((TileEntityTank)tile).type == 2)) foundBases = false;
			return wallCount == 3 * 3 * 3 - 1 && foundBases;
		}
	}
	
	public void neighborsHadChanged() {
		if(this.type == 1) this.needsUpdate = true;
		else if(this.controllerTile != null) this.controllerTile.neighborsHadChanged();
	}
	
}
