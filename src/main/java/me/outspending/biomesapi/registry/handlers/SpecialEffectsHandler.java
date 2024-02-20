package me.outspending.biomesapi.registry.handlers;

import me.outspending.biomesapi.annotations.AsOf;
import me.outspending.biomesapi.biome.CustomBiome;
import me.outspending.biomesapi.registry.BuilderHandler;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.jetbrains.annotations.NotNull;

@AsOf("0.1.0")
public class SpecialEffectsHandler implements BuilderHandler<Biome.BiomeBuilder, BiomeSpecialEffects> {
    private static final ParticleRendererHandler renderHandler = new ParticleRendererHandler();

    @Override
    public void handle(BiomeSpecialEffects value, @NotNull Biome.BiomeBuilder builder) {
        if (value == null) return;

        builder.specialEffects(value);
    }

    @Override
    public BiomeSpecialEffects build(@NotNull CustomBiome biome) {
        BiomeSpecialEffects.Builder builder = new BiomeSpecialEffects.Builder()
                .fogColor(biome.getFogColor())
                .foliageColorOverride(biome.getFoliageColor())
                .skyColor(biome.getSkyColor())
                .waterColor(biome.getWaterColor())
                .waterFogColor(biome.getWaterFogColor())
                .grassColorOverride(biome.getGrassColor());

        renderHandler.handle(biome.getParticleRenderer(), builder);
        return builder.build();
    }

}
