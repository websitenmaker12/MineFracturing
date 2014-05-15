package de.teamdna.mf.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.util.WorldUtil;

public class BlockBore extends BlockContainer {

	public BlockBore() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBore();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		// TODO Auto-generated method stub
		WorldUtil.setBiomeForCoords(world, x, z, 1);
		return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
	}

}
