package de.teamdna.mf.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class PacketChunkUpdate extends Packet {

	public World world;
	public List<Chunk> chunks;
	
	public Map<ChunkCoordIntPair, byte[]> coords;
	public int dimID;
	
	public PacketChunkUpdate() {
	}
	
	public PacketChunkUpdate(World worldObj, List<Chunk> chunks) {
		this.world = worldObj;
		this.chunks = chunks;
	}

	@Override
	public void encode(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.world.provider.dimensionId);
		buffer.writeInt(this.chunks.size());
		
		for(Chunk chunk : this.chunks) {
			if(chunk == null) continue;
			buffer.writeInt(chunk.xPosition);
			buffer.writeInt(chunk.zPosition);
			buffer.writeBytes(chunk.getBiomeArray());
		}
	}

	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.coords = new HashMap<ChunkCoordIntPair, byte[]>();
		
		this.dimID = buffer.readInt();
		int size = buffer.readInt();
		for(int i = 0; i < size; i++) {
			int x = buffer.readInt();
			int z = buffer.readInt();
			byte[] arr = new byte[256];
			buffer.readBytes(arr);
			this.coords.put(new ChunkCoordIntPair(x, z), arr);
		}
	}

	@Override
	public void handleClientSide(ByteBuf stream, Packet packet, EntityPlayer player) {
		if(player.worldObj.provider.dimensionId != this.dimID) return;
		for(Map.Entry<ChunkCoordIntPair, byte[]> pair : this.coords.entrySet()) {
			Chunk chunk = player.worldObj.getChunkFromChunkCoords(pair.getKey().chunkXPos, pair.getKey().chunkZPos);
			chunk.setBiomeArray(pair.getValue());
			player.worldObj.setBlock(chunk.xPosition * 16, 255, chunk.zPosition * 16, Blocks.glass);
			player.worldObj.setBlockToAir(chunk.xPosition * 16, 255, chunk.zPosition * 16);
		}
	}

	@Override
	public void handleServerSide(ByteBuf stream, Packet packet, EntityPlayer player) {
	}

}
