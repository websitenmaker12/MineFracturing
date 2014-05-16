package de.teamdna.mf.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTraverse extends TileEntity {

	@Override
	public boolean canUpdate() {
		return false;
	}
	
	@Override
	public int getBlockMetadata() {
		if(this.worldObj == null) return 0;
		else return super.getBlockMetadata();
    }
	
	@Override
	public Block getBlockType() {
		if(this.worldObj == null) return null;
		else return super.getBlockType();
    }
	
}
