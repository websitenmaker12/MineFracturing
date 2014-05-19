package de.teamdna.mf.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.RenderBlockFluid;

import org.lwjgl.opengl.GL11;

import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.Reference;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.tile.TileEntityChemicalsMixer;
import de.teamdna.mf.tile.TileEntityCondenseChamber;
import de.teamdna.mf.tile.TileEntityExtractor;
import de.teamdna.mf.tile.TileEntityPressureTube;
import de.teamdna.mf.tile.TileEntityTraverse;
import de.teamdna.mf.util.RenderUtil;

public class RenderTiles extends TileEntitySpecialRenderer {

	public final ResourceLocation model_traverseLoc;
	public final ResourceLocation texture_traverseLoc;
	public final IModelCustom model_traverse;
	
	public final ResourceLocation model_traverseBoreLoc;
	public final ResourceLocation texture_traverseBoreLoc;
	public final IModelCustom model_traverseBore;
	
	public final ResourceLocation model_traverseBoreHeadLoc;
	public final ResourceLocation texture_traverseBoreHeadLoc;
	public final IModelCustom model_traverseBoreHead;
	
	public final ResourceLocation model_traverseExLoc;
	public final ResourceLocation texture_traverseExLoc;
	public final IModelCustom model_traverseEx;
	
	public final ResourceLocation model_mixerLoc;
	public final ResourceLocation texture_mixerLoc;
	public final IModelCustom model_mixer;
	
	public final ResourceLocation model_condenserLoc;
	public final ResourceLocation texture_condenserLoc;
	public final IModelCustom model_condenser;
	
	
	public final ResourceLocation model_pipe_01_inner_Loc;
	public final IModelCustom model_pipe_01_inner;
	
	public final ResourceLocation model_pipe_01_outer_Loc;
	public final IModelCustom model_pipe_01_outer;
	
	public final ResourceLocation model_pipe_02_inner_Loc;
	public final IModelCustom model_pipe_02_inner;
	
	public final ResourceLocation model_pipe_02_outer_Loc;
	public final IModelCustom model_pipe_02_outer;
	
	public final ResourceLocation ironTexture;
	
	public final ResourceLocation textureIron = new ResourceLocation("/textures/blocks/iron_block.png");
	
	public RenderTiles() {
		model_traverseLoc = new ResourceLocation(Reference.modid, "model/tower_traverse.obj");
		texture_traverseLoc = new ResourceLocation(Reference.modid, "model/texture/map_traverse.png");
		model_traverse = AdvancedModelLoader.loadModel(model_traverseLoc);
		
		model_traverseBoreLoc = new ResourceLocation(Reference.modid, "model/tower_bore.obj");
		texture_traverseBoreLoc = new ResourceLocation(Reference.modid, "model/texture/map_bore.png");
		model_traverseBore = AdvancedModelLoader.loadModel(model_traverseBoreLoc);
		
		model_traverseBoreHeadLoc = new ResourceLocation(Reference.modid, "model/tower_bore_head.obj");
		texture_traverseBoreHeadLoc = new ResourceLocation(Reference.modid, "model/texture/map_bore_head.png");
		model_traverseBoreHead = AdvancedModelLoader.loadModel(model_traverseBoreHeadLoc);

		model_traverseExLoc = new ResourceLocation(Reference.modid, "model/tower_extracting.obj");
		texture_traverseExLoc = new ResourceLocation(Reference.modid, "model/texture/map_extracting.png");
		model_traverseEx = AdvancedModelLoader.loadModel(model_traverseExLoc);
		
		model_condenserLoc = new ResourceLocation(Reference.modid, "model/condenser.obj");
		texture_condenserLoc = new ResourceLocation(Reference.modid, "model/texture/map_condenser.png");
		model_condenser = AdvancedModelLoader.loadModel(model_condenserLoc);
		
		model_mixerLoc = new ResourceLocation(Reference.modid, "model/mixer.obj");
		texture_mixerLoc = new ResourceLocation(Reference.modid, "model/texture/map_mixer.png");
		model_mixer = AdvancedModelLoader.loadModel(model_mixerLoc);
		
		model_pipe_01_inner_Loc = new ResourceLocation(Reference.modid, "model/pipe_01_inner.obj");
		model_pipe_01_inner = AdvancedModelLoader.loadModel(model_pipe_01_inner_Loc);
		
		model_pipe_01_outer_Loc = new ResourceLocation(Reference.modid, "model/pipe_01_outer.obj");
		model_pipe_01_outer = AdvancedModelLoader.loadModel(model_pipe_01_outer_Loc);
		
		model_pipe_02_inner_Loc = new ResourceLocation(Reference.modid, "model/pipe_02_inner.obj");
		model_pipe_02_inner = AdvancedModelLoader.loadModel(model_pipe_02_inner_Loc);
		
		model_pipe_02_outer_Loc = new ResourceLocation(Reference.modid, "model/pipe_02_outer.obj");
		model_pipe_02_outer = AdvancedModelLoader.loadModel(model_pipe_02_outer_Loc);
		
		ironTexture = new ResourceLocation("textures/blocks/iron_block.png");
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float var8) {
		
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		if (tile instanceof TileEntityTraverse) renderTileTraverse(x, y, z);
		else if (tile instanceof TileEntityBore) {
			renderTileBore(x, y, z, (TileEntityBore)tile);
		}
		else if (tile instanceof TileEntityExtractor) renderTileExtractor(x, y, z);
		else if (tile instanceof TileEntityPressureTube) renderTilePipe((TileEntityPressureTube)tile, x, y, z);
		else if (tile instanceof TileEntityChemicalsMixer) renderTileMixer(x, y, z);
		else if (tile instanceof TileEntityCondenseChamber) renderTileCondenser(x, y, z, (TileEntityCondenseChamber)tile);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void renderTileCondenser(double x, double y, double z, TileEntityCondenseChamber tile) {
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
		int l = tile.getBlockMetadata();
		GL11.glRotated(l == 3 ? -90F : l == 4 ? 180F : l == 2 ? 90F : 0F, 0, 1, 0);
		GL11.glTranslatef(0.1F, 0, 0);
		
		Fluid fluid = FluidRegistry.getFluid(MineFracturing.INSTANCE.liquidOre.getID());
 		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
 		IIcon icon = fluid.getFlowingIcon() != null ? fluid.getFlowingIcon() : fluid.getBlock().getIcon(0, 0);
 		
 		GL11.glPushMatrix();
 		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		
		GL11.glTranslatef(-0.5F, -0.35F, -0.4F);
		GL11.glScalef(0.8F, 0.8F, 0.8F);
 		RenderUtil.setIntColor3(fluid.getColor());
 		RenderUtil.renderIconCube(0, 0, 0, icon);
 		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_condenserLoc);
		model_condenser.renderAll();
		GL11.glPopMatrix();
	}
	
