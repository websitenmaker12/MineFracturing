package de.teamdna.mf.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.gui.GuiHandler;
import de.teamdna.mf.net.ClientProxy;
import de.teamdna.mf.tile.TileEntityBore;

public class BlockBore extends BlockContainer implements IBoreBlock {

	public BlockBore() {
		super(Material.iron);
		this.setBlockTextureName("iron_block");
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBore();
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
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
		entityPlayer.openGui(MineFracturing.INSTANCE, 1, par1World, x, y, z);
		return true;
	}
}
