package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public interface IImporter {

	boolean canImport(ForgeDirection direction, NBTTagCompound packet);
	void doImport(ForgeDirection direction, NBTTagCompound packet);
	
}
