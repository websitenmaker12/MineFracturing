package de.teamdna.mf.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import de.teamdna.mf.net.ClientProxy;
import de.teamdna.mf.tile.TileEntityPressureTube;

public class BlockPressureTube extends BlockContainer {

	public BlockPressureTube() {
		super(Material.iron);
		this.setBlockTextureName("iron_block");
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityPressureTube();
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileEntityPressureTube) {
			((TileEntityPressureTube)tile).neighborsHadChanged();
		}
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return ClientProxy.coreRenderID;
	}
	
	public void setBlockBoundsBasedOnState(IBlockAccess par1BlockAcces, int x, int y, int z)
    {
		TileEntityPressureTube tile = (TileEntityPressureTube) par1BlockAcces.getTileEntity(x, y, z);
		
		float minX = 0.25F;
		float maxX = 0.75F;
		float minY = 0.25F;
		float maxY = 0.75F;
		float minZ = 0.25F;
		float maxZ = 0.75F;
            
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
