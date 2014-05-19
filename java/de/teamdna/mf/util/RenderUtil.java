package de.teamdna.mf.util;

import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

public class RenderUtil {

	public static void setIntColor3(int color) {
		GL11.glColor3ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF));
	}
	
	public static void setColor(int r, int g, int b) {
		setColor(r, g, b, 255);
	}
	
	public static void setColor(int r, int g, int b, int a) {
		float m = 255F / 100F;
		GL11.glColor4f(r * m, g * m, b * m, a * m);
	}

	public static void renderIconCube(float x, float y, float z, IIcon icon) {
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(icon.getMinU(), icon.getMaxV());
			GL11.glVertex3f(x + 0, y + 0, z + 0);
			GL11.glTexCoord2f(icon.getMaxU(), icon.getMaxV());
			GL11.glVertex3f(x + 0, y + 1, z + 0);
			GL11.glTexCoord2f(icon.getMaxU(), icon.getMinV());
			GL11.glVertex3f(x + 1, y + 1, z + 0);
			GL11.glTexCoord2f(icon.getMinU(), icon.getMinV());
			GL11.glVertex3f(x + 1, y + 0, z + 0);
			
			GL11.glTexCoord2f(icon.getMinU(), icon.getMaxV());
			GL11.glVertex3f(x + 0, y + 0, z + 1);
			GL11.glTexCoord2f(icon.getMaxU(), icon.getMaxV());
			GL11.glVertex3f(x + 0, y + 1, z + 1);
			GL11.glTexCoord2f(icon.getMaxU(), icon.getMinV());
			GL11.glVertex3f(x + 1, y + 1, z + 1);
			GL11.glTexCoord2f(icon.getMinU(), icon.getMinV());
			GL11.glVertex3f(x + 1, y + 0, z + 1);
			
			GL11.glTexCoord2f(icon.getMinU(), icon.getMaxV());
			GL11.glVertex3f(x + 0, y + 0, z + 0);
			GL11.glTexCoord2f(icon.getMaxU(), icon.getMaxV());
			GL11.glVertex3f(x + 0, y + 1, z + 0);
			GL11.glTexCoord2f(icon.getMaxU(), icon.getMinV());
			GL11.glVertex3f(x + 0, y + 1, z + 1);
			GL11.glTexCoord2f(icon.getMinU(), icon.getMinV());
			GL11.glVertex3f(x + 0, y + 0, z + 1);
			
			GL11.glTexCoord2f(icon.getMinU(), icon.getMaxV());
			GL11.glVertex3f(x + 1, y + 0, z + 0);
			GL11.glTexCoord2f(icon.getMaxU(), icon.getMaxV());
			GL11.glVertex3f(x + 1, y + 1, z + 0);
			GL11.glTexCoord2f(icon.getMaxU(), icon.getMinV());
			GL11.glVertex3f(x + 1, y + 1, z + 1);
			GL11.glTexCoord2f(icon.getMinU(), icon.getMinV());
			GL11.glVertex3f(x + 1, y + 0, z + 1);
		GL11.glEnd();
	}
	
}
