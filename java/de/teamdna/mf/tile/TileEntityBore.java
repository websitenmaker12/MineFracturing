package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBore extends TileEntity {

	public final int maxBoreY = 8;
	public final int chunkRadius = 4;
	
	public int state = -1; // Inactive: -1 ; Boring: 0 ; Injecting: 1
	public int boreY;
	
	public List<String> oreBlocks = new ArrayList<String>();
	
	@Override
	public void updateEntity() {
		if(!this.worldObj.isRemote) {
			
			// Setups the bore
			if(this.state == -1) {
				this.state = 0;
				this.boreY = this.yCoord;
			}
			
			// Bores to a hole until it reaches maxBoreY
			if(this.state == 0 && this.worldObj.getWorldTime() % 40L == 0L) {
				if(--this.boreY < this.maxBoreY) {
					this.state = 1;
					return;
				}
				
				Block currentBlock = this.worldObj.getBlock(this.xCoord, this.boreY, this.zCoord);
				if(!currentBlock.getMaterial().isLiquid() && currentBlock.getBlockHardness(this.worldObj, this.xCoord, this.boreY, this.zCoord) > 0) {
					this.worldObj.func_147480_a(this.xCoord, this.boreY, this.zCoord, false);
				}
			}
			
			// Starts infecting the world and earning resources
			if(this.state == 1) {
			}
			
		}
	}
	
}
