package de.teamdna.mf.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import de.teamdna.mf.MineFracturing;

public class CoreRegistry {

	private static List<Integer> oreBlocks = new ArrayList<Integer>();
	private static Map<Integer, Integer> containerBlocks = new HashMap<Integer, Integer>();
	private static Map<Integer, ItemStack> condensedItems = new HashMap<Integer, ItemStack>();
	
	/**
	 * Registers a Block as an Ore
	 */
	public static void registerOre(Block ore, ItemStack condensedItem) {
		int id = Block.getIdFromBlock(ore);
		if(!oreBlocks.contains(id)) {
			oreBlocks.add(id);
			registerContainerBlock(ore, Blocks.stone);
			condensedItems.put(id, condensedItem);
		}
	}
	
	/**
	 * Scans the BlockRegistry for Ore entries
	 */
	public static void scanForOres() {
		Iterator iterator = Block.blockRegistry.iterator();
		while(iterator.hasNext()) {
			Object block = iterator.next();
			if(block != null && block instanceof BlockOre) {
				ItemStack stack = null;
				int id = Block.getIdFromBlock((Block)block);
				
				if(id == Block.getIdFromBlock(Blocks.lapis_ore)) stack = new ItemStack(Items.dye, 1, 4);
				else if(id == Block.getIdFromBlock(Blocks.quartz_ore)) stack = new ItemStack(Items.quartz);
				else if(id == Block.getIdFromBlock(Blocks.coal_ore)) stack = new ItemStack(MineFracturing.INSTANCE.coalDust);
				else if(id == Block.getIdFromBlock(Blocks.iron_ore)) stack = new ItemStack(MineFracturing.INSTANCE.ironDust);
				else if(id == Block.getIdFromBlock(Blocks.gold_ore)) stack = new ItemStack(MineFracturing.INSTANCE.goldDust);
				else if(id == Block.getIdFromBlock(Blocks.diamond_ore)) stack = new ItemStack(MineFracturing.INSTANCE.diamondDust);
				else if(id == Block.getIdFromBlock(Blocks.emerald_ore)) stack = new ItemStack(MineFracturing.INSTANCE.emeraldDust);
				registerOre((BlockOre)block, stack);
			}
		}
	}
	
	/**
	 * Checks if a block is a registered ore
	 */
	public static boolean isOre(Block ore) {
		return oreBlocks.contains(Block.getIdFromBlock(ore));
	}
	
	
	/**
	 * Sets the Container block which replaces a fractured ore
	 */
	public static void registerContainerBlock(Block ore, Block container) {
		if(oreBlocks.contains(Block.getIdFromBlock(ore))) {
			containerBlocks.put(Block.getIdFromBlock(ore), Block.getIdFromBlock(container));
		}
	}
	
	/**
	 * Returns the Container for the given Ore
	 */
	public static Block getContainer(Block ore) {
		if(!containerBlocks.containsKey(Block.getIdFromBlock(ore))) return null;
		return Block.getBlockById(containerBlocks.get(Block.getIdFromBlock(ore)));
	}
	
	/**
	 * Returns the Item for the given Ore
	 */
	public static ItemStack getCondensedItem(Block ore) {
		return condensedItems.get(Block.getIdFromBlock(ore));
	}
	
}
