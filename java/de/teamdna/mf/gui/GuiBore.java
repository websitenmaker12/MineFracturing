package de.teamdna.mf.gui;

import org.lwjgl.opengl.GL11;

import de.teamdna.mf.Reference;
import de.teamdna.mf.tile.TileEntityBore;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiBore extends GuiContainer{

    private static final ResourceLocation guiBg = new ResourceLocation(Reference.modid, "textures/gui/guiBore.png");
    
    private TileEntityBore tile;
    
	public GuiBore(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerTank(player, world, x, y, z));
		tile = (TileEntityBore) world.getTileEntity(x, y, z);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
		this.fontRendererObj.drawString("Fracturating Bore", 8, 5, 4210752);
		this.fontRendererObj.drawString("Analyzing status " + tile.getScaledAnalysingProgress(100) + "%", 40, 20, 4210752);
		this.fontRendererObj.drawString("Fracking status " + tile.getScaledFracturingProgress(100) + "%", 40, 32, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		 GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	     this.mc.getTextureManager().bindTexture(guiBg);
	     int k = (this.width - this.xSize) / 2;
	     int l = (this.height - this.ySize) / 2;
	     this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

}
