package de.teamdna.mf.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.item.ItemAcidProofArmor;
import de.teamdna.util.CoreUtil;

public class EntityHandler {
	
	public static final DamageSource fracing = new DamageSourceFracking();
	
	@SubscribeEvent
	public void livingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase e = event.entityLiving;
		if(e instanceof EntityPlayer && isAcidResistant((EntityPlayer)e)) return;
		
		if(e.worldObj.getBiomeGenForCoords(MathHelper.floor_double(e.posX), MathHelper.floor_double(e.posZ)).biomeID == MineFracturing.INSTANCE.infestedBiome.biomeID) {
			e.addPotionEffect(new PotionEffect(Potion.poison.id, 500));
			e.attackEntityFrom(fracing, 0.5F);
		}
	}
	
	@SubscribeEvent
	public void tickEvent(PlayerTickEvent event) {
		EntityPlayer p = event.player;

		ItemStack jetpack = p.inventory.armorInventory[2];
		if(jetpack != null && jetpack.getItem() == MineFracturing.INSTANCE.jetpack && jetpack.getItemDamage() < jetpack.getMaxDamage() - 2 && MineFracturing.keys.isJumpKeyDown(p)) {
			if(p.motionY > 0) p.motionY += 0.086999999105930327D;
            else p.motionY += 0.11699999910593033D;
			if(p.motionY < 0) p.motionY /= 1.1499999761581421D;
            if(!p.onGround) {
            	p.motionX *= 1.0399999618530273D;
            	p.motionZ *= 1.0399999618530273D;
            }

            Vec3 vec = p.getLookVec();
            p.worldObj.spawnParticle("explode", p.posX - vec.xCoord * 1.2D, p.posY - 1.4D, p.posZ - vec.zCoord * 1.2D, 0, -1, 0);
            p.fallDistance = 0F;
            p.distanceWalkedModified = 0F;
            CoreUtil.resetPlayerFlyTicks(p);
            
            jetpack.damageItem(1, p);
		}
	}
	
	public static boolean wearsAcidProofSuite(EntityPlayer player) {
		int founds = 0;
		for(int i = 0; i < 4; i++) if(player.inventory.armorInventory[i] != null && player.inventory.armorInventory[i].getItem() instanceof ItemAcidProofArmor) founds++;
		return founds == 4;
	}
	
	public static boolean isAcidResistant(EntityPlayer player) {
		return wearsAcidProofSuite(player) || player.capabilities.isCreativeMode;
	}
	
}
