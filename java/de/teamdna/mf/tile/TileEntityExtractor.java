package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import de.teamdna.mf.Reference;

public class TileEntityExtractor extends TileEntityCore implements IExtractor {

	private List<FluidStack> fluidsToSend = new ArrayList<FluidStack>();
	private List<Block> blocksToSend = new ArrayList<Block>();
	
	@Override
	public boolean canExtract(ForgeDirection direction) {
		return true;
	}

	@Override
	public NBTTagCompound extract(ForgeDirection direction, boolean doExtract) {
		int i = this.worldObj.rand.nextInt(2);
		if(i < 1 && this.fluidsToSend.size() > 0) {
			FluidStack stack = this.fluidsToSend.get(0);
			if(doExtract) this.fluidsToSend.remove(0);
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
				if(doExtract) this.blocksToSend.remove(0);
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
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		
		NBTTagList fluids = new NBTTagList();
		for(FluidStack fluid : this.fluidsToSend) {
			NBTTagCompound fluidTag = new NBTTagCompound();
			fluid.writeToNBT(fluidTag);
			fluids.appendTag(fluidTag);
		}
		tag.setTag("fluids", fluids);
		
		NBTTagList blocks = new NBTTagList();
		for(Block block : this.blocksToSend) {
			NBTTagCompound blockTag = new NBTTagCompound();
			blockTag.setInteger("blockID", Block.getIdFromBlock(block));
			blocks.appendTag(blockTag);
		}
		tag.setTag("blocks", blocks);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		this.fluidsToSend.clear();
		NBTTagList fluids = tag.getTagList("fluids", 10);
		for(int i = 0; i < fluids.tagCount(); i++) this.fluidsToSend.add(FluidStack.loadFluidStackFromNBT(fluids.getCompoundTagAt(i)));
		
		this.blocksToSend.clear();
		NBTTagList blocks = tag.getTagList("blocks", 10);
		for(int i = 0; i < blocks.tagCount(); i++) this.blocksToSend.add(Block.getBlockById(blocks.getCompoundTagAt(i).getInteger("blockID")));
	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}

	@Override
	public int getX() {
		return this.xCoord;
	}

	@Override
	public int getY() {
		return this.yCoord;
	}

	@Override
	public int getZ() {
		return this.zCoord;
	}
	
}
