package de.teamdna.mf.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import de.teamdna.mf.Reference;

public class ItemAcidProofArmor extends ItemArmor {

	public static final ArmorMaterial material = EnumHelper.addArmorMaterial("acidProof", 0, new int[] { 0, 0, 0, 0 }, 0);

	public ItemAcidProofArmor(int type) {
		super(material, 0, type);
		this.setMaxStackSize(1);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return Reference.modid + ":textures/items/jetpack.png";
    }

}
