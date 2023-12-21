package me.outspending.biomesapi;

import me.outspending.biomesapi.annotations.AsOf;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.bukkit.Bukkit;
import org.bukkit.MusicInstrument;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the BiomeRegistry interface and provides a method to register a custom biome to a Minecraft server.
 * It uses the BiomeLock class to unlock the biome registry before registering the custom biome, and then freezes the registry again.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
public class CustomBiomeRegistry implements BiomeRegistry {

    /**
     * This method registers a custom biome to a Minecraft server.
     * It first unlocks the biome registry using the BiomeLock class.
     * Then, it retrieves the biome registry from the server and creates a new Biome object with the settings and colors from the CustomBiome object.
     * The new Biome object is then registered to the biome registry using the ResourceLocation from the CustomBiome object.
     * Finally, it freezes the biome registry again using the BiomeLock class.
     *
     * @param server The DedicatedServer object to which the custom biome should be registered.
     * @param biome The CustomBiome object that should be registered to the server.
     * @version 0.0.1
     */
    @Override
    @AsOf("0.0.1")
    public void register(@NotNull DedicatedServer server, @NotNull CustomBiome biome) {
        BiomeLock.unlock(() -> {
            // Retrieve the biome registry from the server
            Registry<Biome> registry = server.registryAccess().registry(Registries.BIOME)
                    .orElseThrow(() -> new IllegalStateException("Biome registry not available"));

            // Get the ResourceLocation and BiomeSettings from the CustomBiome object
            ResourceLocation resourceLocation = biome.getResourceKey().resourceLocation();
            BiomeSettings settings = biome.getSettings();

            // Create a new Biome object with the settings and colors from the CustomBiome object
            BiomeSpecialEffects.Builder effectsBuilder = new BiomeSpecialEffects.Builder()
                    .fogColor(biome.getFogColor())
                    .foliageColorOverride(biome.getFoliageColor())
                    .skyColor(biome.getSkyColor())
                    .waterColor(biome.getWaterColor())
                    .waterFogColor(biome.getWaterFogColor())
                    .grassColorOverride(biome.getGrassColor());

            // Check if a custom particle renderer is provided
            ParticleRenderer renderer;
            if ((renderer = biome.getParticleRenderer()) != null) {
                Bukkit.broadcastMessage("Custom particle renderer found");
                effectsBuilder.ambientParticle(new AmbientParticleSettings(
                        renderer.ambientParticle().getParticle(),
                        renderer.probability()
                ));
            }

            // Build the Biome object
            Biome createdBiome = new Biome.BiomeBuilder()
                    .downfall(settings.downfall())
                    .temperature(settings.temperature())
                    .temperatureAdjustment(settings.modifier().getModifier())
                    .mobSpawnSettings(MobSpawnSettings.EMPTY)
                    .generationSettings(BiomeGenerationSettings.EMPTY)
                    .specialEffects(effectsBuilder.build())
                    .build();

            // Register the new Biome object to the biome registry
            Registry.register(registry, resourceLocation, createdBiome);

            // Add the custom biome to the list of registered biomes
            BiomeHandler.getRegisteredBiomes().add(biome);

            return null;
        });
    }


}
