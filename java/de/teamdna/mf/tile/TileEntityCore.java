package de.teamdna.mf.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AABBPool;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityCore extends TileEntity {

	@Override
	public int getBlockMetadata() {
		if(!this.hasWorldObj()) return 0;
		else return super.getBlockMetadata();
    }
	
	@Override
	public Block getBlockType() {
		if(!this.hasWorldObj()) return null;
		else return super.getBlockType();
    }
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
