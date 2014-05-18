package de.teamdna.mf.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import de.teamdna.mf.gui.slot.FuelSlot;
import de.teamdna.mf.tile.TileEntityBore;

public class ContainerBore extends Container {

	private TileEntityBore tile;

	public int fluidAmount = 0;
	public int prog1 = 0;
	public int prog2 = 0;
	
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
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        FluidStack fluid = this.tile.tank.getFluid();
        par1ICrafting.sendProgressBarUpdate(this, 0, fluid == null ? 0 : fluid.amount);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.tile.getScaledAnalysingProgress(117));
        par1ICrafting.sendProgressBarUpdate(this, 2, this.tile.getScaledFracturingProgress(117));
    }

	@Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            FluidStack fluid = this.tile.tank.getFluid();
            icrafting.sendProgressBarUpdate(this, 0, fluid == null ? 0 : fluid.amount);
            icrafting.sendProgressBarUpdate(this, 1, this.tile.getScaledAnalysingProgress(117));
            icrafting.sendProgressBarUpdate(this, 2, this.tile.getScaledFracturingProgress(117));
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
    	switch(par1) {
    		default: super.updateProgressBar(par1, par2); break;
    		case 0: this.fluidAmount = par2; break;
    		case 1: this.prog1 = par2; break;
    		case 2: this.prog2 = par2; break;
    	}
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
