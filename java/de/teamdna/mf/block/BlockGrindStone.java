package de.teamdna.mf.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.teamdna.mf.tile.TileEntityGrindStone;

public class BlockGrindStone extends BlockCore {

	public BlockGrindStone() {
		super(Material.rock);
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

}
