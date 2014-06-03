package de.teamdna.mf.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import de.teamdna.mf.util.Util;

public class PacketBiomUpdate extends Packet {

	private int x;
	private int z;
	private int biomeID;
	
	public PacketBiomUpdate() {
	}
	
	public PacketBiomUpdate(int x, int z, int biomID) {
		this.x = x;
		this.z = z;
		this.biomeID = biomID;
	}
	
	@Override
	public void encode(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.x);
		buffer.writeInt(this.z);
		buffer.writeInt(this.biomeID);
	}

	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.x = buffer.readInt();
		this.z = buffer.readInt();
		this.biomeID = buffer.readInt();
	}

	@Override
	public void handleClientSide(ByteBuf stream, Packet packet, EntityPlayer player) {
		Util.setBiomeForCoords(player.worldObj, this.x, this.z, this.biomeID);
		player.worldObj.markBlockForUpdate(this.x, player.worldObj.getHeightValue(this.x, this.z), this.z);
	}

	@Override
	public void handleServerSide(ByteBuf stream, Packet packet, EntityPlayer player) {
	}

}
