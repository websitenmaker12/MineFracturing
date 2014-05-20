package de.teamdna.mf.tile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import de.teamdna.mf.api.CoreRegistry;

public class TileEntityGrindStone extends TileEntityCore {

	private int spinAmount = 0;
	private final int maxSpinAmount = 12;
	
	public TileEntityGrindStone() {
		this.inventory = new ItemStack[1];
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if(this.worldObj.isRemote) return;
		
		AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(this.xCoord, this.yCoord + 1D, this.zCoord, this.xCoord + 1D, this.yCoord + 2D, this.zCoord + 1D);
		List<Entity> items = this.worldObj.selectEntitiesWithinAABB(EntityItem.class, aabb, IEntitySelector.selectAnything);
		if(items.size() > 0) {
			ItemStack stack = ((EntityItem)items.get(0)).getEntityItem();
			if(Block.getBlockFromItem(stack.getItem()) != null && CoreRegistry.isOre(Block.getBlockFromItem(stack.getItem()))) {
				if(this.inventory[0] == null) {
					this.inventory[0] = stack;
					items.get(0).setDead();
				} else if(this.inventory[0].isItemEqual(stack) && this.inventory[0].stackSize < this.inventory[0].getMaxStackSize()) {
					int newSize = this.inventory[0].stackSize + stack.stackSize;
					if(newSize <= this.inventory[0].getMaxStackSize()) {
						this.inventory[0].stackSize = newSize;
						items.get(0).setDead();
					} else {
						this.inventory[0].stackSize = this.inventory[0].getMaxStackSize();
						stack.stackSize = newSize - this.inventory[0].getMaxStackSize();
					}
				}
			}
		}
	}

	public void spin() {
		if(this.inventory[0] == null) return;
		if(++this.spinAmount > this.maxSpinAmount) {
			this.spinAmount = 0;
			ItemStack stack = new ItemStack(CoreRegistry.getCondensedItem(Block.getBlockFromItem(this.inventory[0].getItem())).getItem(), this.inventory[0].stackSize);
			this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord + 0.5D, this.yCoord + 1.5D, this.zCoord + 0.5D, stack));
			this.inventory[0] = null;
		}
	}
	
}