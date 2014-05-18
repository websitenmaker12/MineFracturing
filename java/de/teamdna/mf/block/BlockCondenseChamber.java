package de.teamdna.mf.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.teamdna.mf.tile.TileEntityCondenseChamber;

public class BlockCondenseChamber extends BlockCore {

	public BlockCondenseChamber() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityCondenseChamber();
	}

}
