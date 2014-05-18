package de.teamdna.mf.tile;

import net.minecraft.block.Block;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.Reference;
import de.teamdna.mf.api.CoreRegistry;

public class TileEntityCondenseChamber extends TileEntityFluidCore implements ISidedInventory {

	public static final int maxIdle = 200;
	
	private int currentBlockID = -1;
	public int currentBlockAmount = 0;
	
	public int idle = 0;
	
	public TileEntityCondenseChamber() {
		super(80);
		this.inventory = new ItemStack[9];
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if(!this.worldObj.isRemote) {
			if(this.currentBlockID != -1 && this.currentBlockAmount > 0) {
				if(this.isEnoughSpace()) {
					if(++this.idle >= maxIdle) {
						this.currentBlockAmount--;
						int amount = this.worldObj.rand.nextInt(7) + 4;
						ItemStack stack = new ItemStack(CoreRegistry.getCondensedItem(Block.getBlockById(this.currentBlockID)), amount);
						this.mergeStackToOutput(stack);
						this.tank.drain(1000, true);
					}
				}
			} else {
				this.currentBlockID = -1;
				this.currentBlockAmount = 0;
			}
		}
	}
	
	private void mergeStackToOutput(ItemStack stack) {
		for(int i = 0; i < 9; i++) {
			if(stack == null) break;
			if(this.inventory[i] == null) this.inventory[i] = stack;
			else if(this.inventory[i].stackSize < this.inventory[i].getMaxDamage() && this.inventory[i].isItemEqual(stack)) {
				int newSize = this.inventory[i].stackSize + stack.stackSize;
				if(newSize <= this.inventory[i].getMaxStackSize()) {
					this.inventory[i].stackSize = newSize;
					stack = null;
				} else {
					this.inventory[i].stackSize = this.inventory[i].getMaxStackSize();
					stack.stackSize = newSize - this.inventory[i].getMaxStackSize();
				}
			}
		}
	}
	
	private boolean isEnoughSpace() {
		for(int i = 0; i < 9; i++) if(this.inventory[i] == null) return true;
		for(int i = 0; i < 9; i++) if(this.inventory[i] != null && this.inventory[i].stackSize < this.inventory[1].getMaxStackSize()) return true;
		return false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("blockID", this.currentBlockID);
		tag.setInteger("amount", this.currentBlockAmount);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.currentBlockID = tag.getInteger("blockID");
		this.currentBlockAmount = tag.getInteger("amount");
	}
	
	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet) {
		return packet.getInteger("id") == Reference.PipePacketIDs.block
				&& (this.currentBlockID == -1 || this.currentBlockID == packet.getInteger("blockID"))
				&& this.tank.getFluidAmount() < this.tank.getInfo().capacity;
	}

	@Override
	public void doImport(ForgeDirection direction, NBTTagCompound packet) {
		this.tank.fill(new FluidStack(MineFracturing.INSTANCE.liquidOre, 1000), true);
		this.currentBlockID = packet.getInteger("blockID");
		this.currentBlockAmount++;
	}
	
	@Override
	public boolean canExtract(ForgeDirection direction) {
		return false;
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
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3) {
		return false;
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3) {
		return true;
	}

}
