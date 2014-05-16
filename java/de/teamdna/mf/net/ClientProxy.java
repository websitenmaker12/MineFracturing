package de.teamdna.mf.net;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import de.teamdna.mf.render.RenderBlockCore;
import de.teamdna.mf.render.RenderTiles;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.tile.TileEntityExtractor;
import de.teamdna.mf.tile.TileEntityTraverse;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

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
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTraverse.class, new RenderTiles());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBore.class, new RenderTiles());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExtractor.class, new RenderTiles());
	}

}
