package de.teamdna.mf.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class Packet {

	public abstract void encode(ChannelHandlerContext ctx, ByteBuf buffer);
	public abstract void decode(ChannelHandlerContext ctx, ByteBuf buffer);
	public abstract void handleClientSide(ByteBuf stream, Packet packet, EntityPlayer player);
	public abstract void handleServerSide(ByteBuf stream, Packet packet, EntityPlayer player);
	
}
