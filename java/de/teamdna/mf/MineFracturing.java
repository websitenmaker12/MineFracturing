package de.teamdna.mf;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
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
import cpw.mods.fml.common.registry.GameRegistry;
import de.teamdna.mf.api.CoreRegistry;
import de.teamdna.mf.biome.BiomeGenInfested;
import de.teamdna.mf.block.BlockBore;
import de.teamdna.mf.block.BlockChemicalsMixer;
import de.teamdna.mf.block.BlockCondenseChamber;
import de.teamdna.mf.block.BlockCrafting;
import de.teamdna.mf.block.BlockFluid;
import de.teamdna.mf.block.BlockGenerator;
import de.teamdna.mf.block.BlockMaterialExtractor;
import de.teamdna.mf.block.BlockPressureTube;
import de.teamdna.mf.block.BlockTank;
import de.teamdna.mf.block.BlockTraverse;
import de.teamdna.mf.event.BucketHandler;
import de.teamdna.mf.event.FuelHandler;
import de.teamdna.mf.gui.GuiHandler;
import de.teamdna.mf.net.CommonProxy;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.tile.TileEntityChemicalsMixer;
import de.teamdna.mf.tile.TileEntityCondenseChamber;
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
			return (new ItemStack(traverse)).getItem();
		}
	};
	
	public BiomeGenBase infestedBiome;
	
	public Block bore;
	public Block pressureTube;
	public Block traverse;
	public Block extractor;
	public Block tankWall;
	public Block tankController;
	public Block oilBlock;
	public Block fracFluidBlock;
	public Block chemicalsMixer;
	public Block condenseChamber;
	public Block liquidOreBlock;
	public Block basicMachine;
	public Block combustionGen;
	
	public Item bucketOil;
	public Item bucketFracFluid;
	public Item bucketLiquidOre;
	public Item coalDust;
	public Item ironDust;
	public Item goldDust;
	public Item diamondDust;
	public Item emeraldDust;
	
	public Fluid oil;
	public Fluid fracFluid;
	public Fluid liquidOre;
	
	public static int boreRadius;
	public static int infestionMultiplier;
	public static int oreMultiplierMin;
	public static int oreMultiplierMax;
	
	// TODO: Resistance, Hardness to blocks; Crafting receipes
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Reference.setupMetadata(event.getModMetadata());
		logger = event.getModLog();
		
		// Configuration
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		config.addCustomCategoryComment(Configuration.CATEGORY_GENERAL, "NOTICE: This values should be the same on client and server. Otherwise it'll crash or look really ugly.");
		
		Property p1 = config.get(Configuration.CATEGORY_GENERAL, "INT.BoreRadius", 64);
		p1.comment = "This is the radius (in blocks) that the Bore will analyse (Standart: 64)";
		boreRadius = p1.getInt(64);
		
		Property p2 = config.get(Configuration.CATEGORY_GENERAL, "INT.InfestionMultiplier", 4);
		p2.comment = "The multiplier calculates the infection radius. INT.BoreRadius * INT.InfestionMultiplier (Standart: 4)";
		infestionMultiplier = p2.getInt(4);
		
		Property p3 = config.get(Configuration.CATEGORY_GENERAL, "INT.OreMultiplierMin", 4);
		p3.comment = "The minimum multiplier of the ores (Standart: 4)";
		oreMultiplierMin = p3.getInt(4);
		
		Property p4 = config.get(Configuration.CATEGORY_GENERAL, "INT.OreMultiplierMax", 7);
		p4.comment = "The maximum multiplier of the ores (Standart: 7)";
		oreMultiplierMax = p4.getInt(7);
		
		config.save();
		
		// Blocks
		this.bore = (new BlockBore()).setBlockName("bore").setCreativeTab(this.tab);
		this.pressureTube = (new BlockPressureTube()).setBlockName("pressureTube").setCreativeTab(this.tab);
		this.traverse = (new BlockTraverse()).setBlockName("traverse").setCreativeTab(this.tab);
		this.extractor = (new BlockMaterialExtractor()).setBlockName("materialExtractor").setCreativeTab(this.tab);
		this.tankWall = (new BlockTank(0)).setBlockName("tankWall").setCreativeTab(this.tab).setBlockTextureName(Reference.modid + ":tank_Wall");
		this.tankController = (new BlockTank(1)).setBlockName("tankController").setCreativeTab(this.tab).setBlockTextureName(Reference.modid + ":tank_controller");
		this.chemicalsMixer = (new BlockChemicalsMixer()).setBlockName("chemicalsMixer").setCreativeTab(this.tab).setBlockTextureName(Reference.modid + ":chemicalsMixer");
		this.condenseChamber = (new BlockCondenseChamber()).setBlockName("condenseChamber").setCreativeTab(this.tab);
		this.basicMachine = (new BlockCrafting()).setBlockName("basicMachine").setResistance(10F).setBlockTextureName(Reference.modid + ":basicMachine").setCreativeTab(tab);
		this.combustionGen = (new BlockGenerator()).setBlockName("combusioneGen");
		
		// Items
		this.coalDust = (new Item()).setUnlocalizedName("coalDust").setTextureName(Reference.modid + ":coalDust").setCreativeTab(this.tab);
		this.ironDust = (new Item()).setUnlocalizedName("ironDust").setTextureName(Reference.modid + ":ironDust").setCreativeTab(this.tab);
		this.goldDust = (new Item()).setUnlocalizedName("goldDust").setTextureName(Reference.modid + ":goldDust").setCreativeTab(this.tab);
		this.diamondDust = (new Item()).setUnlocalizedName("diamondDust").setTextureName(Reference.modid + ":diamondDust").setCreativeTab(this.tab);
		this.emeraldDust = (new Item()).setUnlocalizedName("emeraldDust").setTextureName(Reference.modid + ":emeraldDust").setCreativeTab(this.tab);
		
		// Fluids
		this.oil = (new Fluid("oil")).setViscosity(3400).setDensity(1200);
		FluidRegistry.registerFluid(this.oil);
		this.oilBlock = (new BlockFluid(this.oil, Material.water)).setBlockName("oilSource").setLightOpacity(3);
		
		this.fracFluid = (new Fluid("fracfluid")).setViscosity(3400).setDensity(1200);
		FluidRegistry.registerFluid(this.fracFluid);
		this.fracFluidBlock = (new BlockFluid(this.fracFluid, Material.water)).setBlockName("fracFluidSource").setLightOpacity(3);
		
		this.liquidOre = (new Fluid("liquidOre")).setViscosity(1400).setDensity(1200);
		FluidRegistry.registerFluid(this.liquidOre);
		this.liquidOreBlock = (new BlockFluid(this.liquidOre, Material.water)).setBlockName("liquidOreSource").setLightOpacity(3);
		
		// Buckets
		this.bucketOil = (new ItemBucket(this.oilBlock)).setUnlocalizedName("bucketOil").setCreativeTab(this.tab).setTextureName(Reference.modid + ":bucket_oil").setContainerItem(Items.bucket);
		FluidContainerRegistry.registerFluidContainer(this.oil, new ItemStack(this.bucketOil), FluidContainerRegistry.EMPTY_BUCKET);
		BucketHandler.INSTANCE.register(this.oilBlock, this.bucketOil);
		
		this.bucketFracFluid = (new ItemBucket(this.fracFluidBlock)).setUnlocalizedName("bucketFracFluid").setCreativeTab(this.tab).setTextureName(Reference.modid + ":bucket_fracFluid").setContainerItem(Items.bucket);
		FluidContainerRegistry.registerFluidContainer(this.fracFluid, new ItemStack(this.bucketFracFluid), FluidContainerRegistry.EMPTY_BUCKET);
		BucketHandler.INSTANCE.register(this.fracFluidBlock, this.bucketFracFluid);
		
		this.bucketLiquidOre = (new ItemBucket(this.liquidOreBlock)).setUnlocalizedName("bucketLiquidOre").setCreativeTab(this.tab).setTextureName(Reference.modid + ":bucket_liquidOre").setContainerItem(Items.bucket);
		FluidContainerRegistry.registerFluidContainer(this.liquidOre, new ItemStack(this.bucketLiquidOre), FluidContainerRegistry.EMPTY_BUCKET);
		BucketHandler.INSTANCE.register(this.liquidOreBlock, this.bucketLiquidOre);
		
		proxy.registerBlock(this.bore);
		proxy.registerBlock(this.pressureTube);
		proxy.registerBlock(this.traverse);
		proxy.registerBlock(this.extractor);
		proxy.registerBlock(this.tankWall);
		proxy.registerBlock(this.tankController);
		proxy.registerBlock(this.oilBlock);
		proxy.registerBlock(this.fracFluidBlock);
		proxy.registerBlock(this.chemicalsMixer);
		proxy.registerBlock(this.condenseChamber);
		proxy.registerBlock(this.liquidOreBlock);
		proxy.registerBlock(this.combustionGen);
		proxy.registerBlock(this.basicMachine);
		
		proxy.registerItem(this.bucketOil);
		proxy.registerItem(this.bucketFracFluid);
		proxy.registerItem(this.bucketLiquidOre);
		proxy.registerItem(this.coalDust);
		proxy.registerItem(this.ironDust);
		proxy.registerItem(this.goldDust);
		proxy.registerItem(this.diamondDust);
		proxy.registerItem(this.emeraldDust);
		
		proxy.registerTile(TileEntityCore.class, "core");
		proxy.registerTile(TileEntityBore.class, "bore");
		proxy.registerTile(TileEntityPressureTube.class, "pressureTube");
		proxy.registerTile(TileEntityTraverse.class, "traverse");
		proxy.registerTile(TileEntityExtractor.class, "extractor");
		proxy.registerTile(TileEntityTank.class, "tank");
		proxy.registerTile(TileEntityChemicalsMixer.class, "chemicalsMixer");
		proxy.registerTile(TileEntityCondenseChamber.class, "condenseChamber");
		
		//////////Crafting///////////////// 	(und so was in der hauptklasse!)
		GameRegistry.addRecipe(new ItemStack(traverse), "###", "# #", "###", '#', Blocks.iron_bars);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderer();
		
		this.infestedBiome = (new BiomeGenInfested(Util.getFirstEmptyIndex(BiomeGenBase.getBiomeGenArray()))).setBiomeName("Infested");
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
		GameRegistry.registerFuelHandler(new FuelHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		CoreRegistry.scanForOres();
	}
	
}
