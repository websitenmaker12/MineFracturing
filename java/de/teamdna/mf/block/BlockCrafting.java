package de.teamdna.mf.block;

import de.teamdna.mf.MineFracturing;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockCrafting extends Block{

	public BlockCrafting() {
		super(Material.iron);
		setResistance(10F);
		setCreativeTab(MineFracturing.INSTANCE.tab);
	}

}
