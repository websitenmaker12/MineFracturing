package de.teamdna.mf.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import de.teamdna.mf.Reference;
import de.teamdna.mf.tile.TileEntityBore;

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
		
		//Combustion generator
	    this.fontRendererObj.drawString("Combustion", 177, 18, 4210752);
	    this.fontRendererObj.drawString("Generator", 177, 28, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		 GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	     this.mc.getTextureManager().bindTexture(guiBg);
	     int k = (this.width - this.xSize) / 2;
	     int l = (this.height - this.ySize) / 2;
	     this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	     
		// Combustion Generator
		this.mc.getTextureManager().bindTexture(guiBg);
		int i1 = this.tile.getCurrentItemBurnTime(12);
		this.drawTexturedModalRect(k + 203, l + 50 + 12 - i1, 242, 12 - i1 + 1, 14, i1 + 2);
	}

}
