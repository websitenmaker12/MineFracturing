package de.teamdna.mf;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.teamdna.mf.api.CoreRegistry;
import de.teamdna.mf.biome.BiomeGenInfested;
import de.teamdna.mf.block.BlockBore;
import de.teamdna.mf.net.CommonProxy;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.util.Util;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version)
public class MineFracturing {

	public static Logger logger;
	
	@Instance(Reference.modid)
	public static MineFracturing INSTNACE;
	
	@SidedProxy(clientSide = Reference.clientProxy, serverSide = Reference.commonProxy)
	public static CommonProxy proxy;
	
	public CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), Reference.modid) {
		public Item getTabIconItem() {
			return Items.diamond;
		}
	};
	
	public BiomeGenBase infestedBiome;
	
	public Block bore;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Reference.setupMetadata(event.getModMetadata());
		logger = event.getModLog();
		
<<<<<<< HEAD
		//TODO: Get the next free biome id instead of 75
		infestedBiome = new BiomeGenInfested(75);
		
=======
>>>>>>> a5688bfc45f47d2648dbe4d52ca9cb5b5b6f039c
		this.bore = (new BlockBore()).setBlockName("bore").setCreativeTab(this.tab);
		
		proxy.registerBlock(this.bore);
		
		proxy.registerTile(TileEntityBore.class, "bore");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		this.infestedBiome = new BiomeGenInfested(Util.getFirstEmptyIndex(BiomeGenBase.getBiomeGenArray()));
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		CoreRegistry.scanForOres();
	}
	
}
