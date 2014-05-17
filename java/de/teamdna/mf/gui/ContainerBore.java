package de.teamdna.mf.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import de.teamdna.mf.gui.slot.FuelSlot;
import de.teamdna.mf.tile.TileEntityBore;

public class ContainerBore extends Container {

	private TileEntityBore tile;
	
	public ContainerBore(EntityPlayer player, World world, int x, int y, int z) {
		this.tile = (TileEntityBore)world.getTileEntity(x, y, z);
		this.addSlotToContainer(new FuelSlot(this.tile, 0, 47, 52));
		
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
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(slotID == 0) {
                if(!this.mergeItemStack(itemstack1, 1, 37, true)) return null;
            } else {
                this.mergeItemStack(itemstack1, 0, 1, false);
            }

            if(itemstack1.stackSize == 0) slot.putStack((ItemStack)null);
            else slot.onSlotChanged();

            if(itemstack1.stackSize == itemstack.stackSize) return null;
            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
	
}
