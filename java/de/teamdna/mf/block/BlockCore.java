package de.teamdna.mf.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.teamdna.mf.tile.IExtractor;
import de.teamdna.mf.tile.IImporter;
import de.teamdna.mf.tile.PipeNetworkController;

public abstract class BlockCore extends BlockContainer {

	protected BlockCore(Material material) {
		super(material);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setStepSound(soundTypeMetal);
	}

	@Override
	public abstract TileEntity createNewTileEntity(World var1, int var2);

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if(tile != null && (tile instanceof IExtractor || tile instanceof IImporter)) {
        	PipeNetworkController.INSTNACE.handleBlockBreak(tile);
		}
		
		if(tile != null && tile instanceof IInventory) {
			IInventory inv = (IInventory)tile;
			
			for(int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if(stack != null) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                    
                    while(stack.stackSize > 0) {
                    	int j = world.rand.nextInt(21) + 10;
                    	if(j > stack.stackSize) j = stack.stackSize;
                    	
                    	stack.stackSize -= j;
                    	EntityItem entity = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(stack.getItem(), j, stack.getItemDamage()));
                    	if(stack.hasTagCompound()) entity.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
                    	
                    	float f3 = 0.05F;
                    	entity.motionX = (double)((float)world.rand.nextGaussian() * f3);
                    	entity.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
                    	entity.motionZ = (double)((float)world.rand.nextGaussian() * f3);
                    	world.spawnEntityInWorld(entity);
                    }
				}
			}
			
			world.func_147453_f(x, y, z, block);
		}
		
		super.breakBlock(world, x, y, z, block, meta);
    }

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile != null && (tile instanceof IExtractor || tile instanceof IImporter)) {
        	PipeNetworkController.INSTNACE.handleBlockAdd(tile);
		}
    }
	
}
