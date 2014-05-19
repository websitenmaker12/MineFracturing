package de.teamdna.mf;

import cpw.mods.fml.common.ModMetadata;

public class Reference {

	public static final String modid   = "mf";
	public static final String name    = "MineFracturing";
	public static final String version = "1.0";

	public static final String clientProxy = "de.teamdna.mf.net.ClientProxy";
	public static final String commonProxy = "de.teamdna.mf.net.CommonProxy";
	
	public static void setupMetadata(ModMetadata meta) {
		meta.modId = modid;
		meta.name = name;
		meta.version = version;
		meta.description = "A mod that adds Fracturing to the game to get ores!";
		meta.authorList.add("ObsiLP");
		meta.authorList.add("wara286");
	}
	
	public static class PipePacketIDs {
		
		public static final int fluid = 1;
		public static final int block = 2;
		
	}
	
}
