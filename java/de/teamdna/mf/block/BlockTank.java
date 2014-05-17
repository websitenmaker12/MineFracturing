package de.teamdna.mf.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.teamdna.mf.tile.TileEntityTank;

public class BlockTank extends BlockContainer {

	public final int type;
	
	public BlockTank(int type) {
		super(Material.iron);
		this.type = type;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityTank(this.type);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileEntityTank) {
			world.addBlockEvent(x, y, z, this, 1, 0);
		}
	}

	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int eventID, int eventData) {
		super.onBlockEventReceived(world, x, y, z, eventID, eventData);
		if(eventID != 1) return true;
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileEntityTank) ((TileEntityTank)tile).neighborsHadChanged();
        return true;
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float dx, float dy, float dz) {
		TileEntityTank tile = (TileEntityTank)world.getTileEntity(x, y, z);
		if(!tile.isStructureComplete()) return false;
		
		ItemStack itemstack = player.inventory.getCurrentItem();
	    if(itemstack != null && itemstack.getItem() == Items.bucket) {
		    if(itemstack.stackSize-- == 1) {
		    	player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.milk_bucket));
		    }
		    else if(!player.inventory.addItemStackToInventory(new ItemStack(Items.milk_bucket))) {
		    	player.dropPlayerItemWithRandomChoice(new ItemStack(Items.milk_bucket, 1, 0), false);
		    }
	    }
	    
        return true;
    }
	
}
