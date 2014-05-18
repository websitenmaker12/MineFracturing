package de.teamdna.mf;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
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
import de.teamdna.mf.api.CoreRegistry;
import de.teamdna.mf.biome.BiomeGenInfested;
import de.teamdna.mf.block.BlockBore;
import de.teamdna.mf.block.BlockChemicalsMixer;
import de.teamdna.mf.block.BlockFluid;
import de.teamdna.mf.block.BlockGhostLoader;
import de.teamdna.mf.block.BlockMaterialExtractor;
import de.teamdna.mf.block.BlockPressureTube;
import de.teamdna.mf.block.BlockTank;
import de.teamdna.mf.block.BlockTraverse;
import de.teamdna.mf.event.BucketHandler;
import de.teamdna.mf.gui.GuiHandler;
import de.teamdna.mf.net.CommonProxy;
import de.teamdna.mf.tile.TileEntityBore;
import de.teamdna.mf.tile.TileEntityChemicalsMixer;
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
	public Block ghostLoader;
	
	public Item bucketOil;
	public Item bucketFracFluid;
	
	public Fluid oil;
	public Fluid fracFluid;
	
	// TODO: Resistance, Hardness to blocks; Crafting receipes
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Reference.setupMetadata(event.getModMetadata());
		logger = event.getModLog();
		
		this.bore = (new BlockBore()).setBlockName("bore").setCreativeTab(this.tab);
		this.pressureTube = (new BlockPressureTube()).setBlockName("pressureTube").setCreativeTab(this.tab);
		this.traverse = (new BlockTraverse()).setBlockName("traverse").setCreativeTab(this.tab);
		this.extractor = (new BlockMaterialExtractor()).setBlockName("materialExtractor").setCreativeTab(this.tab);
		this.tankWall = (new BlockTank(0)).setBlockName("tankWall").setCreativeTab(this.tab).setBlockTextureName(Reference.modid + ":tank_Wall");
		this.tankController = (new BlockTank(1)).setBlockName("tankController").setCreativeTab(this.tab).setBlockTextureName(Reference.modid + ":tank_controller");
		this.chemicalsMixer = (new BlockChemicalsMixer()).setBlockName("chemicalsMixer").setCreativeTab(tab).setBlockTextureName(Reference.modid + "chemicalsMixer");
		this.ghostLoader = (new BlockGhostLoader()).setBlockName("ghostLoader");
		
		this.oil = (new Fluid("oil")).setViscosity(3400).setDensity(1200);
		FluidRegistry.registerFluid(this.oil);
		this.oilBlock = (new BlockFluid(this.oil, Material.water)).setBlockName("oilSource").setLightOpacity(3);
		
		this.fracFluid = (new Fluid("fracfluid")).setViscosity(3400).setDensity(1200);
		FluidRegistry.registerFluid(this.fracFluid);
		this.fracFluidBlock = (new BlockFluid(this.fracFluid, Material.water)).setBlockName("fracFluidSource").setLightOpacity(3);
		
		this.bucketOil = (new ItemBucket(this.oilBlock)).setUnlocalizedName("bucketOil").setCreativeTab(this.tab).setTextureName(Reference.modid + ":bucket_oil").setContainerItem(Items.bucket);
		FluidContainerRegistry.registerFluidContainer(this.oil, new ItemStack(this.bucketOil), FluidContainerRegistry.EMPTY_BUCKET);
		BucketHandler.INSTANCE.register(this.oilBlock, this.bucketOil);
		
		this.bucketFracFluid = (new ItemBucket(this.fracFluidBlock)).setUnlocalizedName("bucketFracFluid").setCreativeTab(this.tab).setTextureName(Reference.modid + ":bucket_oil").setContainerItem(Items.bucket);
		FluidContainerRegistry.registerFluidContainer(this.fracFluid, new ItemStack(this.bucketFracFluid), FluidContainerRegistry.EMPTY_BUCKET);
		BucketHandler.INSTANCE.register(this.fracFluidBlock, this.bucketFracFluid);
		
		proxy.registerBlock(this.bore);
		proxy.registerBlock(this.pressureTube);
		proxy.registerBlock(this.traverse);
		proxy.registerBlock(this.extractor);
		proxy.registerBlock(this.tankWall);
		proxy.registerBlock(this.tankController);
		proxy.registerBlock(this.oilBlock);
		proxy.registerBlock(this.fracFluidBlock);
		proxy.registerBlock(this.chemicalsMixer);
		proxy.registerBlock(this.ghostLoader);
		
		proxy.registerItem(this.bucketOil);
		proxy.registerItem(bucketFracFluid);
		
		proxy.registerTile(TileEntityCore.class, "core");
		proxy.registerTile(TileEntityBore.class, "bore");
		proxy.registerTile(TileEntityPressureTube.class, "pressureTube");
		proxy.registerTile(TileEntityTraverse.class, "traverse");
		proxy.registerTile(TileEntityExtractor.class, "extractor");
		proxy.registerTile(TileEntityTank.class, "tank");
		proxy.registerTile(TileEntityChemicalsMixer.class, "chemicalsMixer");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderer();
		
		this.infestedBiome = (new BiomeGenInfested(Util.getFirstEmptyIndex(BiomeGenBase.getBiomeGenArray()))).setBiomeName("Infested");
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		CoreRegistry.scanForOres();
	}
	
}
