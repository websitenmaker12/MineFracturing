package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public interface IExtractor extends IConnectable {

	boolean canExtract(ForgeDirection direction);
	NBTTagCompound extract(ForgeDirection direction, boolean doExtract);
	
}
