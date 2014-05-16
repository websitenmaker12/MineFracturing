package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public interface IExtractor {

	boolean canExtract(ForgeDirection direction);
	NBTTagCompound extract(ForgeDirection direction);
	
}
