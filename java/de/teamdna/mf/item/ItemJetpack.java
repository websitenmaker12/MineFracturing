package de.teamdna.mf.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import de.teamdna.mf.Reference;

public class ItemJetpack extends ItemArmor {

	public static final ArmorMaterial material = EnumHelper.addArmorMaterial("jetpack", 0, new int[] { 0, 0, 0, 0 }, 0);
	
	public ItemJetpack() {
		super(material, 0, 1);
		this.setMaxStackSize(1);
		this.setMaxDamage(3400);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return Reference.modid + ":textures/items/jetpack.png";
    }

}
