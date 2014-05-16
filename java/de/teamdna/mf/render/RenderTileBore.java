package de.teamdna.mf.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import de.teamdna.mf.tile.TileEntityBore;

public class RenderTileBore extends TileEntitySpecialRenderer {

	private void renderTileBore(TileEntityBore tile, double x, double y, double z, float var8) {
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float var8) {
		this.renderTileBore((TileEntityBore)tile, x, y, z, var8);
	}

}
