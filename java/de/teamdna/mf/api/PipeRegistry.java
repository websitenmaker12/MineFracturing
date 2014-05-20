package de.teamdna.mf.api;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import de.teamdna.mf.tile.ICustomImporter;

public class PipeRegistry {

	private static Map<Class<? extends TileEntity>, ICustomImporter> tileImporterMap = new HashMap<Class<? extends TileEntity>, ICustomImporter>();
	
	/**
	 * With this function you can register TileEntities as an Importer and do stuff with them in the
	 * Network. The good thing: You don't need to own the Tile!
	 */
	public static void registerCustomTile(Class<? extends TileEntity> tile, ICustomImporter importer) {
		tileImporterMap.put(tile, importer);
	}
	
	/**
	 * Returns whether a TileEntity has a custom IImporter
	 */
	public static boolean isCustomTile(Class<? extends TileEntity> tile) {
		return tileImporterMap.containsKey(tile);
	}
	
	/**
	 * Returns the IImporter for the given TileEntity
	 */
	public static ICustomImporter getCustomImporter(Class<? extends TileEntity> tile) {
		return tileImporterMap.get(tile);
	}
	
}
