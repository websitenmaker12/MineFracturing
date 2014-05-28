package de.teamdna.mf.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import de.teamdna.mf.Reference;

public class PipeUtil {

	public static NBTTagCompound extractFromTank(FluidTank tank, boolean doExtract) {
		FluidStack stack = tank.drain(FluidContainerRegistry.BUCKET_VOLUME, doExtract);
		if(stack != null && stack.amount > 0) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("id", Reference.PipePacketIDs.fluid);
			tag.setInteger("amount", stack.amount);
			tag.setInteger("fluidID", stack.fluidID);
			if(stack.tag != null) tag.setTag("stackTag", stack.tag);
			return tag;
		}
		return null;
	}
	
	public static boolean canImportToTank(NBTTagCompound packet, FluidTank tank) {
		return packet.getInteger("id") == Reference.PipePacketIDs.fluid
				&& (tank.getFluid() == null || tank.getFluid().fluidID == packet.getInteger("fluidID"))
				&& tank.getFluidAmount() + packet.getInteger("amount") <= tank.getInfo().capacity;
	}
	
	public static void importToTank(NBTTagCompound packet, FluidTank tank) {
		if(packet.hasKey("stackTag") && packet.getCompoundTag("stackTag") != null) tank.fill(FluidStack.loadFluidStackFromNBT(packet.getCompoundTag("stackTag")), true);
		else tank.fill(new FluidStack(FluidRegistry.getFluid(packet.getInteger("fluidID")), packet.getInteger("amount")), true);
	}
	
}
