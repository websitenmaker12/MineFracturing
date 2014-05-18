package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import de.teamdna.mf.Reference;

public class TileEntityExtractor extends TileEntityCore implements IExtractor {

	private List<FluidStack> fluidsToSend = new ArrayList<FluidStack>();
	private List<Block> blocksToSend = new ArrayList<Block>();
	
	// TODO: Save stacks
	
	@Override
	public boolean canExtract(ForgeDirection direction) {
		return true;
	}

	@Override
	public NBTTagCompound extract(ForgeDirection direction) {
		int i = this.worldObj.rand.nextInt(2);
		
		if(i < 0 && this.fluidsToSend.size() > 0) {
			FluidStack stack = this.fluidsToSend.get(0);
			this.fluidsToSend.remove(0);
			if(stack != null && stack.amount > 0) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("id", Reference.PipePacketIDs.fluid);
				tag.setInteger("amount", stack.amount);
				tag.setInteger("fluidID", stack.fluidID);
				if(stack.tag != null) tag.setTag("stackTag", stack.tag);
				return tag;
			}
		} else {
			if(this.blocksToSend.size() > 0) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("id", Reference.PipePacketIDs.block);
				tag.setInteger("blockID", Block.getIdFromBlock(this.blocksToSend.get(0)));
				this.blocksToSend.remove(0);
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

	public void addOre(Block block) {
		this.blocksToSend.add(block);
	}
	
}
