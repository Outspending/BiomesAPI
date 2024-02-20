package me.outspending.biomesapi.registry.handlers;

import me.outspending.biomesapi.biome.CustomBiome;
import me.outspending.biomesapi.registry.BuilderHandler;
import me.outspending.biomesapi.renderer.ParticleRenderer;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.jetbrains.annotations.NotNull;

public class ParticleRendererHandler implements BuilderHandler<BiomeSpecialEffects.Builder, ParticleRenderer> {

    @Override
    public void handle(ParticleRenderer value, @NotNull BiomeSpecialEffects.Builder key) {
        if (value == null) return;

        key.ambientParticle(new AmbientParticleSettings(
                value.ambientParticle().getParticle(),
                value.probability()
        ));
    }

    @Override
    public ParticleRenderer build(@NotNull CustomBiome biome) {
        return null;
    }
}
