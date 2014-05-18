package de.teamdna.mf.gui;

import java.util.ArrayList;
import java.util.List;

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

import de.teamdna.mf.Reference;
import de.teamdna.mf.tile.TileEntityTank;
import de.teamdna.mf.util.RenderUtil;

public class GuiTank extends GuiContainer {

	private static final ResourceLocation guiBg = new ResourceLocation(Reference.modid, "textures/gui/guiTank.png");
	
	private TileEntityTank tile;
	private ContainerTank container;
	
	public GuiTank(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerTank(player, world, x, y, z));
		this.tile = (TileEntityTank)world.getTileEntity(x, y, z);
		this.container = (ContainerTank)this.inventorySlots;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
	     
		String s = StatCollector.translateToLocal("tile.tankController.name");
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 4, 4210752);
		
		// Overlay
		this.mc.getTextureManager().bindTexture(guiBg);
		this.drawTexturedModalRect(12, 13, 0, 166, 124, 65);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		 GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	     int k = (this.width - this.xSize) / 2;
	     int l = (this.height - this.ySize) / 2;
	     drawRect(k + 1, l + 10, k + 150, l + 100, 0xFF8b8b8b);
	     GL11.glColor4f(1F, 1F, 1F, 1F);
	     GL11.glColor4f(1F, 1F, 1F, 1F);
	     //Liquid
		if (this.container.fluidID != -1) {
			Fluid fluid = FluidRegistry.getFluid(this.container.fluidID);
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			RenderUtil.setIntColor3(fluid.getColor());
			IIcon icon = fluid.getStillIcon();
			GL11.glEnable(GL11.GL_BLEND);
			this.drawTexturedModelRectFromIcon(k + 12, l + 78 - (this.container.fluidAmount * 65 / this.container.capacity), icon != null ? icon : fluid.getBlock().getIcon(0, 0), 124, 65);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
	     this.mc.getTextureManager().bindTexture(guiBg);
	     this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
		
		Fluid fluid = null;
		boolean flag = this.container.fluidID != -1 && (fluid = FluidRegistry.getFluid(this.container.fluidID)) != null;
		if(par1 >= k + 12 && par1 <= k + 124 && par2 >= l + 13 && par2 <= l + 65) {
			List<String> list = new ArrayList<String>();
			if(flag) {
				list.add(StatCollector.translateToLocal(fluid.getUnlocalizedName()));
				list.add(this.container.fluidAmount + "/" + this.container.capacity + " mB");
			} else {
				list.add("0/" + this.container.capacity + " mB");
			}
			this.drawHoveringText(list, par1, par2, this.fontRendererObj);
		}
	}

}
