package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import de.teamdna.mf.Reference;

public class TileEntityTank extends TileEntityCore implements IExtractor, IImporter, IFluidHandler {

	public final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 750);
	
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
		this.tank.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.tank.readFromNBT(tag);
	}

	/* IConnectable */
	@Override
	public boolean canConnect(ForgeDirection direction) {
		return this.isConnected;
	}

	/* IImporter */
	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet) {
		return direction != ForgeDirection.DOWN
				&& packet.getInteger("id") == Reference.PipePacketIDs.fluid
				&& FluidStack.loadFluidStackFromNBT(packet.getCompoundTag("stackTag")).isFluidEqual(this.tank.getFluid())
				&& this.tank.getFluidAmount() < this.tank.getInfo().capacity;
	}

	@Override
	public void doImport(ForgeDirection direction, NBTTagCompound packet) {
		this.tank.fill(FluidStack.loadFluidStackFromNBT(packet.getCompoundTag("stackTag")), true);
	}

	/* IExtractor */
	@Override
	public boolean canExtract(ForgeDirection direction) {
		return direction == ForgeDirection.DOWN;
	}

	@Override
	public NBTTagCompound extract(ForgeDirection direction) {
		FluidStack stack = this.tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
		if(stack.amount > 0) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("id", Reference.PipePacketIDs.fluid);
			tag.setLong("timestamp", System.currentTimeMillis());
			tag.setInteger("amount", stack.amount);
			tag.setInteger("fluidID", stack.fluidID);
			tag.setTag("stackTag", stack.tag);
			return tag;
		}
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

	/* IFluidHandler */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return this.tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if(resource == null || !resource.isFluidEqual(this.tank.getFluid())) return null;
		return this.tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return this.isConnected && from != ForgeDirection.DOWN;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return this.isConnected && from == ForgeDirection.DOWN;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.tank.getInfo() };
	}
	
}
