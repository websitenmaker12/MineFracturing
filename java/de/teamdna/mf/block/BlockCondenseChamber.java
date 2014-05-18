package de.teamdna.mf.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.tile.TileEntityCondenseChamber;

public class BlockCondenseChamber extends BlockCore {

	public BlockCondenseChamber() {
		super(Material.iron);
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
	
}
