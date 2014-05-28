package de.teamdna.mf.tile;

import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.util.Util;

public class TileEntityChemicalsMixer extends TileEntityFluidCore implements ISidedInventory {
	
	public static int maxIdle = 200;
	public int idle = 0;
	public int burnTime = 0;
	public int maxBurnTime = 1;
	
	public TileEntityChemicalsMixer() {
		super(6);
		this.inventory = new ItemStack[4];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("burnTime", this.burnTime);
		tag.setInteger("maxBurnTime", this.maxBurnTime);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.burnTime = tag.getInteger("burnTime");
		this.maxBurnTime = tag.getInteger("maxBurnTime");
	}
	
	@Override
	public void updateEntity() {
		if(this.worldObj.isRemote) return;
		
		if(this.inventory[3] != null && this.burnTime == 0 && Util.getFuelValue(this.inventory[3]) > 0) {
			this.burnTime = this.maxBurnTime = Util.getFuelValue(this.inventory[3]);
			Item container = this.inventory[3].getItem().getContainerItem();
			if(--this.inventory[3].stackSize == 0) this.inventory[3] = null;
			if(this.inventory[3] == null && container != null) this.inventory[3] = new ItemStack(container);
		}
		
		if(this.burnTime > 0 && this.inventory[0] != null && this.inventory[1] != null && this.inventory[2] != null && this.tank.getFluidAmount() + 3000 <= this.tank.getCapacity()) {
			boolean flag1 = this.inventory[0].getItem() == Items.blaze_powder || this.inventory[1].getItem() == Items.blaze_powder || this.inventory[2].getItem() == Items.blaze_powder;
			boolean flag2 = this.inventory[0].getItem() == Items.redstone || this.inventory[1].getItem() == Items.redstone || this.inventory[2].getItem() == Items.redstone;
			boolean flag3 = this.inventory[0].getItem() == Items.slime_ball || this.inventory[1].getItem() == Items.slime_ball || this.inventory[2].getItem() == Items.slime_ball;
			
			if(flag1 && flag2 && flag3) {
				if(++this.idle >= maxIdle) {
					this.burnTime -= maxIdle;
					if(--this.inventory[0].stackSize == 0) this.inventory[0] = null;
					if(--this.inventory[1].stackSize == 0) this.inventory[1] = null;
					if(--this.inventory[2].stackSize == 0) this.inventory[2] = null;
					
					this.fill(ForgeDirection.UNKNOWN, new FluidStack(MineFracturing.INSTANCE.fracFluid, 3000), true);
					this.idle = 0;
				}
			}
		}
	}

	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet) {
		return false;
	}

	@Override
	public boolean canExtract(ForgeDirection direction) {
		return true;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] { 0, 1, 2, 3 };
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3) {
		return true;
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3) {
		return false;
	}
}
