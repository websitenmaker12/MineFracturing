package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityTraverse extends TileEntity implements IImporter {

	@Override
	public boolean canUpdate() {
		return false;
	}
	
	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet) {
		return true;
	}

	@Override
	public void doImport(ForgeDirection direction, NBTTagCompound packet) {
		System.out.println("Incoming Msg: " + packet.getString("msg") + " (" + packet.getInteger("sends") + ")");
	}
	
}
