package de.teamdna.mf.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.teamdna.mf.Reference;
import de.teamdna.mf.net.ClientProxy;
import de.teamdna.mf.tile.TileEntityGrindStone;

public class BlockGrindStone extends BlockCore {

	public BlockGrindStone() {
		super(Material.rock);
		this.setBlockTextureName(Reference.modid + ":tank");
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityGrindStone();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hx, float hy, float hz) {
		((TileEntityGrindStone)world.getTileEntity(x, y, z)).spin();
        return true;
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
	public boolean shouldSideBeRendered(IBlockAccess par1BlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.coreRenderID;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0, 0, 0, 1, 0.6F, 1);
    }
	
}
