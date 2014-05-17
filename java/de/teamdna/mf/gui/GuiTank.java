package de.teamdna.mf.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import de.teamdna.mf.Reference;
import de.teamdna.mf.tile.TileEntityTank;

public class GuiTank extends GuiContainer {

	private static final ResourceLocation guiBg = new ResourceLocation(Reference.modid, "textures/gui/guiTank.png");
	
	private TileEntityTank tile;
	
	public GuiTank(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerTank(player, world, x, y, z));
		this.tile = (TileEntityTank)world.getTileEntity(x, y, z);
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
	    
	    String s = StatCollector.translateToLocal("tile.tankController.name");
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 4, 4210752);
		
		// Draw fluid
//		System.out.println(FluidRegistry.getFluid(this.tile.tank.getFluid().fluidID).getFlowingIcon());
		
		// Overlay
		this.mc.getTextureManager().bindTexture(guiBg);
		this.drawTexturedModalRect(12, 13, 0, 166, 124, 65);
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
