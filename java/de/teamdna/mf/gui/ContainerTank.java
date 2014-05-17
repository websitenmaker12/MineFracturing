package de.teamdna.mf.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import de.teamdna.mf.tile.TileEntityTank;

public class ContainerTank extends Container {

	private TileEntityTank tile;
	
	public ContainerTank(EntityPlayer player, World world, int x, int y, int z) {
		this.tile = (TileEntityTank)world.getTileEntity(x, y, z);
		this.addSlotToContainer(new Slot(this.tile, 0, 148, 15));
		this.addSlotToContainer(new Slot(this.tile, 1, 148, 60));
		
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
