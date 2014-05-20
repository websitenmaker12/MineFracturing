package de.teamdna.mf.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;
import de.teamdna.mf.api.PipeRegistry;
import de.teamdna.mf.util.WorldBlock;

public class PipeNetworkController {
	
	public static PipeNetworkController INSTNACE = new PipeNetworkController();
	
	public Random rand = new Random(System.currentTimeMillis());
	
	public List<Long> networkIDs = new ArrayList<Long>();
	public Map<Long, List<TileEntityPipe>> networkPipeMap = new HashMap<Long, List<TileEntityPipe>>();
	public Map<Long, List<IExtractor>> extractorMap = new HashMap<Long, List<IExtractor>>();
	public Map<Long, List<IImporter>> importerMap = new HashMap<Long, List<IImporter>>();
	public Map<Long, Integer> iterationCounts = new HashMap<Long, Integer>();
	public Map<Long, List<TileEntity>> customTilesMap = new HashMap<Long, List<TileEntity>>();
	public Map<String, List<Long>> tileIDMap = new HashMap<String, List<Long>>();
	
	public boolean isScanning = false;
	
	// TODO: Implement custom IImporter to network
	
	public long createNetwork() {
		long id;
		while(this.networkIDs.contains((id = Math.abs(this.rand.nextLong())))) ;
		this.networkIDs.add(id);
		this.networkPipeMap.put(id, new ArrayList<TileEntityPipe>());
		this.extractorMap.put(id, new ArrayList<IExtractor>());
		this.importerMap.put(id, new ArrayList<IImporter>());
		this.customTilesMap.put(id, new ArrayList<TileEntity>());
		this.iterationCounts.put(id, 0);
		return id;
	}
	
	public void addPipe(TileEntityPipe pipe, long network) {
		if(network == -1L || this.networkPipeMap.get(Long.valueOf(network)) == null) return;
		this.networkPipeMap.get(Long.valueOf(network)).add(pipe);
	}
	
	public boolean doesNetworkExists(long network) {
		return this.networkIDs.contains(Long.valueOf(network));
	}
	
	public void destroyNetwork(long network) {
		if(network == -1L) return;
		
		this.isScanning = true;
		for(TileEntityPipe pipe : this.networkPipeMap.get(network)) {
			if(!pipe.isInvalid()) pipe.networkID = -1L;
		}
		this.networkIDs.remove(Long.valueOf(network));
		this.extractorMap.remove(Long.valueOf(network));
		this.importerMap.remove(Long.valueOf(network));
		this.iterationCounts.remove(Long.valueOf(network));
		this.networkPipeMap.remove(Long.valueOf(network));
		this.customTilesMap.remove(Long.valueOf(network));
		this.isScanning = false;
	}
	
	public void dispose() {
		this.rand.setSeed(System.currentTimeMillis());
		this.networkIDs.clear();
		this.networkPipeMap.clear();
		this.extractorMap.clear();
		this.importerMap.clear();
		this.tileIDMap.clear();
		this.iterationCounts.clear();
		this.customTilesMap.clear();
		this.isScanning = false;
	}

	public boolean canNetworkProcessPacket(long network, NBTTagCompound packet, ForgeDirection dir) {
		if(this.importerMap.get(Long.valueOf(network)) == null) return false;
		for(IImporter importer : this.importerMap.get(Long.valueOf(network))) {
			if(importer.canImport(dir, packet)) return true;
		}
		return false;
	}
	
	public void processPacket(long network, NBTTagCompound packet, ForgeDirection dir, IExtractor extractor) {
		for(IImporter importer : this.importerMap.get(Long.valueOf(network))) {
			if(importer instanceof ICustomImporter) {
				ICustomImporter custom = (ICustomImporter)importer;
				TileEntity orig = extractor.getWorld().getTileEntity(extractor.getX(), extractor.getY(), extractor.getZ());
				if(custom.canImport(dir, packet, orig)) {
					custom.doImport(dir, packet, orig);
				}
			}
			if(importer.canImport(dir, packet)) {
				importer.doImport(dir, packet);
			}
		}
	}
	
