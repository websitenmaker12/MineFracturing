package de.teamdna.mf.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
			case 0: return new ContainerTank(player, world, x, y, z);
			case 1: return new ContainerBore(player, world, x, y, z);
			case 2: return new ContainerChemicalsMixer(player, world, x, y, z);
			case 3: return new ContainerCondenseChamber(player, world, x, y, z);
			default: return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
			case 0: return new GuiTank(player, world, x, y, z);
			case 1: return new GuiBore(player, world, x, y, z);
			case 2: return new GuiChemicalsMixer(player, world, x, y, z);
			case 3: return new GuiCondenseChamber(player, world, x, y, z);
			default: return null;
		}
	}

}
