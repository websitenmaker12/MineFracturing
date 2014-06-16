package de.teamdna.mf.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.teamdna.core.packethandling.IPacket;
import de.teamdna.mf.Reference;
import de.teamdna.util.CoreUtil;

public class PacketBiomUpdate implements IPacket {

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
	public void toBytes(ByteBuf buf, ChannelHandlerContext ctx) {
		buf.writeInt(this.x);
		buf.writeInt(this.z);
		buf.writeInt(this.biomeID);
	}

	@Override
	public void fromBytes(ByteBuf buf, ChannelHandlerContext ctx) {
		this.x = buf.readInt();
		this.z = buf.readInt();
		this.biomeID = buf.readInt();
	}

	@Override
	public void handle(Side side, IPacket packet, ChannelHandlerContext ctx, EntityPlayer player) {
		CoreUtil.setBiomeForCoords(player.worldObj, this.x, this.z, this.biomeID);
		player.worldObj.markBlockForUpdate(this.x, player.worldObj.getHeightValue(this.x, this.z), this.z);
	}

	@Override
	public String modid() {
		return Reference.modid;
	}

}
