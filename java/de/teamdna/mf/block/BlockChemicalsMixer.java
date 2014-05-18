package de.teamdna.mf.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChemicalsMixer extends BlockContainer {

	protected BlockChemicalsMixer(Material material) {
		super(material);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return null;
	}

}
