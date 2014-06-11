package de.teamdna.mf.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.teamdna.mf.Reference;
import de.teamdna.util.CoreUtil;
import de.teamdna.util.RenderUtil;

@SideOnly(Side.CLIENT)
public class RenderHandler {

	private IModelCustom modelJetpack = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.modid, "model/jetpack.obj"));
	private ResourceLocation texJetpack = new ResourceLocation(Reference.modid, "model/texture/map_jetpack.png");
	
	@SubscribeEvent
	public void renderLivingPostEvent(RenderLivingEvent.Specials.Post event) {
		if(!(event.entity instanceof EntityPlayer)) return;
		
		EntityPlayer player = (EntityPlayer)event.entity;
		ItemStack jetpack = player.inventory.armorInventory[2];
		
		if(jetpack != null) {
			GL11.glPushMatrix();
	        
			GL11.glRotated(-CoreUtil.getBodyRotation(player), 0, 1, 0);
			if(player.isSneaking()) GL11.glRotatef(27F, 1, 0, 0);
			GL11.glTranslatef(0.14F, -0.5F, -0.25F + (player.isSneaking() ? 0.08F : 0F));
			GL11.glScalef(0.6F, 0.6F, 0.6F);
			
			RenderUtil.clearColor();
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.texJetpack);
			this.modelJetpack.renderAll();
			
			GL11.glPopMatrix();
		}
	}
	
}
