package me.outspending.biomesapi.biome;

import com.google.common.base.Preconditions;
import me.outspending.biomesapi.annotations.AsOf;
import me.outspending.biomesapi.exceptions.UnknownBiomeException;
import me.outspending.biomesapi.registry.BiomeResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a utility class that provides static methods to interact with the biome registry in a Minecraft server.
 * It provides methods to retrieve a Biome object from the registry and to check if a biome exists in the registry.
 * This class uses the Bukkit API to interact with the Minecraft server.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
public class BiomeHandler {

    private static final List<CustomBiome> registeredBiomes = new ArrayList<>();

    public BiomeHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    /**
     * This method gets the registered biomes list
     *
     * @version 0.0.1
     * @return
     */
    @AsOf("0.0.1")
    public static List<CustomBiome> getRegisteredBiomes() {
        return registeredBiomes;
    }

    /**
     * This method retrieves a Biome object from the Minecraft server's biome registry.
     * It uses the Bukkit API to get the server instance and then accesses the server's biome registry.
     * If the biome registry exists, it retrieves the Biome object corresponding to the provided BiomeResourceKey.
     * If the biome registry does not exist or the Biome object does not exist in the registry, it returns null.
     *
     * @param resourceKey The BiomeResourceKey for the biome that needs to be retrieved.
     * @return Biome object corresponding to the provided BiomeResourceKey, or null if the biome does not exist.
     *
     * @throws UnknownBiomeException if the biome does not exist in the registry.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static @Nullable CustomBiome getBiome(@NotNull BiomeResourceKey resourceKey) {
        Preconditions.checkNotNull(resourceKey, "resourceKey cannot be null");

        return registeredBiomes.stream()
                .filter(b -> resourceKey.equals(b.getResourceKey()))
                .findFirst()
                .orElseThrow(() -> new UnknownBiomeException("Unknown biome: " + resourceKey));
    }

    /**
     * This method checks if a biome exists in the Minecraft server's biome registry.
     * It uses the getBiome method to retrieve the Biome object corresponding to the provided BiomeResourceKey.
     * If the Biome object exists, it returns true. Otherwise, it returns false.
     *
     * @param resourceKey The BiomeResourceKey for the biome that needs to be checked.
     * @return true if the biome exists in the registry, false otherwise.
     *
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static boolean isBiome(@NotNull BiomeResourceKey resourceKey) {
        Preconditions.checkNotNull(resourceKey, "resourceKey cannot be null");

        return getBiome(resourceKey) != null;
    }

}
