package de.teamdna.mf.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockGhostLoader extends Block {

	public BlockGhostLoader() {
		super(Material.air);
		this.setTickRandomly(true);
	}

	@Override
	public int getRenderType() {
        return -1;
    }

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

	@Override
    public boolean isOpaqueCube() {
        return false;
    }

	@Override
    public boolean canCollideCheck(int par1, boolean par2) {
        return false;
    }

	@Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float f, int i) {  }
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int meta, float f1, float f2, float f3, int i) {
		world.markBlockForUpdate(x, y, z);
        return i;
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		world.setBlockToAir(x, y, z);
	}
	
}
