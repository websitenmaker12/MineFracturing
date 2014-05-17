package de.teamdna.mf;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import de.teamdna.mf.api.CoreRegistry;
import de.teamdna.mf.biome.BiomeGenInfested;
import de.teamdna.mf.block.BlockBore;
import de.teamdna.mf.block.BlockMaterialExtractor;
import de.teamdna.mf.block.BlockPressureTube;
import de.teamdna.mf.block.BlockTank;
import de.teamdna.mf.block.BlockTraverse;
import de.teamdna.mf.gui.GuiHandler;
import de.teamdna.mf.net.CommonProxy;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.tile.TileEntityCore;
import de.teamdna.mf.tile.TileEntityExtractor;
import de.teamdna.mf.tile.TileEntityPressureTube;
import de.teamdna.mf.tile.TileEntityTank;
import de.teamdna.mf.tile.TileEntityTraverse;
import de.teamdna.mf.util.Util;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version)
public class MineFracturing {

	public static Logger logger;
	
	@Instance(Reference.modid)
	public static MineFracturing INSTANCE;
	
	@SidedProxy(clientSide = Reference.clientProxy, serverSide = Reference.commonProxy)
	public static CommonProxy proxy;
	
	public CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), Reference.modid) {
		public Item getTabIconItem() {
			return Items.diamond;
		}
	};
	
	public BiomeGenBase infestedBiome;
	
	public Block bore;
	public Block pressureTube;
	public Block traverse;
	public Block extractor;
	public Block tankWall;
	public Block tankController;
	
	public Fluid oil;
	
	// TODO: Resistance, Hardness to blocks; Crafting receipes
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Reference.setupMetadata(event.getModMetadata());
		logger = event.getModLog();
		
		this.bore = (new BlockBore()).setBlockName("bore").setCreativeTab(this.tab);
		this.pressureTube = (new BlockPressureTube()).setBlockName("pressureTube").setCreativeTab(this.tab);
		this.traverse = (new BlockTraverse()).setBlockName("traverse").setCreativeTab(this.tab);
		this.extractor = (new BlockMaterialExtractor()).setBlockName("materialExtractor").setCreativeTab(this.tab);
		this.tankWall = (new BlockTank(0)).setBlockName("tankWall").setCreativeTab(this.tab).setBlockTextureName("mf" + ":tank_Wall");;
		this.tankController = (new BlockTank(1)).setBlockName("tankController").setCreativeTab(this.tab);
		
		this.oil = (new Fluid("oil")).setDensity(900).setViscosity(1600);
		FluidRegistry.registerFluid(this.oil);
		
		proxy.registerBlock(this.bore);
		proxy.registerBlock(this.pressureTube);
		proxy.registerBlock(this.traverse);
		proxy.registerBlock(this.extractor);
		proxy.registerBlock(this.tankWall);
		proxy.registerBlock(this.tankController);
		
		proxy.registerTile(TileEntityCore.class, "core");
		proxy.registerTile(TileEntityBore.class, "bore");
		proxy.registerTile(TileEntityPressureTube.class, "pressureTube");
		proxy.registerTile(TileEntityTraverse.class, "traverse");
		proxy.registerTile(TileEntityExtractor.class, "extractor");
		proxy.registerTile(TileEntityTank.class, "tank");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderer();
		
		this.infestedBiome = (new BiomeGenInfested(Util.getFirstEmptyIndex(BiomeGenBase.getBiomeGenArray()))).setBiomeName("Infested");
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		CoreRegistry.scanForOres();
	}
	
}
