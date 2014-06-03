package de.teamdna.mf.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.event.EntityHandler;

public class ItemMFBucket extends ItemBucket {

	public ItemMFBucket(Block fluidBlock) {
		super(fluidBlock);
		this.setContainerItem(Items.bucket);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int i, boolean b) {
		if(this == MineFracturing.INSTANCE.bucketFracFluid && entity instanceof EntityPlayer && !EntityHandler.isAcidResistant((EntityPlayer)entity)) {
			((EntityPlayer)entity).addPotionEffect(new PotionEffect(Potion.poison.id, 100));
			entity.attackEntityFrom(EntityHandler.fracing, 0.5F);
		}
	}

}
