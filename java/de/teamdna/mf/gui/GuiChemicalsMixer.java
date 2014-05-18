package de.teamdna.mf.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.Reference;
import de.teamdna.mf.tile.TileEntityChemicalsMixer;
import de.teamdna.mf.tile.TileEntityTank;
import de.teamdna.mf.util.RenderUtil;

public class GuiChemicalsMixer extends GuiContainer {

	public ResourceLocation bg = new ResourceLocation(Reference.modid, "/textures/gui/chemicalsMixer.png");
	
	private TileEntityChemicalsMixer tile;
	private ContainerChemicalsMixer container;
	
	public GuiChemicalsMixer(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerChemicalsMixer(player, world, x, y, z));
		container = (ContainerChemicalsMixer)this.inventorySlots;
		tile = (TileEntityChemicalsMixer) world.getTileEntity(x, y, z);
		this.xSize = 242;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
	    
	    this.fontRendererObj.drawString("Combustion", 177, 18, 4210752);
	    this.fontRendererObj.drawString("Generator", 177, 28, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
	    
	    drawRect(k + 1, l + 10, k + 150, l + 100, 0xFF8b8b8b);
	    
	    if (container.capacity > 0) {
	    	 Fluid fluid = FluidRegistry.getFluid(MineFracturing.INSTANCE.fracFluid.getID());
	 		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	 		RenderUtil.setIntColor3(fluid.getColor());
	 		IIcon icon = fluid.getStillIcon();
	 		GL11.glEnable(GL11.GL_BLEND);
	 		this.drawTexturedModelRectFromIcon(k + 112, l + 15, icon != null ? icon : fluid.getBlock().getIcon(0, 0), 24, 60);
	 		GL11.glDisable(GL11.GL_BLEND);
	    }
 		GL11.glColor4f(1F, 1F, 1F, 1F);
	    this.mc.getTextureManager().bindTexture(bg);
	    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	    
 		this.mc.getTextureManager().bindTexture(bg);
 		drawTexturedModalRect(k + 112, l + 13, 0, 182, 24, 65);
 		
 		GL11.glEnable(GL11.GL_BLEND);
 		this.mc.getTextureManager().bindTexture(bg);
        this.drawTexturedModalRect(k + 50, l + 36, 0, 166, tile.getWorkProgressScaled(49) + 1, 16);
 		GL11.glDisable(GL11.GL_BLEND);
 		
 		//Combustion Generator
 		this.mc.getTextureManager().bindTexture(bg);
        int i1 = this.tile.getBurnTimeScaled(12);
        this.drawTexturedModalRect(k + 203, l + 50 + 12 - i1, 242, 12 - i1 + 1, 14, i1 + 2);
	}

}
