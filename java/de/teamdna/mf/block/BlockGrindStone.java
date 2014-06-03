package de.teamdna.mf.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.teamdna.mf.net.ClientProxy;
import de.teamdna.mf.tile.TileEntityGrindStone;

public class BlockGrindStone extends BlockCore {

	private IIcon topIcon;
	public BlockGrindStone() {
		super(Material.rock);
		this.setBlockTextureName("mf:grinder");
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityGrindStone();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hx, float hy, float hz) {
		if(!world.isRemote) ((TileEntityGrindStone)world.getTileEntity(x, y, z)).spin();
        return true;
    }
	
//	@Override
//	public boolean renderAsNormalBlock() {
//		return false;
//	}
//	
//	@Override
//	public boolean isOpaqueCube() {
//		return false;
//	}
//	
//	@Override
//	public boolean shouldSideBeRendered(IBlockAccess par1BlockAccess, int par2, int par3, int par4, int par5) {
//		return true;
//	}
//	
//	@SideOnly(Side.CLIENT)
//	@Override
//	public int getRenderType() {
//		return ClientProxy.coreRenderID;
//	}
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        return par1 == 0 ? this.topIcon : (par1 == 1 ? this.topIcon : this.blockIcon);
    }
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.blockIcon = register.registerIcon(this.getTextureName());
		this.topIcon = register.registerIcon(this.getTextureName() + "_top");
	}
}
