package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityFluidCore extends TileEntityCore implements IExtractor, IImporter, IFluidHandler{

	public final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 4);
	
	@Override
	public boolean canConnect(ForgeDirection direction) {
		return true;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return null;
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
}
