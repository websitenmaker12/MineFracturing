package de.teamdna.mf.tile;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IConnectable {

	boolean canConnect(ForgeDirection direction);
	World getWorld();
	int getX();
	int getY();
	int getZ();
	
}
