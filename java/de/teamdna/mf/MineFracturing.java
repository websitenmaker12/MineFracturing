package de.teamdna.mf;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import de.teamdna.mf.api.CoreRegistry;
import de.teamdna.mf.api.PipeRegistry;
import de.teamdna.mf.biome.BiomeGenInfested;
import de.teamdna.mf.block.BlockBore;
import de.teamdna.mf.block.BlockChemicalsMixer;
import de.teamdna.mf.block.BlockCondenseChamber;
import de.teamdna.mf.block.BlockCrafting;
import de.teamdna.mf.block.BlockFluid;
import de.teamdna.mf.block.BlockGenerator;
import de.teamdna.mf.block.BlockGrindStone;
import de.teamdna.mf.block.BlockMaterialExtractor;
import de.teamdna.mf.block.BlockPipe;
import de.teamdna.mf.block.BlockTank;
import de.teamdna.mf.block.BlockTraverse;
import de.teamdna.mf.event.BucketHandler;
import de.teamdna.mf.event.EntityHandler;
import de.teamdna.mf.event.FuelHandler;
import de.teamdna.mf.gui.GuiHandler;
import de.teamdna.mf.item.ItemAcidProofArmor;
import de.teamdna.mf.item.ItemJetpack;
import de.teamdna.mf.item.ItemMFBucket;
import de.teamdna.mf.item.ItemWoodenPillar;
import de.teamdna.mf.net.CommonProxy;
import de.teamdna.mf.net.ServerKeys;
import de.teamdna.mf.packet.PacketBiomUpdate;
import de.teamdna.mf.packet.PacketHandler;
import de.teamdna.mf.packet.PacketKeyUpdate;
import de.teamdna.mf.tile.FurnaceImporter;
import de.teamdna.mf.tile.PipeNetworkController;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.tile.TileEntityChemicalsMixer;
import de.teamdna.mf.tile.TileEntityCondenseChamber;
import de.teamdna.mf.tile.TileEntityCore;
import de.teamdna.mf.tile.TileEntityExtractor;
import de.teamdna.mf.tile.TileEntityGrindStone;
import de.teamdna.mf.tile.TileEntityPipe;
import de.teamdna.mf.tile.TileEntityTank;
import de.teamdna.mf.tile.TileEntityTraverse;
import de.teamdna.mf.util.Util;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version)
public class MineFracturing {

	public static Logger logger;
	public static final PacketHandler packetHandler = new PacketHandler();
	
	@Instance(Reference.modid)
	public static MineFracturing INSTANCE;
	
	@SidedProxy(clientSide = Reference.clientProxy, serverSide = Reference.commonProxy)
	public static CommonProxy proxy;
	
	@SidedProxy(clientSide = "de.teamdna.mf.net.ClientKeys", serverSide = "de.teamdna.mf.net.ServerKeys")
	public static ServerKeys keys;
	
