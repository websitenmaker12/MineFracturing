package de.teamdna.mf.damagsource;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class DamageSourceFracking extends DamageSource{

	public DamageSourceFracking() {
		super("fracFluid");
	}@Override
	public IChatComponent func_151519_b(EntityLivingBase entity) {
		if(entity instanceof EntityPlayer) return new ChatComponentText(((EntityPlayer)entity).getDisplayName() + " was fragged by frag fluid");
		else return null;
	}
}
