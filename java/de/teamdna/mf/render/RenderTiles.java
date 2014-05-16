package de.teamdna.mf.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import de.teamdna.mf.Reference;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.tile.TileEntityTraverse;

public class RenderTiles extends TileEntitySpecialRenderer {

	public static final ResourceLocation model_traverseLoc = new ResourceLocation(Reference.modid, "model/tower_traverse.obj");
	public static final ResourceLocation texture_traverseLoc = new ResourceLocation(Reference.modid, "model/texture/map_traverse.png");
	public static final IModelCustom model_traverse = AdvancedModelLoader.loadModel(model_traverseLoc);
	
	public static final ResourceLocation model_traverseBoreLoc = new ResourceLocation(Reference.modid, "model/tower_bore.obj");
	public static final ResourceLocation texture_traverseBoreLoc = new ResourceLocation(Reference.modid, "model/texture/map_bore.png");
	public static final IModelCustom model_traverseBore = AdvancedModelLoader.loadModel(model_traverseBoreLoc);
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float var8) {
		if(tile instanceof TileEntityTraverse) renderTileTraverse(x, y, z);
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
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

}
