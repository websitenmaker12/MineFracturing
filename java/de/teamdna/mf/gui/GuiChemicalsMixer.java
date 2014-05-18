package de.teamdna.mf.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import de.teamdna.mf.Reference;
import de.teamdna.mf.tile.TileEntityChemicalsMixer;
import de.teamdna.mf.tile.TileEntityTank;

public class GuiChemicalsMixer extends GuiContainer {

	public ResourceLocation bg = new ResourceLocation(Reference.modid, "/textures/gui/chemicalsMixer.png");
	
	private TileEntityChemicalsMixer tile;
	private ContainerChemicalsMixer container;
	
	public GuiChemicalsMixer(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerChemicalsMixer(player, world, x, y, z));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
	    
	    drawRect(k + 1, l + 10, k + 150, l + 100, 0xFF8b8b8b);
	    
	    GL11.glColor4f(1F, 1F, 1F, 1F);
	    this.mc.getTextureManager().bindTexture(bg);
	    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

}
