package de.teamdna.mf.tile;

import net.minecraft.block.BlockFurnace;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.Reference;

public class FurnaceImporter extends CustomImporter {

	private TileEntity tile;
	
	public FurnaceImporter(World world, int x, int y, int z) {
		super(world, x, y, z);
		this.tile = this.getTile();
	}

	@Override
	public boolean canConnect(ForgeDirection direction) {
		return true;
	}

	@Override
	public boolean canImport(ForgeDirection direction, NBTTagCompound packet) {
		return packet.getInteger("id") == Reference.PipePacketIDs.fluid
				&& packet.getInteger("fluidID") == MineFracturing.INSTANCE.oil.getID()
				&& (((TileEntityFurnace)this.tile).currentItemBurnTime < ((TileEntityFurnace)this.tile).furnaceBurnTime || ((TileEntityFurnace)this.tile).furnaceBurnTime == 0);
	}

	@Override
	public void doImport(ForgeDirection direction, NBTTagCompound packet) {
		TileEntityFurnace furnace = (TileEntityFurnace)this.tile;
		BlockFurnace.updateFurnaceBlockState(true, this.tile.getWorldObj(), this.x, this.y, this.z);
		furnace.furnaceBurnTime = 25000;
		furnace.currentItemBurnTime = furnace.furnaceBurnTime;
	}

}
