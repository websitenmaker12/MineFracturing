package de.teamdna.mf.util;

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
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class Util {

	public static String createUID(Object... elements) {
		return createUID("/", elements);
	}
	
	public static String createUID(String seperator, Object... elements) {
		String output = "";
		for(Object obj : elements) output += obj.toString() + seperator;
		return output.substring(0, output.length() - 1);
	}
	
	public static Object[] splitUID(String uid) {
		return splitUID(uid, "/");
	}
	
	public static Object[] splitUID(String uid, String seperator) {
		return uid.split(seperator);
	}
	
	public static boolean runsOnServer() {
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}
	
	public static int getFirstEmptyIndex(Object[] array) {
		for(int i = 0; i < array.length; i++) if(array[i] == null) return i;
		return -1;
	}
	
	public static int getFuelValue(ItemStack stack) {
		if(stack == null) return 0;
        else {
            Item item = stack.getItem();

            if(item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
                Block block = Block.getBlockFromItem(item);
                
                if(block == Blocks.wooden_slab) return 150;
                if(block.getMaterial() == Material.wood) return 300;
                if(block == Blocks.coal_block) return 16000;
            }

            if(item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if(item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if(item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD")) return 200;
            if(item == Items.stick) return 100;
            if(item == Items.coal) return 1600;
            if(item == Items.lava_bucket) return 20000;
            if(item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if(item == Items.blaze_rod) return 2400;
            return GameRegistry.getFuelValue(stack);
        }
	}
	
	public static ItemStack getEmptyForFilledContainer(ItemStack filledContainer) {
		for(FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
			if(data.filledContainer.isItemEqual(filledContainer)) return data.emptyContainer;
		}
		
		return null;
	}
	
	public static ItemStack getFilledForEmptyContainer(ItemStack emptyContainer, FluidStack fluid) {
		for(FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
			if(data.emptyContainer.isItemEqual(emptyContainer) && data.fluid.isFluidEqual(fluid)) return data.filledContainer;
		}
		
		return null;
	}
	
}
