package de.teamdna.mf.event;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;
import de.teamdna.mf.MineFracturing;

public class FuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {
		if(fuel.getItem() == MineFracturing.INSTANCE.bucketOil) return 25000;
		return 0;
	}

}
