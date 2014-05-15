package de.teamdna.mf.util;

import cpw.mods.fml.common.FMLCommonHandler;

public class Util {

	public static String createUID(Object... elements) {
		return createUID("/", elements);
	}
	
	public static String createUID(String seperator, Object... elements) {
		String output = "";
		for(Object obj : elements) output += obj.toString() + seperator;
		return output.substring(0, output.length() - 1);
	}
	
	public static Object[] splitUID(String uid) {
		return splitUID(uid, "/");
	}
	
	public static Object[] splitUID(String uid, String seperator) {
		return uid.split(seperator);
	}
	
	public static boolean runOnServer() {
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}
	
}
