package de.teamdna.mf.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
}
