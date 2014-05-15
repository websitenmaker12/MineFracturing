package de.teamdna.mf.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Level;

import de.teamdna.mf.MineFracturing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;

public class CoreRegistry {

	private static List<Integer> oreBlocks = new ArrayList<Integer>();
	
	public static void registerOre(Block block) {
		int id = Block.getIdFromBlock(block);
		if(!oreBlocks.contains(id)) {
			oreBlocks.add(id);
		}
	}
	
	public static void scanForOres() {
		Iterator iterator = Block.blockRegistry.iterator();
		while(iterator.hasNext()) {
			Object block = iterator.next();
			if(block != null && block instanceof BlockOre) {
				registerOre((BlockOre)block);
			}
		}
	}
	
	public static boolean isOre(Block block) {
		return oreBlocks.contains(Block.getIdFromBlock(block));
	}
	
}
