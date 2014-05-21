package de.teamdna.mf.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderHandler {

	@SubscribeEvent
	public void renderLivingPostEvent(RenderLivingEvent.Specials.Post event) {
		if(!(event.entity instanceof EntityPlayer)) return;
		
		EntityPlayer player = (EntityPlayer)event.entity;
		ItemStack jetpack = player.inventory.armorInventory[2];
		
		if(jetpack != null) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_CULL_FACE);
	        
//	        float w = 0.8F;
//			
//			GL11.glRotated(-Util.getBodyRotation(player) + 90F, 0, 1, 0);
//			GL11.glTranslatef(0.3F + (player instanceof EntityPlayer && ((EntityPlayer)player).isSneaking() ? 0.4F : 0),
//					-1.27F + (player instanceof EntityPlayer && ((EntityPlayer)player).isSneaking() ? 0.2F : 0), -0.5F);
//			GL11.glRotatef(25F + (player instanceof EntityPlayer && ((EntityPlayer)player).isSneaking() ? 18F : 0), 0, 0, 1);
//			
//			Tessellator tessellator = Tessellator.instance;
//			tessellator.startDrawingQuads();
//			tessellator.setColorRGBA(255, 255, 255, 64);
//			
//			tessellator.addVertexWithUV(0, 0.45F, 0.45F, 0, 0);
//			tessellator.addVertexWithUV(0, 0.55F, 0.45F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.55F, 0.45F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.45F, 0.45F, 0, 0);
//			
//			tessellator.addVertexWithUV(0, 0.45F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(0, 0.55F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.55F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.45F, 0.55F, 0, 0);
//			
//			tessellator.addVertexWithUV(0, 0.45F, 0.45F, 0, 0);
//			tessellator.addVertexWithUV(0, 0.45F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(0, 0.55F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(0, 0.55F, 0.45F, 0, 0);
//			
//			tessellator.addVertexWithUV(w, 0.45F, 0.45F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.45F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.55F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.55F, 0.45F, 0, 0);
//			
//			tessellator.addVertexWithUV(0, 0.45F, 0.45F, 0, 0);
//			tessellator.addVertexWithUV(0, 0.45F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.45F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.45F, 0.45F, 0, 0);
//			
//			tessellator.addVertexWithUV(0, 0.55F, 0.45F, 0, 0);
//			tessellator.addVertexWithUV(0, 0.55F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.55F, 0.55F, 0, 0);
//			tessellator.addVertexWithUV(w, 0.55F, 0.45F, 0, 0);
//			
//			tessellator.draw();
	        
	        GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}
	
}
