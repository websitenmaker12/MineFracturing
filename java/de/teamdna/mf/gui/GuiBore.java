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
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.util.RenderUtil;

public class GuiBore extends GuiContainer {

    private static final ResourceLocation guiBg = new ResourceLocation(Reference.modid, "textures/gui/guiBore.png");
    
    private TileEntityBore tile;
    private ContainerBore container;
    
	public GuiBore(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerBore(player, world, x, y, z));
		this.tile = (TileEntityBore)world.getTileEntity(x, y, z);
		this.container = (ContainerBore)this.inventorySlots;
		this.xSize = 242;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
		drawRect(47, 26, 46 + this.container.prog1, 40, 0x9F00DEFF);
		drawRect(47, 46, 46 + this.container.prog2, 60, 0x9F00DEFF);
		
		String s = StatCollector.translateToLocal("tile.bore.name");
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2 - 30, 8, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.bore.analyzingStatus") + " " + (int)((double)this.container.prog1 / 117D * 100D) + "%", 50, 30, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.bore.frackingStatus") + " " + (int)((double)this.container.prog2 / 117D * 100D) + "%", 50, 50, 4210752);
		
		// Combustion generator
	    this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.combustion"), 177, 18, 4210752);
	    this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.generator"), 177, 28, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
		
		drawRect(k + 8, l + 14, k + 150, l + 100, 0xFF8b8b8b);
		
		if(this.container.fluidAmount > 0) {
		   	Fluid fluid = FluidRegistry.getFluid(MineFracturing.INSTANCE.fracFluid.getID());
	 		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
	 		RenderUtil.setIntColor3(fluid.getColor());
			IIcon icon = fluid.getStillIcon();
			GL11.glEnable(GL11.GL_BLEND);
			this.drawTexturedModelRectFromIcon(k + 8, l + 14 + 65 - this.container.fluidAmount * 65 / this.tile.tank.getCapacity(), icon != null ? icon : fluid.getBlock().getIcon(0, 0), 24, 65);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
	 	GL11.glColor4f(1F, 1F, 1F, 1F);
	    this.mc.getTextureManager().bindTexture(this.guiBg);
	    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

	    // Combustion Generator
		this.mc.getTextureManager().bindTexture(guiBg);
		int i1 = this.container.burnTime * 12 / this.container.maxBurnTime;
		this.drawTexturedModalRect(k + 203, l + 50 + 12 - i1, 242, 12 - i1 + 1, 14, i1 + 2);
	}

}
