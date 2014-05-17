package de.teamdna.mf.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class FluidSlot extends Slot {

	public FluidSlot(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}

	public boolean isItemValid(ItemStack stack) {
        return FluidContainerRegistry.isContainer(stack);
    }
	
}
