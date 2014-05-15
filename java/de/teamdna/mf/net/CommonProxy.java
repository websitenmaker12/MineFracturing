package de.teamdna.mf.net;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

public class CommonProxy {

	public void registerBlock(Block block) {
		GameRegistry.registerBlock(block, block.getUnlocalizedName());
	}
	
	public void registerItem(Item item) {
		GameRegistry.registerItem(item, item.getUnlocalizedName());
	}
	
	public void registerTile(Class<? extends TileEntity> tile, String name) {
		GameRegistry.registerTileEntity(tile, name);
	}
	
}
