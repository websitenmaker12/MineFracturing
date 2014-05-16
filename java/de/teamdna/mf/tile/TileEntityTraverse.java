package de.teamdna.mf.tile;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityTraverse extends TileEntity implements IImporter {

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

	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet) {
		return true;
	}

	@Override
	public void doImport(ForgeDirection direction, NBTTagCompound packet) {
		System.out.println("Incoming Msg: " + packet.getString("msg"));
	}
	
}
