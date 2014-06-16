package de.teamdna.mf.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.teamdna.core.packethandling.IPacket;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.Reference;
import de.teamdna.util.IOUtil;

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
	public void toBytes(ByteBuf buf, ChannelHandlerContext ctx) {
		buf.writeInt(this.id);
		IOUtil.writeString(buf, this.username);
		buf.writeBoolean(this.pressed);
	}

	@Override
	public void fromBytes(ByteBuf buf, ChannelHandlerContext ctx) {
		this.id = buf.readInt();
		this.username = IOUtil.readString(buf);
		this.pressed = buf.readBoolean();
	}

	@Override
	public void handle(Side side, IPacket packet, ChannelHandlerContext ctx, EntityPlayer player) {
		MineFracturing.keys.onReceivePacket(this);
	}

	@Override
	public String modid() {
		return Reference.modid;
	}

}
