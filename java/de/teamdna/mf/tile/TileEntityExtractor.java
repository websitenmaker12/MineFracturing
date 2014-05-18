package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import de.teamdna.mf.Reference;

public class TileEntityExtractor extends TileEntityCore implements IExtractor {

	private List<FluidStack> fluidsToSend = new ArrayList<FluidStack>();
	
	@Override
	public boolean canExtract(ForgeDirection direction) {
		return true;
	}

	@Override
	public NBTTagCompound extract(ForgeDirection direction) {
		if(this.fluidsToSend.size() > 0) {
			FluidStack stack = this.fluidsToSend.get(0);
			this.fluidsToSend.remove(0);
			if(stack != null && stack.amount > 0) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("id", Reference.PipePacketIDs.fluid);
				tag.setInteger("amount", stack.amount);
				tag.setInteger("fluidID", stack.fluidID);
				tag.setTag("stackTag", stack.tag);
				return tag;
			}
		}
		return null;
	}

	@Override
	public boolean canConnect(ForgeDirection direction) {
		return true;
	}
	
	public void addFluid(FluidStack stack) {
		this.fluidsToSend.add(stack);
	}

}
