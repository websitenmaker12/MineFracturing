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
import de.teamdna.mf.tile.TileEntityCondenseChamber;
import de.teamdna.util.RenderUtil;

public class GuiCondenseChamber extends GuiContainer {

	private ResourceLocation gui = new ResourceLocation(Reference.modid, "textures/gui/condenseChamber.png");
	
	public TileEntityCondenseChamber tile;
	public ContainerCondenseChamber container;
	
	public GuiCondenseChamber(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerCondenseChamber(player, world, x, y, z));
		this.tile = (TileEntityCondenseChamber)world.getTileEntity(x, y, z);
		this.container = (ContainerCondenseChamber)this.inventorySlots;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
	    
	    String s = StatCollector.translateToLocal("tile.condenseChamber.name");
	    this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2 + 5, 6, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
	    
	    drawRect(k + 13, l + 14, k + 150, l + 100, 0xFF8b8b8b);
	    
	    if(container.capacity > 0) {
	    	Fluid fluid = FluidRegistry.getFluid(MineFracturing.INSTANCE.liquidOre.getID());
	 		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	 		RenderUtil.setIntColor3(fluid.getColor());
	 		IIcon icon = fluid.getStillIcon();
	 		GL11.glEnable(GL11.GL_BLEND);
	 		this.drawTexturedModelRectFromIcon(k + 13, l + 14 + 65 - this.container.fluidAmount * 65 / this.container.capacity, icon != null ? icon : fluid.getBlock().getIcon(0, 0), 24, 65);
	 		GL11.glDisable(GL11.GL_BLEND);
	    }
	    
 		GL11.glColor4f(1F, 1F, 1F, 1F);
	    this.mc.getTextureManager().bindTexture(this.gui);
	    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
 		this.drawTexturedModalRect(k + 13, l + 14, 176, 26, 24, 65);
 		
 		GL11.glEnable(GL11.GL_BLEND);
 		this.drawTexturedModalRect(k + 56, l + 36, 176, 0, this.container.idle * 37 / this.container.maxIdle, 15);
 		GL11.glDisable(GL11.GL_BLEND);
	}

}
