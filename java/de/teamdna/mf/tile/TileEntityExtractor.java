package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityExtractor extends TileEntity implements IExtractor {

	@Override
	public boolean canExtract(ForgeDirection side) {
		return false;
	}

	@Override
	public NBTTagCompound extract(ForgeDirection side) {
		return null;
	}

}
