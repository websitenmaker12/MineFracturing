package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityExtractor extends TileEntityCore implements IExtractor {

	@Override
	public boolean canExtract(ForgeDirection direction) {
		return true;
	}

	@Override
	public NBTTagCompound extract(ForgeDirection direction) {
		return null;
	}

}
