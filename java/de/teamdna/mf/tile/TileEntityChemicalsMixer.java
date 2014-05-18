package de.teamdna.mf.tile;

import cpw.mods.fml.common.registry.GameRegistry;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityChemicalsMixer extends TileEntityFluidCore implements IExtractor, IFluidHandler{
	
	private int workProgress = 0;
	private int maxWorkProgress = 200;
	private int burnTime = 0;
	
	public TileEntityChemicalsMixer() {
		super(6);
		this.inventory = new ItemStack[4];
	}
	
	@Override
	public void updateEntity() {
		if (this.burnTime == 0 && canWork()) {
			if (isItemFuel(inventory[3])) {
				this.burnTime = getItemBurnTime(inventory[3]);
				this.decrStackSize(3);
			}
		}
		else this.burnTime--;
		if (canWork()) {
			if(this.workProgress < maxWorkProgress) this.workProgress++;
			else {
				this.doWork();
				this.workProgress = 0;
			}
		}
	}
	
	private boolean isWorking() {
		return this.workProgress > 0;
	}
	
	public int getWorkProgressScaled(int pixels) {
		return this.workProgress * pixels / this.maxWorkProgress;
	}
	
	private int getItemBurnTime(ItemStack itemstack) {
		return Util.getFuelValue(itemstack);
	}
	
	private boolean isItemFuel(ItemStack itemstack) {
		return getItemBurnTime(itemstack) > 0;
	}
	
	private boolean canWork()
	{
//		System.out.println("amount: "+tank.getFluidAmount() + " & capacity: "+tank.getCapacity() + " amount + 1000: " + (tank.getFluidAmount() + 1000));
		if (this.burnTime > 0 && this.tank.getFluidAmount() + 1000 <= this.tank.getCapacity()) {
			if(isRecipe(inventory)) return true;
			System.out.println("hi");
		}
		return false;
	}
	
	private void doWork() {
		if (canWork()) {
			tank.fill(new FluidStack(MineFracturing.INSTANCE.fracFluid, 1000), !this.worldObj.isRemote);
			decrStackSize(0);
			decrStackSize(1);
			decrStackSize(2);
		}
	}
	
	private void decrStackSize(int stackID) {
		if (inventory[stackID] != null) {
			ItemStack itemstack = inventory[stackID].copy();
			itemstack.stackSize--;
			if (itemstack.stackSize == 0) itemstack = null;
			
			inventory[stackID] = itemstack;
		}
	}
	
	private boolean isRecipe(ItemStack[] itemstacks)
	{
		if (itemstacks[0] != null && itemstacks[1] != null && itemstacks[2] != null) {
			boolean blazeRodFound = false;
			boolean slimeFound = false;
			boolean redstoneFound = false;
			
			if(itemstacks[0].getItem() == Items.slime_ball || itemstacks[1].getItem() == Items.slime_ball || itemstacks[2].getItem() == Items.slime_ball) slimeFound = true;
			if(itemstacks[0].getItem() == Items.blaze_powder || itemstacks[1].getItem() == Items.blaze_powder || itemstacks[2].getItem() == Items.blaze_powder) blazeRodFound = true;
			if(itemstacks[0].getItem() == Items.redstone || itemstacks[1].getItem() == Items.redstone || itemstacks[2].getItem() == Items.redstone) redstoneFound = true;
			
			if(redstoneFound && blazeRodFound && slimeFound) return true;
		}
		return false;
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
}
