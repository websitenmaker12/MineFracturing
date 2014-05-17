package de.teamdna.mf.gui;

import org.lwjgl.opengl.GL11;

import de.teamdna.mf.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiTank extends GuiContainer {

	public GuiTank(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerTank(player, world, x, y, z));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2) {
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		
	}

}
