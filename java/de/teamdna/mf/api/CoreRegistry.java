package de.teamdna.mf.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.init.Blocks;

public class CoreRegistry {

	private static List<Integer> oreBlocks = new ArrayList<Integer>();
	private static Map<Integer, Integer> containerBlocks = new HashMap<Integer, Integer>();
	
	/**
	 * Registers a Block as an Ore
	 */
	public static void registerOre(Block ore) {
		int id = Block.getIdFromBlock(ore);
		if(!oreBlocks.contains(id)) {
			oreBlocks.add(id);
			registerContainerBlock(ore, Blocks.stone);
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
				registerOre((BlockOre)block);
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
	
}
