package de.teamdna.mf.item;

import net.minecraft.block.BlockStoneSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import de.teamdna.mf.MineFracturing;

public class ItemWoodenPillar extends Item {

	public ItemWoodenPillar() {
		this.setMaxStackSize(1);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int meta, float hx, float hy, float hz) {
		if(!world.isRemote && world.getBlock(x, y, z) instanceof BlockStoneSlab && world.getBlock(x, y, z).renderAsNormalBlock() && world.getBlockMetadata(x, y, z) == 0) {
			player.inventory.consumeInventoryItem(MineFracturing.INSTANCE.woodenPillar);
			world.setBlock(x, y, z, MineFracturing.INSTANCE.grindStone);
			return true;
		}
		
        return false;
    }
	
}
