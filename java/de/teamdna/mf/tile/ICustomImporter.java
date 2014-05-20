package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public interface ICustomImporter extends IImporter {

	boolean canImport(ForgeDirection direction, NBTTagCompound packet, TileEntity tile);
	void doImport(ForgeDirection direction, NBTTagCompound packet, TileEntity tile);
	
}
