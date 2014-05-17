package de.teamdna.mf.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
		
		// Draw fluid
		Fluid fluid = null;
		if(this.container.fluidID != -1) {
			fluid = FluidRegistry.getFluid(this.container.fluidID);
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			RenderUtil.setIntColor3(fluid.getColor());
			this.drawTexturedModelRectFromIcon(12, 13, fluid.getStillIcon(), 124, 65);
		}
		
		// Overlay
		this.mc.getTextureManager().bindTexture(guiBg);
		this.drawTexturedModalRect(12, 13, 0, 166, 124, 65);
		
		ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int x = Mouse.getX() / sr.getScaleFactor();
		int y = this.height - Mouse.getY() / sr.getScaleFactor();
		if(x >= k + 12 && x <= k + 124 && y >= l + 13 && y <= l + 65) {
			if(fluid == null) {
				List<String> list = new ArrayList<String>();
				list.add("0/" + String.valueOf(this.container.capacity) + " mB");
				this.drawHoveringText(list, x, y, this.fontRendererObj);
			} else {
				List<String> list = new ArrayList<String>();
				list.add(StatCollector.translateToLocal(fluid.getUnlocalizedName()));
				list.add(String.valueOf(this.container.fluidAmount) + "/" + String.valueOf(this.container.capacity) + " mB");
				this.drawHoveringText(list, x / sr.getScaleFactor(), y, this.fontRendererObj);
			}
		}
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
