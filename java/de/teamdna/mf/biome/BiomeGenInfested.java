package de.teamdna.mf.biome;

import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BiomeGenInfested extends BiomeGenBase{

	public BiomeGenInfested(int par1) {
		super(par1);
		this.setTemperatureRainfall(12, 12);
		this.flowers.clear();
		this.getSkyColorByTemp(50);
	}
	
	@SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_, int p_150558_3_) {
        double d0 = plantNoise.func_151601_a((double)p_150558_1_ * 0.0225D, (double)p_150558_3_ * 0.0225D);
        return 0x3f3f3f;
    }

    @SideOnly(Side.CLIENT)
    public int getBiomeFoliageColor(int p_150571_1_, int p_150571_2_, int p_150571_3_) {
        return 0x3f3f3f;
    }
    
    @Override
    public int getSkyColorByTemp(float par1) {
    	return 0x3f3f3f;
    }
    
    @Override
    public int getWaterColorMultiplier() {
    	return 0x005500;
    }

}
