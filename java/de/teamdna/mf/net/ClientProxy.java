package de.teamdna.mf.net;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import de.teamdna.mf.render.RenderBlockCore;
import de.teamdna.mf.render.RenderTiles;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.tile.TileEntityChemicalsMixer;
import de.teamdna.mf.tile.TileEntityCondenseChamber;
import de.teamdna.mf.tile.TileEntityExtractor;
import de.teamdna.mf.tile.TileEntityPressureTube;
import de.teamdna.mf.tile.TileEntityTraverse;

public class ClientProxy extends CommonProxy {

	public static int coreRenderID;
	
	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public void registerRenderer() {
		coreRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(coreRenderID, new RenderBlockCore());
		
		RenderTiles stdTileRenderer = new RenderTiles();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTraverse.class, stdTileRenderer);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBore.class, stdTileRenderer);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExtractor.class, stdTileRenderer);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPressureTube.class, stdTileRenderer);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalsMixer.class, stdTileRenderer);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCondenseChamber.class, stdTileRenderer);
	}

}
