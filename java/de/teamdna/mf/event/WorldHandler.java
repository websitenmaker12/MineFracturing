package de.teamdna.mf.event;

import net.minecraftforge.event.world.WorldEvent;
import de.teamdna.mf.tile.PipeNetworkController;

public class WorldHandler {

	public void worldUnloadEvent(WorldEvent.Unload event) {
		if(event.world.provider.dimensionId == 0) {
			PipeNetworkController.INSTNACE.dispose();
		}
	}
	
}
