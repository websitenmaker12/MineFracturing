package de.teamdna.mf.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import de.teamdna.mf.Reference;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.tile.TileEntityExtractor;
import de.teamdna.mf.tile.TileEntityPressureTube;
import de.teamdna.mf.tile.TileEntityTank;
import de.teamdna.mf.tile.TileEntityTraverse;

public class RenderTiles extends TileEntitySpecialRenderer {

	public static final ResourceLocation model_traverseLoc = new ResourceLocation(Reference.modid, "model/tower_traverse.obj");
	public static final ResourceLocation texture_traverseLoc = new ResourceLocation(Reference.modid, "model/texture/map_traverse.png");
	public static final IModelCustom model_traverse = AdvancedModelLoader.loadModel(model_traverseLoc);
	
	public static final ResourceLocation model_traverseBoreLoc = new ResourceLocation(Reference.modid, "model/tower_bore.obj");
	public static final ResourceLocation texture_traverseBoreLoc = new ResourceLocation(Reference.modid, "model/texture/map_bore.png");
	public static final IModelCustom model_traverseBore = AdvancedModelLoader.loadModel(model_traverseBoreLoc);
	
	public static final ResourceLocation model_traverseExLoc = new ResourceLocation(Reference.modid, "model/tower_extracting.obj");
	public static final ResourceLocation texture_traverseExLoc = new ResourceLocation(Reference.modid, "model/texture/map_extracting.png");
	public static final IModelCustom model_traverseEx = AdvancedModelLoader.loadModel(model_traverseExLoc);
	
	public static final ResourceLocation model_tank_base_01Loc = new ResourceLocation(Reference.modid, "model/tank_base_01.obj");
	public static final ResourceLocation texture_tank_base_01Loc = new ResourceLocation(Reference.modid, "model/texture/map_tank_base_01.png");
	public static final IModelCustom model_tank_base_01 = AdvancedModelLoader.loadModel(model_tank_base_01Loc);
	
	public static final ResourceLocation model_tank_base_02Loc = new ResourceLocation(Reference.modid, "model/tank_base_02.obj");
	public static final ResourceLocation texture_tank_base_02Loc = new ResourceLocation(Reference.modid, "model/texture/map_tank_base_02.png");
	public static final IModelCustom model_tank_base_02 = AdvancedModelLoader.loadModel(model_tank_base_02Loc);
	
	public static final ResourceLocation model_pipe_01_inner_Loc = new ResourceLocation(Reference.modid, "model/pipe_01_inner.obj");
	//public static final ResourceLocation texture_pipe_01_inner_Loc = new ResourceLocation(Reference.modid, "model/texture/map_extracting.png");
	public static final IModelCustom model_pipe_01_inner = AdvancedModelLoader.loadModel(model_pipe_01_inner_Loc);
	
	public static final ResourceLocation model_pipe_01_outer_Loc = new ResourceLocation(Reference.modid, "model/pipe_01_outer.obj");
	public static final IModelCustom model_pipe_01_outer = AdvancedModelLoader.loadModel(model_pipe_01_outer_Loc);
	
	public static final ResourceLocation model_pipe_02_inner_Loc = new ResourceLocation(Reference.modid, "model/pipe_02_inner.obj");
	public static final IModelCustom model_pipe_02_inner = AdvancedModelLoader.loadModel(model_pipe_02_inner_Loc);
	
	public static final ResourceLocation model_pipe_02_outer_Loc = new ResourceLocation(Reference.modid, "model/pipe_02_outer.obj");
	public static final IModelCustom model_pipe_02_outer = AdvancedModelLoader.loadModel(model_pipe_02_outer_Loc);
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float var8) {
		if (tile instanceof TileEntityTraverse) renderTileTraverse(x, y, z);
		else if (tile instanceof TileEntityBore) renderTileBore(x, y, z);
		else if (tile instanceof TileEntityExtractor) renderTileEx(x, y, z);
		else if (tile instanceof TileEntityPressureTube) renderTilePipe((TileEntityPressureTube)tile, x, y, z, false);
		else if (tile instanceof TileEntityTank) renderTileTankBase(x, y, z);
	}
	
	private void renderTileTraverse(double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.9D, y+0.5, z+0.1D);
		GL11.glScaled(0.8D, 1.D, 0.8D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseLoc);
		model_traverse.renderAll();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	private void renderTileBore(double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.9D, y+0.5, z+0.1D);
		GL11.glScaled(0.8D, 1.D, 0.8D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseBoreLoc);
		model_traverseBore.renderAll();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseLoc);
		model_traverse.renderAll();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	private void renderTileEx(double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.9D, y+0.5, z+0.1D);
		GL11.glScaled(0.8D, 1.D, 0.8D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseExLoc);
		model_traverseEx.renderAll();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_traverseLoc);
		model_traverse.renderAll();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public void renderTilePipe(TileEntityPressureTube tubeTile, double x, double y, double z, boolean isInInv) {
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.5, y+0.5, z+0.5D);
		GL11.glScaled(0.8D, 1.D, 0.8D);
		GL11.glScaled(0.75, 0.75, 0.75);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D); //TODO: REMOVE!!!
		GL11.glColor4f(0.5F, 0.5F, 0.5F, 1);
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
		
		if (tubeTile.isConnectedToSide(ForgeDirection.NORTH) || isInInv) {
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
		
		if (tubeTile.isConnectedToSide(ForgeDirection.DOWN) || isInInv) {
			GL11.glPushMatrix();
			//GL11.glTranslated(0, +0.25, 0);
			GL11.glRotated(-90, 0, 0, 1);
			GL11.glColor4f(0.5F, 0.5F, 0.5F, 1);
			model_pipe_02_inner.renderAll();
			GL11.glColor4f(0.25F, 0.25F, 0.25F, 1);
			model_pipe_02_outer.renderAll();
			GL11.glPopMatrix();
		}
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	
	private void renderTileTankBase(double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.9D, y+0.5, z+0.1D);
		GL11.glScaled(0.8D, 1.D, 0.8D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_tank_base_01Loc);
		model_tank_base_01.renderAll();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture_tank_base_02Loc);
		model_tank_base_02.renderAll();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}