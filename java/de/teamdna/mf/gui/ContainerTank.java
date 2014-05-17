package de.teamdna.mf.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.teamdna.mf.tile.TileEntityTank;

public class ContainerTank extends Container {

	private TileEntityTank tile;
	
	public int fluidID = -1;
	public int fluidAmount = 0;
	public int capacity = 0;
	
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

	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        FluidStack fluid = this.tile.tank.getFluid();
        par1ICrafting.sendProgressBarUpdate(this, 0, fluid == null ? -1 : fluid.fluidID);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.tile.tank.getFluidAmount());
        par1ICrafting.sendProgressBarUpdate(this, 2, this.tile.tank.getCapacity());
    }

	@Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            FluidStack fluid = this.tile.tank.getFluid();
            icrafting.sendProgressBarUpdate(this, 0, fluid == null ? -1 : fluid.fluidID);
            icrafting.sendProgressBarUpdate(this, 1, this.tile.tank.getFluidAmount());
            icrafting.sendProgressBarUpdate(this, 2, this.tile.tank.getCapacity());
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
    	switch(par1) {
    		default: super.updateProgressBar(par1, par2); break;
    		case 0: this.fluidID = par2; break;
    		case 1: this.fluidAmount = par2; break;
    		case 2: this.capacity = par2; break;
    	}
    }
    
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        return null;
    }
	
}
