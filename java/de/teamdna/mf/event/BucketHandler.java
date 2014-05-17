package de.teamdna.mf.event;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BucketHandler {

	public static BucketHandler INSTANCE = new BucketHandler();
	private Map<Block, Item> buckets = new HashMap<Block, Item>();
	
	@SubscribeEvent
	public void bucketFillEvent(FillBucketEvent event) {
		Block block = event.world.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);
		Item bucket = buckets.get(block);
		if(bucket != null && event.world.getBlockMetadata(event.target.blockX, event.target.blockY, event.target.blockZ) == 0) {
			event.world.setBlockToAir(event.target.blockX, event.target.blockY, event.target.blockZ);
		} else {
			return;
		}
		
		event.result = new ItemStack(bucket);
		event.setResult(Result.ALLOW);
	}

	public void register(Block fluid, Item bucket) {
		this.buckets.put(fluid, bucket);
	}
	
}
