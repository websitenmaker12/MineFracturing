package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class CustomImporter implements IImporter {

	public final World world;
	public final int x, y, z;
	
	public CustomImporter(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public abstract boolean canConnect(ForgeDirection direction);

	@Override
	public abstract boolean canImport(ForgeDirection direction, NBTTagCompound packet);

	@Override
	public abstract void doImport(ForgeDirection direction, NBTTagCompound packet);

	public final TileEntity getTile() {
		return this.world.getTileEntity(this.x, this.y, this.z);
	}
	
}
