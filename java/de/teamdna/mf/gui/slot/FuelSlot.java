package de.teamdna.mf.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import de.teamdna.mf.util.Util;

public class FuelSlot extends Slot {

	public FuelSlot(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}

	public boolean isItemValid(ItemStack stack) {
        return Util.getFuelValue(stack) > 0;
    }
	
}
