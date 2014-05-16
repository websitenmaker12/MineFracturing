package de.teamdna.mf.block;

import de.teamdna.mf.net.ClientProxy;
import de.teamdna.mf.tile.TileEntityExtractor;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMaterialExtractor extends BlockContainer{

	public BlockMaterialExtractor() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityExtractor();
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
