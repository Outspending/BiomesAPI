package me.outspending.biomesapi.registry;

import com.google.common.base.Preconditions;
import me.outspending.biomesapi.BiomeLock;
import me.outspending.biomesapi.BiomeSettings;
import me.outspending.biomesapi.annotations.AsOf;
import me.outspending.biomesapi.biome.BiomeHandler;
import me.outspending.biomesapi.biome.CustomBiome;
import me.outspending.biomesapi.nms.NMSHandler;
import me.outspending.biomesapi.registry.handlers.SpecialEffectsHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the BiomeRegistry interface and provides a method to register a custom biome to a Minecraft server.
 * It uses the BiomeLock class to unlock the biome registry before registering the custom biome, and then freezes the registry again.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
public class CustomBiomeRegistry implements BiomeRegistry {
    private static final SpecialEffectsHandler effectsHandler = new SpecialEffectsHandler();

    /**
     * This method registers a custom biome to a Minecraft server.
     * It first unlocks the biome registry using the BiomeLock class.
     * Then, it retrieves the biome registry from the server and creates a new Biome object with the settings and colors from the CustomBiome object.
     * The new Biome object is then registered to the biome registry using the ResourceLocation from the CustomBiome object.
     * Finally, it freezes the biome registry again using the BiomeLock class.
     *
     * @param biome The CustomBiome object that should be registered to the server.
     * @version 0.0.1
     */
    @Override
    @AsOf("0.0.1")
    @SuppressWarnings("unchecked")
    public void register(@NotNull CustomBiome biome) {
        Preconditions.checkNotNull(biome, "biome cannot be null");

        BiomeLock.unlock(() -> {

            // Retrieve the biome registry from NMS
            NMSHandler.executeNMS(nms -> {
                Registry<Biome> registry = (Registry<Biome>) nms.getRegistry();

                // Get the ResourceLocation and BiomeSettings from the CustomBiome object
                ResourceLocation resourceLocation = biome.getResourceKey().resourceLocation();
                BiomeSettings settings = biome.getSettings();

                // Build the Biome object
                Biome.BiomeBuilder biomeBuilder = new Biome.BiomeBuilder()
                        .downfall(settings.downfall())
                        .temperature(settings.temperature())
                        .temperatureAdjustment(settings.modifier().getModifier())
                        .mobSpawnSettings(MobSpawnSettings.EMPTY)
                        .generationSettings(BiomeGenerationSettings.EMPTY);

                // Create a new Biome object with the settings and colors from the CustomBiome object
                BiomeSpecialEffects effects = effectsHandler.build(biome);
                effectsHandler.handle(effects, biomeBuilder);

                // Register the new Biome object to the biome registry
                Biome createdBiome = biomeBuilder.build();
                Registry.register(registry, resourceLocation, createdBiome);

                // Add the custom biome to the list of registered biomes
                BiomeHandler.getRegisteredBiomes().add(biome);
            });

            return null;
        });
    }

}
