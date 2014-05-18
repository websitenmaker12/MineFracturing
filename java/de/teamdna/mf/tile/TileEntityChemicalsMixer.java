package de.teamdna.mf.tile;

import cpw.mods.fml.common.registry.GameRegistry;
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
import net.minecraft.tileentity.TileEntity;

public class TileEntityChemicalsMixer extends TileEntityCore {
	
	private int workProgress = 0;
	private int maxWorkProgress = 1000;
	private int burnTime = 0;
	
	public TileEntityChemicalsMixer() {
		this.inventory = new ItemStack[3];
	}
	
	@Override
	public void updateEntity() {
		
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
		return false;
	}
	
	private void doWork()
	{
		if (canWork()) {
			
		}
	}
	
	private boolean isRecipe(ItemStack[] itemstacks)
	{
		if (itemstacks[0] != null && itemstacks[1] != null && itemstacks[2] != null) {
			boolean blazeRodFound = false;
			boolean slimeFound = false;
			boolean redstoneFound = false;
			
			if(itemstacks[0].getItem() == Items.slime_ball || itemstacks[1].getItem() == Items.slime_ball || itemstacks[2].getItem() == Items.slime_ball) slimeFound = true;
		}
		return false;
	}
}
