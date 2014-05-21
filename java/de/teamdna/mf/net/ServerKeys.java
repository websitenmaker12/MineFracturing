package de.teamdna.mf.net;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import de.teamdna.mf.packet.PacketKeyUpdate;

public class ServerKeys {

	protected List<String> jumpKeyDown = new ArrayList<String>();
	
	public boolean isJumpKeyDown(EntityPlayer player) {
		return this.jumpKeyDown.contains(player.getCommandSenderName());
	}

	public void onReceivePacket(PacketKeyUpdate packet) {
		if(packet.id == 0) {
			if(!packet.pressed) this.jumpKeyDown.remove(packet.username);
			else this.jumpKeyDown.add(packet.username);
		}
	}
	
}
