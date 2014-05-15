package de.teamdna.mf;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.teamdna.mf.block.BlockBore;
import de.teamdna.mf.net.CommonProxy;
import de.teamdna.mf.tile.TileEntityBore;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version)
public class MineFracturing {

	@Instance(Reference.modid)
	public static MineFracturing INSTNACE;
	
	@SidedProxy(clientSide = Reference.clientProxy, serverSide = Reference.commonProxy)
	public static CommonProxy proxy;
	
	public CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), Reference.modid) {
		public Item getTabIconItem() {
			return Items.diamond;
		}
	};
	
	public Block bore;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.bore = (new BlockBore()).setBlockName("bore").setCreativeTab(this.tab);
		
		proxy.registerBlock(this.bore);
		
		proxy.registerTile(TileEntityBore.class, "bore");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
	
}
