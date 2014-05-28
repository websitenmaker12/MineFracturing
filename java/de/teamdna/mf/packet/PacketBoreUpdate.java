package de.teamdna.mf.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import de.teamdna.mf.tile.TileEntityBore;

public class PacketBoreUpdate extends Packet {

	private int dim, x, y, z, clientState;
	private int radius, state, oreBlocks, totalOres;
	
	public PacketBoreUpdate() {
	}
	
	public PacketBoreUpdate(TileEntityBore tile, int state) {
		this.dim = tile.getWorldObj().provider.dimensionId;
		this.x = tile.xCoord;
		this.y = tile.yCoord;
		this.z = tile.zCoord;
		this.clientState = state;
		
		this.radius = tile.radius;
		this.state = tile.state;
		this.oreBlocks = tile.oreBlocks.size();
		this.totalOres = tile.totalOres;
	}
	
	@Override
	public void encode(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.dim);
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		buffer.writeInt(this.clientState);
		buffer.writeInt(this.radius);
		buffer.writeInt(this.state);
		buffer.writeInt(this.oreBlocks);
		buffer.writeInt(this.totalOres);
	}

	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.dim = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.clientState = buffer.readInt();
		this.radius = buffer.readInt();
		this.state = buffer.readInt();
		this.oreBlocks = buffer.readInt();
		this.totalOres = buffer.readInt();
	}

	@Override
	public void handleClientSide(ByteBuf stream, Packet packet, EntityPlayer player) {
		if(player.worldObj.provider.dimensionId == this.dim) {
			TileEntity tile = player.worldObj.getTileEntity(this.x, this.y, this.z);
			if(tile != null && tile instanceof TileEntityBore) {
				TileEntityBore bore = (TileEntityBore)tile;
				bore.pauseClientDummy = this.clientState == 0;
				bore.radius = this.radius;
				bore.state = this.state;
				bore.clientOreBlocksSize = this.oreBlocks;
				bore.totalOres = this.totalOres;
			}
		}
	}

	@Override
	public void handleServerSide(ByteBuf stream, Packet packet, EntityPlayer player) {
	}

}
