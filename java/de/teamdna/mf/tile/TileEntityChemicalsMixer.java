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
	private int maxBurnTime = 1;
	
	public TileEntityChemicalsMixer() {
		super(6);
		this.inventory = new ItemStack[4];
	}
	
	//Updates the entity
	@Override
	public void updateEntity() {
		if (this.burnTime == 0) {
			if (isItemFuel(inventory[3]) && canWork()) {
				this.burnTime = getItemBurnTime(inventory[3]);
				this.maxBurnTime = this.burnTime;
				this.decrStackSize(3);
			}
		}
		else this.burnTime--;
		if (this.burnTime > 0 && canWork()) {
			if(this.workProgress < maxWorkProgress) this.workProgress++;
			else {
				this.doWork();
				this.workProgress = 0;
			}
		}
	}
	
	//Returns if the mixer is working
	private boolean isWorking() {
		return this.workProgress > 0;
	}
	
	//For GUI
	public int getWorkProgressScaled(int pixels) {
		return this.workProgress * pixels / this.maxWorkProgress;
	}
	
	public int getBurnTimeScaled(int pixels) {
		return this.burnTime * pixels / this.maxBurnTime;
	}
	
	//Returns the burn time of an item
	private int getItemBurnTime(ItemStack itemstack) {
		return Util.getFuelValue(itemstack);
	}
	
	//Returns if the item is a fuel
	public boolean isItemFuel(ItemStack itemstack) {
		return getItemBurnTime(itemstack) > 0;
	}
	
	//checks if the machine could work
	private boolean canWork()
	{
		if (this.tank.getFluidAmount() + 1000 <= this.tank.getCapacity()) {
			if(isRecipe(inventory)) return true;
		}
		return false;
	}
	
	//Lets the machine finish it´s work (Adds liquid, etc.)
	private void doWork() {
		if (canWork()) {
			tank.fill(new FluidStack(MineFracturing.INSTANCE.fracFluid, 500), !this.worldObj.isRemote);
			decrStackSize(0);
			decrStackSize(1);
			decrStackSize(2);
		}
	}
	
	//Decreases the stack size of the given index by one
	private void decrStackSize(int stackID) {
		if (inventory[stackID] != null) {
			ItemStack itemstack = inventory[stackID];
			itemstack.stackSize--;
			if (itemstack.stackSize == 0) itemstack = null;
			
			inventory[stackID] = itemstack;
		}
	}
	
	//Vailed recipe?
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
