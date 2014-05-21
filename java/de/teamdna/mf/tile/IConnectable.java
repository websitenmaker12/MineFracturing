package de.teamdna.mf.tile;

import net.minecraftforge.common.util.ForgeDirection;

public interface IConnectable {

	boolean canConnect(ForgeDirection direction);
	
}
