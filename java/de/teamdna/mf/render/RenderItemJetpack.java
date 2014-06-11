package de.teamdna.mf.render;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import de.teamdna.mf.Reference;
import de.teamdna.util.RenderUtil;

public class RenderItemJetpack implements IItemRenderer {

	private IModelCustom modelJetpack = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.modid, "model/jetpack.obj"));
	private ResourceLocation texJetpack = new ResourceLocation(Reference.modid, "model/texture/map_jetpack.png");
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type != ItemRenderType.FIRST_PERSON_MAP;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		
		if(type == ItemRenderType.INVENTORY) {
			GL11.glTranslatef(0.18F, 0, 0);
		} else if(type == ItemRenderType.ENTITY) {
			GL11.glTranslatef(0.25F, 0.45F, 0);
		} else if(type == ItemRenderType.EQUIPPED) {
			GL11.glTranslatef(0.8F, 0.5F, 0.8F);
		} else if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			GL11.glTranslatef(0, 0.7F, 0.6F);
		}
		
		RenderUtil.clearColor();
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.texJetpack);
		this.modelJetpack.renderAll();
		
		GL11.glPopMatrix();
	}

}
