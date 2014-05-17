package de.teamdna.mf.util;

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
	
	public static void draw3DLine(double b1, double b2, double b3, double b4, double t1, double t2, double t3, double t4) {
	}
	
}
