package de.teamdna.mf.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class ContainerBore extends Container{

	public ContainerBore(EntityPlayer player, World world, int x, int y, int z) {
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}

}