	public CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), Reference.modid) {
		public Item getTabIconItem() {
			return (new ItemStack(traverse)).getItem();
		}
	};
	
	public BiomeGenBase infestedBiome;
	
	public Block bore;
	public Block pipe;
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
	public Block grindStone;
	
	public Item bucketOil;
	public Item bucketFracFluid;
	public Item bucketLiquidOre;
	public Item coalDust;
	public Item ironDust;
	public Item goldDust;
	public Item diamondDust;
	public Item emeraldDust;
	public Item valve;
	public Item woodenPillar;
	public Item flour;
	public Item jetpack;
	public Item acidProofBoots;
	public Item acidProofLeggins;
	public Item acidProofChestplate;
	public Item acidProofHelmet;
	
	public Fluid oil;
	public Fluid fracFluid;
	public Fluid liquidOre;
	
	public static int boreRadius;
	public static int infestionMultiplier;
	public static int oreMultiplierMin;
	public static int oreMultiplierMax;
	public static int infestedBiomeID;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Reference.setupMetadata(event.getModMetadata());
		logger = event.getModLog();
		
		// Configuration
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		config.addCustomCategoryComment(Configuration.CATEGORY_GENERAL, "NOTICE: This values should be the same on client and server. Otherwise it'll crash or look really ugly.");
		
		Property p = config.get(Configuration.CATEGORY_GENERAL, "INT.BoreRadius", 64);
		p.comment = "This is the radius (in blocks) that the Bore will analyse (Standart: 64)";
		boreRadius = p.getInt(64);
		
		p = config.get(Configuration.CATEGORY_GENERAL, "INT.InfestionMultiplier", 4);
		p.comment = "The multiplier calculates the infection radius. INT.BoreRadius * INT.InfestionMultiplier (Standart: 4)";
		infestionMultiplier = p.getInt(4);
		
		p = config.get(Configuration.CATEGORY_GENERAL, "INT.OreMultiplierMin", 4);
		p.comment = "The minimum multiplier of the ores (Standart: 4)";
		oreMultiplierMin = p.getInt(4);
		
		p = config.get(Configuration.CATEGORY_GENERAL, "INT.OreMultiplierMax", 7);
		p.comment = "The maximum multiplier of the ores (Standart: 7)";
		oreMultiplierMax = p.getInt(7);
		
		int standartID = Util.getFirstEmptyIndex(BiomeGenBase.getBiomeGenArray());
		p = config.get(Configuration.CATEGORY_GENERAL, "INT.InfestedBiomeID", standartID);
		p.comment = "The ID of the Infested Biome (Standart: The first empty ID in the Biome Registry)";
		infestedBiomeID = p.getInt(standartID);
		
		config.save();

		// Blocks
		this.bore = (new BlockBore()).setBlockName("bore").setCreativeTab(this.tab);
		this.pipe = (new BlockPipe()).setBlockName("pipe").setCreativeTab(this.tab);
		this.traverse = (new BlockTraverse()).setBlockName("traverse").setCreativeTab(this.tab);
		this.extractor = (new BlockMaterialExtractor()).setBlockName("materialExtractor").setCreativeTab(this.tab);
		this.tankWall = (new BlockTank(0)).setBlockName("tankWall").setCreativeTab(this.tab).setBlockTextureName(Reference.modid + ":tank_Wall");
		this.tankController = (new BlockTank(1)).setBlockName("tankController").setCreativeTab(this.tab).setBlockTextureName(Reference.modid + ":tank_controller");
		this.chemicalsMixer = (new BlockChemicalsMixer()).setBlockName("chemicalsMixer").setCreativeTab(this.tab).setBlockTextureName(Reference.modid + ":chemicalsMixer");
		this.condenseChamber = (new BlockCondenseChamber()).setBlockName("condenseChamber").setCreativeTab(this.tab);
		this.basicMachine = (new BlockCrafting()).setBlockName("basicMachine").setResistance(10F).setBlockTextureName(Reference.modid + ":basicMachine").setCreativeTab(this.tab);
		this.combustionGen = (new BlockGenerator()).setBlockName("combustionGen").setCreativeTab(this.tab);
		this.grindStone = (new BlockGrindStone()).setBlockName("grindStone").setCreativeTab(this.tab);
		
		// Items
		this.coalDust = (new Item()).setUnlocalizedName("coalDust").setTextureName(Reference.modid + ":coalDust").setCreativeTab(this.tab);
		this.ironDust = (new Item()).setUnlocalizedName("ironDust").setTextureName(Reference.modid + ":ironDust").setCreativeTab(this.tab);
		this.goldDust = (new Item()).setUnlocalizedName("goldDust").setTextureName(Reference.modid + ":goldDust").setCreativeTab(this.tab);
		this.diamondDust = (new Item()).setUnlocalizedName("diamondDust").setTextureName(Reference.modid + ":diamondDust").setCreativeTab(this.tab);
		this.emeraldDust = (new Item()).setUnlocalizedName("emeraldDust").setTextureName(Reference.modid + ":emeraldDust").setCreativeTab(this.tab);
		this.valve = (new Item()).setUnlocalizedName("valve").setTextureName(Reference.modid + ":valve").setCreativeTab(this.tab);
		this.woodenPillar = (new ItemWoodenPillar()).setUnlocalizedName("woodenPillar").setCreativeTab(this.tab);
		this.flour = (new Item()).setUnlocalizedName("flour").setCreativeTab(this.tab);
		this.jetpack = (new ItemJetpack()).setUnlocalizedName("jetpack").setCreativeTab(this.tab);
		this.acidProofBoots = (new ItemAcidProofArmor(3)).setUnlocalizedName("acidProofBoots").setCreativeTab(this.tab);
		this.acidProofLeggins = (new ItemAcidProofArmor(2)).setUnlocalizedName("acidProofLegs").setCreativeTab(this.tab);
		this.acidProofChestplate = (new ItemAcidProofArmor(1)).setUnlocalizedName("acidProofChestplate").setCreativeTab(this.tab);
		this.acidProofHelmet = (new ItemAcidProofArmor(0)).setUnlocalizedName("acidProofHelmet").setCreativeTab(this.tab);
		
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
		this.bucketOil = (new ItemMFBucket(this.oilBlock)).setUnlocalizedName("bucketOil").setCreativeTab(this.tab).setTextureName(Reference.modid + ":bucket_oil");
		FluidContainerRegistry.registerFluidContainer(this.oil, new ItemStack(this.bucketOil), FluidContainerRegistry.EMPTY_BUCKET);
		BucketHandler.INSTANCE.register(this.oilBlock, this.bucketOil);
		
		this.bucketFracFluid = (new ItemMFBucket(this.fracFluidBlock)).setUnlocalizedName("bucketFracFluid").setCreativeTab(this.tab).setTextureName(Reference.modid + ":bucket_fracFluid");
		FluidContainerRegistry.registerFluidContainer(this.fracFluid, new ItemStack(this.bucketFracFluid), FluidContainerRegistry.EMPTY_BUCKET);
		BucketHandler.INSTANCE.register(this.fracFluidBlock, this.bucketFracFluid);
		
		this.bucketLiquidOre = (new ItemMFBucket(this.liquidOreBlock)).setUnlocalizedName("bucketLiquidOre").setCreativeTab(this.tab).setTextureName(Reference.modid + ":bucket_liquidOre");
		FluidContainerRegistry.registerFluidContainer(this.liquidOre, new ItemStack(this.bucketLiquidOre), FluidContainerRegistry.EMPTY_BUCKET);
		BucketHandler.INSTANCE.register(this.liquidOreBlock, this.bucketLiquidOre);
		
		proxy.registerBlock(this.bore);
		proxy.registerBlock(this.pipe);
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
		proxy.registerBlock(this.grindStone);
		
		proxy.registerItem(this.bucketOil);
		proxy.registerItem(this.bucketFracFluid);
		proxy.registerItem(this.bucketLiquidOre);
		proxy.registerItem(this.coalDust);
		proxy.registerItem(this.ironDust);
		proxy.registerItem(this.goldDust);
		proxy.registerItem(this.diamondDust);
		proxy.registerItem(this.emeraldDust);
		proxy.registerItem(this.valve);
		proxy.registerItem(this.woodenPillar);
		proxy.registerItem(this.flour);
		proxy.registerItem(this.jetpack);
		proxy.registerItem(this.acidProofHelmet);
		proxy.registerItem(this.acidProofChestplate);
		proxy.registerItem(this.acidProofLeggins);
		proxy.registerItem(this.acidProofBoots);
		
		proxy.registerTile(TileEntityCore.class, "core");
		proxy.registerTile(TileEntityBore.class, "bore");
		proxy.registerTile(TileEntityPipe.class, "pressureTube");
		proxy.registerTile(TileEntityTraverse.class, "traverse");
		proxy.registerTile(TileEntityExtractor.class, "extractor");
		proxy.registerTile(TileEntityTank.class, "tank");
		proxy.registerTile(TileEntityChemicalsMixer.class, "chemicalsMixer");
		proxy.registerTile(TileEntityCondenseChamber.class, "condenseChamber");
		proxy.registerTile(TileEntityGrindStone.class, "grindStone");
		
		OreDictionary.registerOre("dustCoal", this.coalDust);
		OreDictionary.registerOre("dustIron", this.ironDust);
		OreDictionary.registerOre("dustGold", this.goldDust);
		OreDictionary.registerOre("dustDiamond", this.diamondDust);
		OreDictionary.registerOre("dustEmerald", this.emeraldDust);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		packetHandler.init();
		packetHandler.registerPacket(PacketKeyUpdate.class);
		packetHandler.registerPacket(PacketBiomUpdate.class);
		proxy.registerRenderer();
		
		// Bioms
		this.infestedBiome = (new BiomeGenInfested(infestedBiomeID)).setBiomeName("Infested");
		
		// Registrations
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
		FMLCommonHandler.instance().bus().register(PipeNetworkController.INSTNACE);
		
		EntityHandler entityHandler = new EntityHandler();
		MinecraftForge.EVENT_BUS.register(entityHandler);
		FMLCommonHandler.instance().bus().register(entityHandler);
		
		GameRegistry.registerFuelHandler(new FuelHandler());
		
		// Crafting
		GameRegistry.addRecipe(new ItemStack(traverse), "###", "# #", "###", '#', Blocks.iron_bars);
		GameRegistry.addRecipe(new ItemStack(bore), "#C#", "#A#", "#B#", '#', traverse, 'A', basicMachine, 'B', Items.diamond_pickaxe, 'C', combustionGen);
		GameRegistry.addRecipe(new ItemStack(extractor), "###", "#A#", "###", '#', traverse, 'A', this.valve);
		GameRegistry.addRecipe(new ItemStack(valve), "###", "# #", "###", '#', Blocks.iron_bars);
		GameRegistry.addRecipe(new ItemStack(combustionGen), "#A#", "CBC", "#A#", '#', Items.iron_ingot, 'A', Blocks.iron_bars, 'B', Blocks.furnace, 'C', basicMachine);
		GameRegistry.addRecipe(new ItemStack(basicMachine), "#A#", "ABA", "#A#", '#', Items.iron_ingot, 'A', Items.gold_ingot, 'B', Blocks.iron_block);
		GameRegistry.addRecipe(new ItemStack(basicMachine), "#A#", "ABA", "#A#", '#', Items.gold_ingot, 'A', Items.iron_ingot, 'B', Blocks.iron_block);
		GameRegistry.addRecipe(new ItemStack(pipe, 32), "###", "ABA", "###", '#', Blocks.stone, 'A', Items.iron_ingot, 'B', Items.bucket);
		GameRegistry.addRecipe(new ItemStack(tankWall, 8), "###", "#A#", "###", '#', Items.iron_ingot, 'A', Blocks.iron_bars);
		GameRegistry.addRecipe(new ItemStack(tankController), "#A#", "ABA", "#A#", '#', tankWall, 'A', Items.iron_ingot, 'B', basicMachine);
		GameRegistry.addRecipe(new ItemStack(tankController), "#A#", "ABA", "#A#", '#', Items.iron_ingot, 'A', tankWall, 'B', basicMachine);
		GameRegistry.addRecipe(new ItemStack(chemicalsMixer), "#A#", "ABA", "#A#", '#', Items.glass_bottle, 'A', Blocks.glass, 'B', basicMachine);
		GameRegistry.addRecipe(new ItemStack(chemicalsMixer), "#A#", "ABA", "#A#", '#', Blocks.glass, 'A', Items.glass_bottle, 'B', basicMachine);
		GameRegistry.addRecipe(new ItemStack(condenseChamber), "BBB", "C  ", "BAB", 'A', Items.bucket, 'B', Items.iron_ingot, 'C', Items.stick);
		GameRegistry.addRecipe(new ItemStack(this.woodenPillar), "#", "#", '#', Items.stick);
		GameRegistry.addRecipe(new ItemStack(this.jetpack), "IBI", "ILI", "F F", 'I', Items.iron_ingot, 'B', this.bucketOil, 'L', Items.leather, 'F', Items.flint_and_steel);
		GameRegistry.addRecipe(new JetpackRecipe());
		GameRegistry.addShapelessRecipe(new ItemStack(this.acidProofBoots), Items.iron_boots, this.bucketFracFluid);
		GameRegistry.addShapelessRecipe(new ItemStack(this.acidProofChestplate), Items.iron_chestplate, this.bucketFracFluid);
		GameRegistry.addShapelessRecipe(new ItemStack(this.acidProofHelmet), Items.iron_helmet, this.bucketFracFluid);
		GameRegistry.addShapelessRecipe(new ItemStack(this.acidProofLeggins), Items.iron_leggings, this.bucketFracFluid);
		
		GameRegistry.addSmelting(ironDust, new ItemStack(Items.iron_ingot), 1F);
		GameRegistry.addSmelting(coalDust, new ItemStack(Items.coal), 1F);
		GameRegistry.addSmelting(ironDust, new ItemStack(Items.iron_ingot), 1F);
		GameRegistry.addSmelting(diamondDust, new ItemStack(Items.diamond), 1F);
		GameRegistry.addSmelting(emeraldDust, new ItemStack(Items.emerald), 1F);
		GameRegistry.addSmelting(goldDust, new ItemStack(Items.gold_ingot), 1F);
		GameRegistry.addSmelting(this.flour, new ItemStack(Items.bread), 0F);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		packetHandler.postInit();
		
		CoreRegistry.scanForOres();
		CoreRegistry.registerOre(Blocks.redstone_ore, new ItemStack(Items.redstone));
		
		PipeRegistry.registerCustomTile(TileEntityFurnace.class, FurnaceImporter.class);
	}
	
	@EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		PipeNetworkController.INSTNACE.dispose();
	}
	
}
