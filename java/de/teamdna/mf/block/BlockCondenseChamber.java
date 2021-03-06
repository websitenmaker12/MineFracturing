package de.teamdna.mf.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.net.ClientProxy;
import de.teamdna.mf.tile.TileEntityCondenseChamber;

public class BlockCondenseChamber extends BlockCore {

	public BlockCondenseChamber() {
		super(Material.iron);
		this.setBlockTextureName("iron_block");
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
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderType() {
		return ClientProxy.coreRenderID;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityCondenseChamber();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
		player.openGui(MineFracturing.INSTANCE, 3, world, x, y, z);
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(l == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if(l == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if(l == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if(l == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
    }
}
