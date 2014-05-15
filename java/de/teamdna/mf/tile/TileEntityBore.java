package de.teamdna.mf.tile;

import net.minecraft.tileentity.TileEntity;

public class TileEntityBore extends TileEntity {

	public int state = 0; // Inactive: -1
	
	public int boreY;
	
	public TileEntityBore() {
		this.boreY = this.yCoord;
		System.out.println(this.yCoord);
	}
	
	@Override
	public void updateEntity() {
		if(!this.worldObj.isRemote) {
			if(this.state == 0) {
			}
		}
	}
	
}
