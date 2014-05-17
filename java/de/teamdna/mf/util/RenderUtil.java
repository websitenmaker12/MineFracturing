package de.teamdna.mf.util;

import org.lwjgl.opengl.GL11;

public class RenderUtil {

	public static void setIntColor3(int color) {
		GL11.glColor3ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF));
	}
	
}
