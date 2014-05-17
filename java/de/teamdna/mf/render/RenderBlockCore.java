package de.teamdna.mf.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.Reference;
import de.teamdna.mf.net.ClientProxy;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.tile.TileEntityExtractor;
import de.teamdna.mf.tile.TileEntityPressureTube;
import de.teamdna.mf.tile.TileEntityTank;
import de.teamdna.mf.tile.TileEntityTraverse;

public class RenderBlockCore implements ISimpleBlockRenderingHandler {

	public TileEntityTraverse dummyTraverse = new TileEntityTraverse();
	public TileEntityBore dummyBore = new TileEntityBore();
	public TileEntityExtractor dummyExtractor = new TileEntityExtractor();
	public TileEntityPressureTube dummyPipe = new TileEntityPressureTube();
	public TileEntityTank dummyTank = new TileEntityTank();
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glTranslated(-0.5, -0.5, -0.5);
		if(block == MineFracturing.INSTANCE.traverse) TileEntityRendererDispatcher.instance.renderTileEntityAt(this.dummyTraverse, 0, 0, 0, 0);
		else if (block == MineFracturing.INSTANCE.bore) TileEntityRendererDispatcher.instance.renderTileEntityAt(this.dummyBore, 0, 0, 0, 0);
		else if (block == MineFracturing.INSTANCE.extractor) TileEntityRendererDispatcher.instance.renderTileEntityAt(this.dummyExtractor, 0, 0, 0, 0);
		else if (block == MineFracturing.INSTANCE.pressureTube) TileEntityRendererDispatcher.instance.renderTileEntityAt(dummyPipe, 0, 0, 0, 0);
		else if (block == MineFracturing.INSTANCE.tankBase) TileEntityRendererDispatcher.instance.renderTileEntityAt(dummyTank, 0, 0, 0, 0);
		
		GL11.glPopMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.coreRenderID;
	}

}
