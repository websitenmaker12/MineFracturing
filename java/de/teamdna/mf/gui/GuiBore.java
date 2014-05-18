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
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	    int k = (this.width - this.xSize) / 2;
	    int l = (this.height - this.ySize) / 2;
		drawRect(47, 14, 46 + this.container.prog1, 28, 0x9F00DEFF);
		drawRect(47, 34, 46 + this.container.prog2, 48, 0x9F00DEFF);
		
		String s = StatCollector.translateToLocal("tile.bore.name");
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 4, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.bore.analyzingStatus") + " " + (int)((double)this.container.prog1 / 117D * 100D) + "%", 50, 18, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.bore.frackingStatus") + " " + (int)((double)this.container.prog2 / 117D * 100D) + "%", 50, 37, 4210752);
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
