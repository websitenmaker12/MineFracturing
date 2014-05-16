package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityExtractor extends TileEntity implements IExtractor {

	private long ticks = 0;
	
	@Override
	public void updateEntity() {
		ticks++;
	}
	
	@Override
	public boolean canExtract(ForgeDirection direction) {
		return ticks == 20;
	}

	@Override
	public NBTTagCompound extract(ForgeDirection direction) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("msg", "hallo");
		tag.setInteger("sends", 0);
		return tag;
	}

}
