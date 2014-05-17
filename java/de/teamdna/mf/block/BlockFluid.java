package de.teamdna.mf.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.teamdna.mf.Reference;

public class BlockFluid extends BlockFluidClassic {

	@SideOnly(Side.CLIENT)
	private IIcon still, flow;
	
	private boolean flammable;
	private int flammability = 0;
	
	public BlockFluid(Fluid fluid, Material material) {
		super(fluid, material);
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return side != 0 && side != 1 ? this.flow : this.still;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.still = register.registerIcon(Reference.modid + ":" + this.fluidName + "_still");
		this.flow = register.registerIcon(Reference.modid + ":" + this.fluidName + "_flow");
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		if(world.getBlock(x, y, z).getMaterial().isLiquid()) return false;
		return super.canDisplace(world, x, y, z);
	}
	
	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		if(world.getBlock(x, y, z).getMaterial().isLiquid()) return false;
		return super.displaceIfPossible(world, x, y, z);
	}
	
}
