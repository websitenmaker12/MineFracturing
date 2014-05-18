package de.teamdna.mf.tile;

import de.teamdna.mf.util.PipeUtil;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

public class TileEntityCondenseChamber extends TileEntityFluidCore implements ISidedInventory {

	public TileEntityCondenseChamber() {
		super(80);
		this.inventory = new ItemStack[9];
	}

	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet) {
		return PipeUtil.canImportToTank(packet, this.tank);
	}

	@Override
	public boolean canExtract(ForgeDirection direction) {
		return false;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
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
