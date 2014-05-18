package de.teamdna.mf.gui;

import de.teamdna.mf.tile.TileEntityChemicalsMixer;
import de.teamdna.mf.tile.TileEntityTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;

public class ContainerChemicalsMixer extends Container {

	private TileEntityChemicalsMixer tile;
	
	public ContainerChemicalsMixer(EntityPlayer player, World world, int x, int y, int z) {
		this.tile = (TileEntityChemicalsMixer)world.getTileEntity(x, y, z);
		
		for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
            	this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}

}
