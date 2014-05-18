package de.teamdna.mf.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockGenerator extends Block{

    @SideOnly(Side.CLIENT)
    private IIcon frontIcon;
    @SideOnly(Side.CLIENT)
    private IIcon topIcon;
    
	public BlockGenerator() {
		super(Material.iron);
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		return par1 == 1 ? this.topIcon : (par1 == 0 ? this.topIcon : (par1 != par2 ? this.blockIcon : this.frontIcon));
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		this.blockIcon = p_149651_1_.registerIcon("gen_side");
		this.frontIcon = p_149651_1_.registerIcon("gen_front");
		this.topIcon = p_149651_1_.registerIcon("gen");
	}
}
