package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import de.teamdna.mf.util.PipeUtil;

public abstract class TileEntityFluidCore extends TileEntityCore implements IExtractor, IImporter, IFluidHandler {

	public final FluidTank tank;
	
	public TileEntityFluidCore(int buckets) {
		this.tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * buckets);
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
	
	@Override
	public boolean canConnect(ForgeDirection direction) {
		return true;
	}
	
	/* IImporter */
	@Override
	public abstract boolean canImport(ForgeDirection direction, NBTTagCompound packet);

	@Override
	public void doImport(ForgeDirection direction, NBTTagCompound packet) {
		PipeUtil.importToTank(packet, this.tank);
	}

	/* IExtractor */
	@Override
	public abstract boolean canExtract(ForgeDirection direction);

	@Override
	public NBTTagCompound extract(ForgeDirection direction, boolean doExtract) {
		return PipeUtil.extractFromTank(this.tank, doExtract);
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
	public abstract boolean canFill(ForgeDirection from, Fluid fluid);

	@Override
	public abstract boolean canDrain(ForgeDirection from, Fluid fluid);

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.tank.getInfo() };
	}

}