	private void renderTileMixer(double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_mixerLoc);
		model_mixer.renderAll();
		GL11.glPopMatrix();
	}
	
	private void renderTileTraverse(double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.9D, y+0.5, z+0.1D);
		GL11.glScaled(0.8D, 1.D, 0.8D);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseLoc);
		model_traverse.renderAll();
		GL11.glPopMatrix();
	}
	
	private void renderTileBore(double x, double y, double z, TileEntityBore tile) {
		// Block
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.9D, y+0.5D, z+0.1D);
		GL11.glScaled(0.8D, 1.D, 0.8D);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseBoreLoc);
		model_traverseBore.renderAll();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseLoc);
		model_traverse.renderAll();
		GL11.glPopMatrix();
		
		if(!tile.hasWorldObj()) return;
		
		// Bore Head
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.5, y - (tile.yCoord - tile.boreY) + 1, z+0.5);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseBoreHeadLoc);
		model_traverseBoreHead.renderAll();
		GL11.glPopMatrix();
		
		// Connection Pipe
		GL11.glPushMatrix();
		GL11.glColor4f(0.2F, 0.2F, 0.2F, 1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glTranslated(x, y - (tile.yCoord - tile.boreY) + 1, z);
		
		float height = tile.yCoord - tile.boreY;
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.4F, 0, 0.4F);
			GL11.glVertex3f(0.4F, height, 0.4F);
			GL11.glVertex3f(0.6F, height, 0.4F);
			GL11.glVertex3f(0.6F, 0, 0.4F);
			GL11.glVertex3f(0.4F, 0, 0.6F);
			GL11.glVertex3f(0.6F, 0, 0.6F);
			GL11.glVertex3f(0.6F, height, 0.6F);
			GL11.glVertex3f(0.4F, height, 0.6F);
			GL11.glVertex3f(0.4F, 0, 0.4F);
			GL11.glVertex3f(0.4F, 0, 0.6F);
			GL11.glVertex3f(0.4F, height, 0.6F);
			GL11.glVertex3f(0.4F, height, 0.4F);
			GL11.glVertex3f(0.6F, 0, 0.4F);
			GL11.glVertex3f(0.6F, height, 0.4F);
			GL11.glVertex3f(0.6F, height, 0.6F);
			GL11.glVertex3f(0.6F, 0, 0.6F);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		
		// Ropes
		GL11.glPushMatrix();
		GL11.glColor4f(0.1F, 0.1F, 0.1F, 1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glTranslated(x, y, z);
		
		if(tile.isMultiblockComplete()) {
			int centerOffset = 8;
			GL11.glBegin(GL11.GL_LINE_LOOP);
				GL11.glVertex3f(0.5F, 0F, 0.9F);
				GL11.glVertex3f(0.5F, tile.getWorldObj().getHeightValue(tile.xCoord, tile.zCoord + centerOffset) - tile.yCoord, centerOffset);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINE_LOOP);
				GL11.glVertex3f(0.5F, 0F, 0.1F);
				GL11.glVertex3f(0.5F, tile.getWorldObj().getHeightValue(tile.xCoord, tile.zCoord - centerOffset + 1) - tile.yCoord, -centerOffset + 1);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINE_LOOP);
				GL11.glVertex3f(0.9F, 0F, 0.5F);
				GL11.glVertex3f(centerOffset, tile.getWorldObj().getHeightValue(tile.xCoord + centerOffset, tile.zCoord) - tile.yCoord, 0.5F);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINE_LOOP);
				GL11.glVertex3f(0.1F, 0F, 0.5F);
				GL11.glVertex3f(-centerOffset + 1, tile.getWorldObj().getHeightValue(tile.xCoord - centerOffset + 1, tile.zCoord) - tile.yCoord, 0.5F);
			GL11.glEnd();
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	
	private void renderTileExtractor(double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.9D, y+0.5, z+0.1D);
		GL11.glScaled(0.8D, 1.D, 0.8D);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseExLoc);
		model_traverseEx.renderAll();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseLoc);
		model_traverse.renderAll();
		GL11.glPopMatrix();
	}
	
	public void renderTilePipe(TileEntityPressureTube tubeTile, double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.5, y+0.5, z+0.5D);
		GL11.glScaled(0.8D, 1.D, 0.8D);
		GL11.glScaled(0.75, 0.75, 0.75);
		GL11.glColor4f(0.5F, 0.5F, 0.5F, 1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		model_pipe_01_inner.renderAll();
		GL11.glColor4f(0.25F, 0.25F, 0.25F, 1);
		model_pipe_01_outer.renderAll();
		
		if (tubeTile.isConnectedToSide(ForgeDirection.EAST)) {
			GL11.glPushMatrix();
			GL11.glColor4f(0.5F, 0.5F, 0.5F, 1);
			model_pipe_02_inner.renderAll();
			GL11.glColor4f(0.25F, 0.25F, 0.25F, 1);
			model_pipe_02_outer.renderAll();
			GL11.glPopMatrix();
		}
		
		if (tubeTile.isConnectedToSide(ForgeDirection.SOUTH)) {
			GL11.glPushMatrix();
			GL11.glRotated(-90, 0, 1, 0);
			GL11.glColor4f(0.5F, 0.5F, 0.5F, 1);
			model_pipe_02_inner.renderAll();
			GL11.glColor4f(0.25F, 0.25F, 0.25F, 1);
			model_pipe_02_outer.renderAll();
			GL11.glPopMatrix();
		}
		
		if (tubeTile.isConnectedToSide(ForgeDirection.WEST)) {
			GL11.glPushMatrix();
			GL11.glRotated(180, 0, 1, 0);
			GL11.glColor4f(0.5F, 0.5F, 0.5F, 1);
			model_pipe_02_inner.renderAll();
			GL11.glColor4f(0.25F, 0.25F, 0.25F, 1);
			model_pipe_02_outer.renderAll();
			GL11.glPopMatrix();
		}
		
		if (tubeTile.isConnectedToSide(ForgeDirection.NORTH)) {
			GL11.glPushMatrix();
			GL11.glRotated(90, 0, 1, 0);
			GL11.glColor4f(0.5F, 0.5F, 0.5F, 1);
			model_pipe_02_inner.renderAll();
			GL11.glColor4f(0.25F, 0.25F, 0.25F, 1);
			model_pipe_02_outer.renderAll();
			GL11.glPopMatrix();
		}
		
		if (tubeTile.isConnectedToSide(ForgeDirection.UP)) {
			GL11.glPushMatrix();
			GL11.glTranslated(0, -0.25, 0);
			GL11.glRotated(90, 0, 0, 1);
			GL11.glColor4f(0.5F, 0.5F, 0.5F, 1);
			model_pipe_02_inner.renderAll();
			GL11.glColor4f(0.25F, 0.25F, 0.25F, 1);
			model_pipe_02_outer.renderAll();
			GL11.glPopMatrix();
		}
		
		if (tubeTile.isConnectedToSide(ForgeDirection.DOWN)) {
			GL11.glPushMatrix();
			//GL11.glTranslated(0, +0.25, 0);
			GL11.glRotated(-90, 0, 0, 1);
			GL11.glColor4f(0.5F, 0.5F, 0.5F, 1);
			model_pipe_02_inner.renderAll();
			GL11.glColor4f(0.25F, 0.25F, 0.25F, 1);
			model_pipe_02_outer.renderAll();
			GL11.glPopMatrix();
		}
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	
}