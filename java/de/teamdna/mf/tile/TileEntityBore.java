package de.teamdna.mf.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBore extends TileEntity {

	public int state = -1; // Inactive: -1 ; Boring: 0
	public int boreY;
	
	public TileEntityBore() {
	}
	
	@Override
	public void updateEntity() {
		if(!this.worldObj.isRemote) {
			
			if(this.state == -1) {
				this.state = 0;
				this.boreY = this.yCoord;
			}
			
			if(this.state == 0 && this.worldObj.getWorldTime() % 40L == 0L) {
				if(--this.boreY < 8) {
					this.state = 1;
					return;
				}
				
				Block currentBlock = this.worldObj.getBlock(this.xCoord, this.boreY, this.zCoord);
				if(!currentBlock.getMaterial().isLiquid() && currentBlock.getBlockHardness(this.worldObj, this.xCoord, this.boreY, this.zCoord) > 0) {
					this.worldObj.setBlockToAir(this.xCoord, this.boreY, this.zCoord);
				}
			}
			
		}
	}
	
}
