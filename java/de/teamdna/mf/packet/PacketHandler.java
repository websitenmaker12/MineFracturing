package de.teamdna.mf.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.teamdna.mf.Reference;

@io.netty.channel.ChannelHandler.Sharable
public class PacketHandler extends MessageToMessageCodec<FMLProxyPacket, Packet> {

	private EnumMap<Side, FMLEmbeddedChannel> channels;
	private LinkedList<Class<? extends Packet>> packets = new LinkedList<Class<? extends Packet>>();
	private boolean isPostInit = false;
	
	public boolean registerPacket(Class<? extends Packet> packet) {
		if(this.isPostInit) return false;
		this.packets.add(packet);
		return true;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
		ByteBuf buffer = Unpooled.buffer();
		Class<? extends Packet> clazz = msg.getClass();
		if(!this.packets.contains(msg.getClass())) return;
		
		byte disc = (byte)this.packets.indexOf(clazz);
		buffer.writeByte(disc);
		msg.encode(ctx, buffer);
		FMLProxyPacket proPacket = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
		out.add(proPacket);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
		ByteBuf payload = msg.payload();
		byte disc = payload.readByte();
		Class<? extends Packet> clazz = this.packets.get(disc);
		if(clazz == null) return;
		
		Packet pkt = clazz.newInstance();
		ByteBuf stream = payload.slice();
		pkt.decode(ctx, stream);
		
		EntityPlayer player;
		switch(FMLCommonHandler.instance().getEffectiveSide()) {
			case CLIENT:
				player = this.getClientPlayer();
				pkt.handleClientSide(payload.slice(), pkt, player);
				break;
			case SERVER:
				INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
				player = ((NetHandlerPlayServer)netHandler).playerEntity;
				pkt.handleServerSide(payload.slice(), pkt, player);
				break;
			default: break;
		}
		
		out.add(pkt);
	}

	public void init() {
		this.channels = NetworkRegistry.INSTANCE.newChannel(Reference.modid, this);
	}
	
	public void postInit() {
		this.isPostInit = true;
		Collections.sort(this.packets, new Comparator<Class<? extends Packet>>() {
			public int compare(Class<? extends Packet> o1, Class<? extends Packet> o2) {
				int com = String.CASE_INSENSITIVE_ORDER.compare(o1.getCanonicalName(), o2.getCanonicalName());
				if(com == 0) com = o1.getCanonicalName().compareTo(o2.getCanonicalName());
				return com;
			}
		});
	}
	
	@SideOnly(Side.CLIENT)
	private EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	public void sendToAll(Packet pkt) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		this.channels.get(Side.SERVER).writeAndFlush(pkt);
	}
	
	public void sendToDimension(Packet pkt, int dimID) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimID);
		this.channels.get(Side.SERVER).writeAndFlush(pkt);
	}
	
	public void sendToServer(Packet pkt) {
		this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		this.channels.get(Side.CLIENT).writeAndFlush(pkt);
	}
	
}
