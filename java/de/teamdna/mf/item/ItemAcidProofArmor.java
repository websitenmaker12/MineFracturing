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
		
		switch(type) {
			case 0: this.setTextureName(Reference.modid + ":acidProof_helmet"); break;
			case 1: this.setTextureName(Reference.modid + ":acidProof_chestplate"); break;
			case 2: this.setTextureName(Reference.modid + ":acidProof_leggins"); break;
			case 3: this.setTextureName(Reference.modid + ":acidProof_boots"); break;
		}
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return Reference.modid + ":textures/armor/acidProof.png";
    }

}
