package de.teamdna.mf;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class JetpackRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for(int i = 0; i < var1.getSizeInventory(); i++) {
			if(var1.getStackInSlot(i) != null) stacks.add(var1.getStackInSlot(i));
		}

		if(stacks.size() != 2) return false;
		if((stacks.get(0).getItem() == MineFracturing.INSTANCE.jetpack && stacks.get(1).getItem() == MineFracturing.INSTANCE.bucketOil)
				|| (stacks.get(1).getItem() == MineFracturing.INSTANCE.jetpack && stacks.get(0).getItem() == MineFracturing.INSTANCE.bucketOil)) return true;
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		return new ItemStack(MineFracturing.INSTANCE.jetpack);
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(MineFracturing.INSTANCE.jetpack);
	}

}
