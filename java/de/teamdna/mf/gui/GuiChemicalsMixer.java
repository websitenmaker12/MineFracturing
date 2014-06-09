package de.teamdna.mf.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.opengl.GL11;

import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.Reference;
import de.teamdna.mf.tile.TileEntityChemicalsMixer;
import de.teamdna.util.RenderUtil;

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

	    String s = StatCollector.translateToLocal("tile.chemicalsMixer.name");
	    this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s), 4, 4210752);
	    this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.combustion"), 177, 18, 4210752);
	    this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.generator"), 177, 28, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
	    
	    drawRect(k + 1, l + 10, k + 150, l + 100, 0xFF8b8b8b);
	    
	    if(container.capacity > 0) {
	    	Fluid fluid = FluidRegistry.getFluid(MineFracturing.INSTANCE.fracFluid.getID());
	 		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	 		RenderUtil.setIntColor3(fluid.getColor());
	 		IIcon icon = fluid.getStillIcon();
	 		GL11.glEnable(GL11.GL_BLEND);
	 		this.drawTexturedModelRectFromIcon(k + 112, l + 15 + 60 - container.fluidAmount * 60 / tile.tank.getCapacity(), icon != null ? icon : fluid.getBlock().getIcon(0, 0), 24, 60);
	 		GL11.glDisable(GL11.GL_BLEND);
	    }
	    
 		GL11.glColor4f(1F, 1F, 1F, 1F);
 		this.mc.getTextureManager().bindTexture(this.bg);
	    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
 		this.drawTexturedModalRect(k + 112, l + 13, 0, 182, 24, 65);
 		
 		GL11.glEnable(GL11.GL_BLEND);
        this.drawTexturedModalRect(k + 50, l + 36, 0, 166, this.container.idle * 49 / TileEntityChemicalsMixer.maxIdle, 16);
 		GL11.glDisable(GL11.GL_BLEND);
 		
 		// Combustion Generator
 		this.tile.burnTime = this.container.burnTime;
 		this.tile.maxBurnTime = this.container.maxBurnTime;
        int i1 = this.container.burnTime * 12 / this.container.maxBurnTime;
        this.drawTexturedModalRect(k + 203, l + 50 + 12 - i1, 242, 12 - i1 + 1, 14, i1 + 2);
	}

}
