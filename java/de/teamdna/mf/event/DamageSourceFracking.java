package de.teamdna.mf.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class DamageSourceFracking extends DamageSource {

	public DamageSourceFracking() {
		super("fracFluidDamage");
		this.setDifficultyScaled();
	}
	
	@Override
	public IChatComponent func_151519_b(EntityLivingBase entity) {
		if(entity instanceof EntityPlayer) return new ChatComponentText(((EntityPlayer)entity).getDisplayName() + " " + StatCollector.translateToLocal("damage.fracFluid"));
		else return null;
	}
}
