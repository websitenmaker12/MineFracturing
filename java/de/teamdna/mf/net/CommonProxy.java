package de.teamdna.mf.net;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import de.teamdna.mf.Reference;

public abstract class CommonProxy {

	public void registerBlock(Block block) {
		GameRegistry.registerBlock(block, Reference.modid + "_" + block.getUnlocalizedName());
	}
	
	public void registerItem(Item item) {
		GameRegistry.registerItem(item, Reference.modid + "_" + item.getUnlocalizedName());
	}
	
	public void registerTile(Class<? extends TileEntity> tile, String name) {
		GameRegistry.registerTileEntity(tile, Reference.modid + "_" + name);
	}
	
	public abstract EntityPlayer getClientPlayer();
	
}
