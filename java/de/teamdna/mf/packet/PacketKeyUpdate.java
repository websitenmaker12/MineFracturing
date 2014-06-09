package de.teamdna.mf.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import de.teamdna.core.packethandling.IPacket;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.Reference;
import de.teamdna.util.BufferUtil;

public class PacketKeyUpdate implements IPacket {

	public PacketKeyUpdate() {
	}
	
	public int id;
	public String username;
	public boolean pressed;
	
	public PacketKeyUpdate(int id, String username, boolean pressed) {
		this.id = id;
		this.username = username;
		this.pressed = pressed;
	}
	
	@Override
	public String getModID() {
		return Reference.modid;
	}
	
	@Override
	public void encode(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.id);
		BufferUtil.writeString(buffer, this.username);
		buffer.writeBoolean(this.pressed);
	}

	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.id = buffer.readInt();
		this.username = BufferUtil.readString(buffer);
		this.pressed = buffer.readBoolean();
	}

	@Override
	public void handleClientSide(ByteBuf stream, IPacket packet, EntityPlayer player) {
	}

	@Override
	public void handleServerSide(ByteBuf stream, IPacket packet, EntityPlayer player) {
		MineFracturing.keys.onReceivePacket(this);
	}

}
