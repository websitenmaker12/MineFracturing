package de.teamdna.mf.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import de.teamdna.mf.net.ClientProxy;
import de.teamdna.mf.tile.TileEntityTank;

public class BlockTank extends BlockContainer {

	public final int type;
	
	public BlockTank(int type) {
		super(Material.iron);
		this.type = type;
	}
	@Override
	public boolean renderAsNormalBlock() {
		//if (type == 2) return false;
		//return super.renderAsNormalBlock();
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		//if (type == 2) return false;
		//return super.isOpaqueCube();
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1BlockAccess, int par2, int par3, int par4, int par5) {
		//if (type == 2) return true;
		//return super.shouldSideBeRendered(par1BlockAccess, par2, par3, par4, par5);
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderType() {
		//if (type == 2) return ClientProxy.coreRenderID;
		//return super.getRenderType();
		return ClientProxy.coreRenderID;
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