	@SubscribeEvent
	public void worldTickEvent(WorldTickEvent event) {
		if(event.side == Side.SERVER && event.phase == Phase.END) {
			
			Map<Long, Integer> copy = new HashMap<Long, Integer>(this.iterationCounts);
			for(Map.Entry<Long, Integer> entry : copy.entrySet()) {
				int itCount = entry.getValue();
				if(this.networkPipeMap.get(entry.getKey()) == null) continue;
				if(itCount < this.networkPipeMap.get(entry.getKey()).size()) {
					TileEntityPipe pipe = this.networkPipeMap.get(entry.getKey()).get(itCount);
					if(pipe != null && pipe.networkID != -1L) {
						for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
							TileEntity tile = pipe.getWorldObj().getTileEntity(pipe.xCoord + dir.offsetX, pipe.yCoord + dir.offsetY, pipe.zCoord + dir.offsetZ);
							if(tile != null && (tile instanceof IExtractor || tile instanceof IImporter || PipeRegistry.isCustomTile(tile.getClass()))) {
								String uid = (new WorldBlock(tile)).getBlockUID();
								if(tile instanceof IExtractor) this.extractorMap.get(pipe.networkID).add((IExtractor)tile);
								if(tile instanceof IImporter) this.importerMap.get(pipe.networkID).add((IImporter)tile);
								if(PipeRegistry.isCustomTile(tile.getClass())) {
									this.importerMap.get(pipe.networkID).add(PipeRegistry.getCustomImporter(tile.getClass()));
									this.customTilesMap.get(entry.getKey()).add(tile);
								}
								
								if(!this.tileIDMap.containsKey(uid)) this.tileIDMap.put(uid, new ArrayList<Long>(Arrays.asList(new Long[] { pipe.networkID })));
								else this.tileIDMap.get(uid).add(pipe.networkID);
							}
						}
						
						itCount++;
						this.iterationCounts.put(entry.getKey(), itCount);
					}
				}
				
				List<TileEntity> copy2 = new ArrayList<TileEntity>(this.customTilesMap.get(entry.getKey()));
				for(TileEntity tile : copy2) {
					if(tile.isInvalid()) {
						this.customTilesMap.get(entry.getKey()).remove(tile);
						this.handleBlockBreak(tile);
					}
				}
			}
			
		}
	}

	public void handleBlockAdd(TileEntity tile) {
		String uid = (new WorldBlock(tile)).getBlockUID();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile2 = tile.getWorldObj().getTileEntity(tile.xCoord + dir.offsetX, tile.yCoord + dir.offsetY, tile.zCoord + dir.offsetZ);
			if(tile2 != null && tile2 instanceof TileEntityPipe) {
				TileEntityPipe pipe = (TileEntityPipe)tile2;
				if(pipe.networkID != -1L) {
					if(tile instanceof IExtractor) this.extractorMap.get(pipe.networkID).add((IExtractor)tile);
					if(tile instanceof IImporter) this.importerMap.get(pipe.networkID).add((IImporter)tile);
					if(PipeRegistry.isCustomTile(tile.getClass())) {
						this.importerMap.get(pipe.networkID).add(PipeRegistry.getCustomImporter(tile.getClass()));
						this.customTilesMap.get(pipe.networkID).add(tile);
					}
					
					if(!this.tileIDMap.containsKey(uid)) this.tileIDMap.put(uid, new ArrayList<Long>(Arrays.asList(new Long[] { pipe.networkID })));
					else this.tileIDMap.get(uid).add(pipe.networkID);
				}
			}
		}
	}
	
	public void handleBlockBreak(TileEntity tile) {
		String uid = (new WorldBlock(tile)).getBlockUID();
		if(this.tileIDMap.containsKey(uid)) {
			List<Long> ids = this.tileIDMap.get(uid);
			this.tileIDMap.remove(uid);
			
			for(Long id : ids) {
				if(tile instanceof IExtractor && this.extractorMap.get(id) != null) this.extractorMap.get(id).remove((IExtractor)tile);
				if(tile instanceof IImporter && this.importerMap.get(id) != null) this.importerMap.get(id).remove((IImporter)tile);
				if(PipeRegistry.isCustomTile(tile.getClass()) && this.importerMap.get(id) != null) this.importerMap.get(id).remove(PipeRegistry.getCustomImporter(tile.getClass()));
			}
		}
	}

}
