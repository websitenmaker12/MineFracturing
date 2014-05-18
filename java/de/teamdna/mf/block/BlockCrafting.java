package de.teamdna.mf.block;

import de.teamdna.mf.MineFracturing;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockCrafting extends Block {

	public BlockCrafting() {
		super(Material.iron);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(MineFracturing.INSTANCE.tab);
	}

}
