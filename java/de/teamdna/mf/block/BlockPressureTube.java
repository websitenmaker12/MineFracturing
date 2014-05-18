package de.teamdna.mf.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import de.teamdna.mf.net.ClientProxy;
import de.teamdna.mf.tile.TileEntityPressureTube;

public class BlockPressureTube extends BlockCore {

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
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.coreRenderID;
	}
	
	public void setBlockBoundsBasedOnState(IBlockAccess par1BlockAcces, int x, int y, int z)
    {
		TileEntityPressureTube tile = (TileEntityPressureTube) par1BlockAcces.getTileEntity(x, y, z);
		
		float minX = 0.4F;
		float maxX = 0.6F;
		float minY = 0.4F;
		float maxY = 0.6F;
		float minZ = 0.4F;
		float maxZ = 0.6F;
        
		if (tile.isConnectedToSide(ForgeDirection.NORTH)) minZ -= 0.4F;
		if (tile.isConnectedToSide(ForgeDirection.SOUTH)) maxZ += 0.4F;
		if (tile.isConnectedToSide(ForgeDirection.WEST)) minX -= 0.4F;
		if (tile.isConnectedToSide(ForgeDirection.EAST)) maxX += 0.4F;
		if (tile.isConnectedToSide(ForgeDirection.UP)) maxY += 0.4F;
		if (tile.isConnectedToSide(ForgeDirection.DOWN)) minY -= 0.4F;
		
		this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
