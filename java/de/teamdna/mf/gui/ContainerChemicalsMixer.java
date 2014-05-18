package de.teamdna.mf.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.teamdna.mf.gui.slot.OutputSlot;
import de.teamdna.mf.tile.TileEntityChemicalsMixer;
import de.teamdna.mf.tile.TileEntityTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class ContainerChemicalsMixer extends Container {

	private TileEntityChemicalsMixer tile;
	
	public int fluidAmount = 0;
	public int capacity = 0;
	
	public ContainerChemicalsMixer(EntityPlayer player, World world, int x, int y, int z) {
		this.tile = (TileEntityChemicalsMixer)world.getTileEntity(x, y, z);
		
		this.addSlotToContainer(new Slot(this.tile, 0, 25, 16));
		this.addSlotToContainer(new Slot(this.tile, 1, 25, 36));
		this.addSlotToContainer(new Slot(this.tile, 2, 25, 56));
		this.addSlotToContainer(new Slot(this.tile, 3, 176, 48));
		
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
        par1ICrafting.sendProgressBarUpdate(this, 0, this.tile.tank.getFluidAmount());
        par1ICrafting.sendProgressBarUpdate(this, 1, this.tile.tank.getCapacity());
    }

	@Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            FluidStack fluid = this.tile.tank.getFluid();
            icrafting.sendProgressBarUpdate(this, 0, this.tile.tank.getFluidAmount());
            icrafting.sendProgressBarUpdate(this, 1, this.tile.tank.getCapacity());
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
    	switch(par1) {
    		default: super.updateProgressBar(par1, par2); break;
    		case 0: this.fluidAmount = par2; break;
    		case 1: this.capacity = par2; break;
    	}
    }
    
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
    	ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(slotID == 0) {
                if(!this.mergeItemStack(itemstack1, 1, 37, true)) return null;
            } else {
                this.mergeItemStack(itemstack1, 3, 4, false);
            }

            if(itemstack1.stackSize == 0) slot.putStack((ItemStack)null);
            else slot.onSlotChanged();

            if(itemstack1.stackSize == itemstack.stackSize) return null;
            slot.onPickupFromSlot(player, itemstack1);
        }
        return itemstack;
    }
}
