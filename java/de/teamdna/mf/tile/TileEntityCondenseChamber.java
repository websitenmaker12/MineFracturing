package de.teamdna.mf.tile;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.Reference;

public class TileEntityCondenseChamber extends TileEntityFluidCore implements ISidedInventory {

	private int currentBlockID = -1;
	public int currentBlockAmount = 0;
	
	// TODO: save stuff
	
	public TileEntityCondenseChamber() {
		super(80);
		this.inventory = new ItemStack[9];
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
