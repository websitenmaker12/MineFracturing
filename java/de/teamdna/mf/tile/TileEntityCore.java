package de.teamdna.mf.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityCore extends TileEntity implements IInventory {

	protected ItemStack[] inventory = new ItemStack[0];
	
	@Override
	public int getBlockMetadata() {
		if(!this.hasWorldObj()) return 0;
		else return super.getBlockMetadata();
    }
	
	@Override
	public Block getBlockType() {
		if(!this.hasWorldObj()) return null;
		else return super.getBlockType();
    }
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1) {
		return this.inventory[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if(this.inventory[par1] != null) {
            ItemStack itemstack;

            if(this.inventory[par1].stackSize <= par2) {
                itemstack = this.inventory[par1];
                this.inventory[par1] = null;
                return itemstack;
            } else {
                itemstack = this.inventory[par1].splitStack(par2);

                if(this.inventory[par1].stackSize == 0) {
                    this.inventory[par1] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if(this.inventory[par1] != null) {
            ItemStack itemstack = this.inventory[par1];
            this.inventory[par1] = null;
            return itemstack;
        } else {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2) {
		this.inventory[par1] = par2;
        if(par2 != null && par2.stackSize > this.getInventoryStackLimit()) {
        	par2.stackSize = this.getInventoryStackLimit();
        }
	}

	@Override
	public String getInventoryName() {
		return "inv.core";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {  }

	@Override
	public void closeInventory() {  }

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        
        NBTTagList nbttaglist = tag.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if(b0 >= 0 && b0 < this.inventory.length) {
                this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

	@Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < this.inventory.length; i++) {
            if(this.inventory[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        tag.setTag("Items", nbttaglist);
    }
	
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }
	
}
