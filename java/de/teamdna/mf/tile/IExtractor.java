package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public interface IExtractor {

	boolean canExtract(ForgeDirection side);
	NBTTagCompound extract(ForgeDirection side);
	
}
