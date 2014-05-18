package de.teamdna.mf.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.teamdna.mf.tile.TileEntityCondenseChamber;

public class ContainerCondenseChamber extends Container {

	public TileEntityCondenseChamber tile;
	
	public int fluidAmount = 0;
	public int capacity = 0;
	public int idle = 0;
	public int maxIdle = TileEntityCondenseChamber.maxIdle;
	
	public ContainerCondenseChamber(EntityPlayer player, World world, int x, int y, int z) {
		this.tile = (TileEntityCondenseChamber)world.getTileEntity(x, y, z);
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				this.addSlotToContainer(new Slot(this.tile, j + i * 3, 107 + j * 18, 18 + i * 18)); // TODO: output slot
			}
		}
		
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
		// TODO
		return true;
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        FluidStack fluid = this.tile.tank.getFluid();
        par1ICrafting.sendProgressBarUpdate(this, 0, this.tile.tank.getFluidAmount());
        par1ICrafting.sendProgressBarUpdate(this, 1, this.tile.tank.getCapacity());
        par1ICrafting.sendProgressBarUpdate(this, 2, this.tile.idle);
    }

	@Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            FluidStack fluid = this.tile.tank.getFluid();
            icrafting.sendProgressBarUpdate(this, 0, this.tile.tank.getFluidAmount());
            icrafting.sendProgressBarUpdate(this, 1, this.tile.tank.getCapacity());
            icrafting.sendProgressBarUpdate(this, 2, this.tile.idle);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
    	switch(par1) {
    		default: super.updateProgressBar(par1, par2); break;
    		case 0: this.fluidAmount = par2; break;
    		case 1: this.capacity = par2; break;
    		case 2: this.idle = par2; break;
    	}
    }

}
