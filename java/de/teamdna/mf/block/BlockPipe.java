package de.teamdna.mf.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.teamdna.mf.net.ClientProxy;
import de.teamdna.mf.tile.PipeNetworkController;
import de.teamdna.mf.tile.TileEntityPipe;

public class BlockPipe extends BlockCore {

	public BlockPipe() {
		super(Material.iron);
		this.setBlockTextureName("iron_block");
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityPipe();
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileEntityPipe) {
			((TileEntityPipe)tile).neighborsHadChanged();
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileEntityPipe) {
			TileEntityPipe pipe = (TileEntityPipe)tile;
			if(pipe.networkID != -1L) {
				PipeNetworkController.INSTNACE.destroyNetwork(pipe.networkID);
			}
		}
		
        super.breakBlock(world, x, y, z, block, meta);
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
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		TileEntityPipe tile = (TileEntityPipe)world.getTileEntity(x, y, z);
		
		float minX = 0.4F;
		float maxX = 0.6F;
		float minY = 0.4F;
		float maxY = 0.6F;
		float minZ = 0.4F;
		float maxZ = 0.6F;
        
		if(tile.isConnectedToSide(ForgeDirection.NORTH)) minZ -= 0.4F;
		if(tile.isConnectedToSide(ForgeDirection.SOUTH)) maxZ += 0.4F;
		if(tile.isConnectedToSide(ForgeDirection.WEST)) minX -= 0.4F;
		if(tile.isConnectedToSide(ForgeDirection.EAST)) maxX += 0.4F;
		if(tile.isConnectedToSide(ForgeDirection.UP)) maxY += 0.4F;
		if(tile.isConnectedToSide(ForgeDirection.DOWN)) minY -= 0.4F;
		
		this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
