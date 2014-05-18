package de.teamdna.mf.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import de.teamdna.mf.block.BlockTank;
import de.teamdna.mf.util.PipeUtil;
import de.teamdna.mf.util.Util;

public class TileEntityTank extends TileEntityCore implements IExtractor, IImporter, IFluidHandler {

	// TODO: Inventory doesn't get saved....
	
	public final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 750);
	
	public int type;
	private boolean isConnected = false;
	private boolean needsUpdate = true;
	private boolean isFirstTick = true;
	
	public TileEntityTank controllerTile;

	@Override
	public void updateEntity() {
		if(this.isFirstTick) {
			this.isFirstTick = false;
			this.type = ((BlockTank)this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord)).type;
			this.inventory = new ItemStack[2];
		}
		
		if(this.type == 1) {
			if(this.needsUpdate || !this.isConnected) {
				this.needsUpdate = false;
				this.isConnected = this.isStructureComplete();
				for(int x = -1; x <= 1; x++) {
					for(int y = -1; y <= 2; y++) {
						for(int z = -1; z <= 1; z++) {
							TileEntity tile = this.worldObj.getTileEntity(this.xCoord + x, this.yCoord + y, this.zCoord + z);
							if(tile != null && tile instanceof TileEntityTank) {
								((TileEntityTank)tile).controllerTile = this;
								((TileEntityTank)tile).isConnected = this.isConnected;
							}
						}
					}
				}
			}
			
			if(!this.worldObj.isRemote && this.isConnected && this.inventory[0] != null && this.controllerTile != null) {
				if(FluidContainerRegistry.isFilledContainer(this.inventory[0])) {
					FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(this.inventory[0]);
					if((this.tank.getFluid() == null || this.tank.getFluid().isFluidEqual(fluid)) && this.tank.getFluidAmount() + fluid.amount <= this.tank.getCapacity()) {
						ItemStack output = Util.getEmptyForFilledContainer(this.inventory[0]);
						if(this.inventory[1] == null || (this.inventory[1].stackSize + 1 <= this.inventory[1].getMaxStackSize() && this.inventory[1].isItemEqual(output))) {
							this.fill(ForgeDirection.UNKNOWN, fluid, true);
							if(--this.inventory[0].stackSize == 0) this.inventory[0] = null;
							if(this.inventory[1] == null) this.inventory[1] = new ItemStack(output.getItem());
							else this.inventory[1].stackSize++;
						}
					}
				} else if(FluidContainerRegistry.isEmptyContainer(this.inventory[0])) {
					if(this.tank.getFluid() != null && this.tank.getFluidAmount() - 1000 >= 0) {
						ItemStack output = Util.getFilledForEmptyContainer(this.inventory[0], this.tank.getFluid());
						if(output != null && (this.inventory[1] == null || (this.inventory[1].stackSize + 1 <= this.inventory[1].getMaxStackSize() && this.inventory[1].isItemEqual(output)))) {
							this.drain(ForgeDirection.UNKNOWN, 1000, true);
							if(--this.inventory[0].stackSize == 0) this.inventory[0] = null;
							if(this.inventory[1] == null) this.inventory[1] = new ItemStack(output.getItem());
							else this.inventory[1].stackSize++;
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
		return direction != ForgeDirection.DOWN && PipeUtil.canImportToTank(packet, this.controllerTile.tank);
	}

	@Override
	public void doImport(ForgeDirection direction, NBTTagCompound packet) {
		PipeUtil.importToTank(packet, this.controllerTile.tank);
	}

	/* IExtractor */
	@Override
	public boolean canExtract(ForgeDirection direction) {
		return direction == ForgeDirection.DOWN;
	}

	@Override
	public NBTTagCompound extract(ForgeDirection direction) {
		return PipeUtil.extractFromTank(this.controllerTile.tank);
	}
	
	public boolean isStructureComplete() {
		if(this.type != 1) return this.isConnected;
		else {
			int wallCount = 0;
			for(int x = -1; x <= 1; x++) {
				for(int y = -1; y <= 2; y++) {
					for(int z = -1; z <= 1; z++) {
						TileEntity tile = this.worldObj.getTileEntity(this.xCoord + x, this.yCoord + y, this.zCoord + z);
						if(tile != null && tile instanceof TileEntityTank && ((TileEntityTank)tile).type == 0) wallCount++;
					}
				}
			}

			return wallCount == 3 * 4 * 3 - 1;
		}
	}
	
	public void neighborsHadChanged() {
		if(this.type == 1) this.needsUpdate = true;
		else if(this.controllerTile != null) this.controllerTile.neighborsHadChanged();
	}

	/* IFluidHandler */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return this.controllerTile.tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if(resource == null || !resource.isFluidEqual(this.controllerTile.tank.getFluid())) return null;
		return this.controllerTile.tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.controllerTile.tank.drain(maxDrain, doDrain);
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
		return new FluidTankInfo[] { this.controllerTile.tank.getInfo() };
	}
	
}
