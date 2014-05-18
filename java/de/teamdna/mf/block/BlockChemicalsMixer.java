package de.teamdna.mf.block;

import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.tile.TileEntityChemicalsMixer;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChemicalsMixer extends BlockCore {

	public BlockChemicalsMixer() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityChemicalsMixer();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
		player.openGui(MineFracturing.INSTANCE, 2, world, x, y, z);
		return true;
	}
}
