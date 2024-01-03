package me.outspending.biomesapi.biome;

import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

public class BiomeCreator {

    private final Biome.BiomeBuilder biomeBuilder;
    private final CustomBiome biome;

    public BiomeCreator(@NotNull CustomBiome biome) {
        this.biomeBuilder = new Biome.BiomeBuilder();
        this.biome = biome;
    }

    public void checkField(Object obj) {

    }

    public CustomBiome getBiome() {
        return biome;
    }

}
