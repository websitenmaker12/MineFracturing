package de.teamdna.mf.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.Reference;

public class FurnaceImporter implements ICustomImporter {

	@Override
	public boolean canConnect(ForgeDirection direction) {
		return true;
	}

	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet, TileEntity tile) {
		return packet.getInteger("id") == Reference.PipePacketIDs.fluid
				&& packet.getInteger("fluidID") == MineFracturing.INSTANCE.oil.getID()
				&& ((TileEntityFurnace)tile).currentItemBurnTime < ((TileEntityFurnace)tile).furnaceBurnTime;
	}

	@Override
	public void doImport(ForgeDirection direction, NBTTagCompound packet, TileEntity tile) {
		TileEntityFurnace furnace = (TileEntityFurnace)tile;
		furnace.furnaceBurnTime = 25000;
		furnace.currentItemBurnTime = furnace.furnaceBurnTime;
	}

	@Override
	public World getWorld() {
		return null;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getZ() {
		return 0;
	}

	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet) {
		return false;
	}

	@Override
	public void doImport(ForgeDirection direction, NBTTagCompound packet) {
	}

}
