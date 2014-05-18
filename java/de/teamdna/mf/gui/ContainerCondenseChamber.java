package de.teamdna.mf.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import de.teamdna.mf.tile.TileEntityCondenseChamber;

public class ContainerCondenseChamber extends Container {

	public TileEntityCondenseChamber tile;
	
	public ContainerCondenseChamber(EntityPlayer player, World world, int x, int y, int z) {
		this.tile = (TileEntityCondenseChamber)world.getTileEntity(x, y, z);
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}

}
