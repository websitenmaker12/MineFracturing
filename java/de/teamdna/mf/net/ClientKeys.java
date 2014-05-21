package de.teamdna.mf.net;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import de.teamdna.mf.MineFracturing;
import de.teamdna.mf.packet.PacketKeyUpdate;

public class ClientKeys extends ServerKeys {

	private Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public boolean isJumpKeyDown(EntityPlayer player) {
		String username = player.getCommandSenderName();
		
		boolean pressed = this.mc.gameSettings.keyBindJump.getIsKeyPressed() && Minecraft.getMinecraft().currentScreen == null;
		boolean hasChanged = false;
		
		if(this.jumpKeyDown.contains(username) && !pressed) {
			this.jumpKeyDown.remove(username);
			hasChanged = true;
		} else if(!this.jumpKeyDown.contains(username) && pressed) {
			this.jumpKeyDown.add(username);
			hasChanged = true;
		}
		
		if(hasChanged) {
			MineFracturing.packetHandler.sendToServer(new PacketKeyUpdate(0, username, pressed));
		}
		
		return this.jumpKeyDown.contains(username);
	}
	
}
