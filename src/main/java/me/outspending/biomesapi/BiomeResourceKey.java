package me.outspending.biomesapi;

import lombok.Getter;
import me.outspending.biomesapi.annotations.AsOf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a key for a biome resource in the game.
 * It uses the @AsOf annotation to indicate the version since the class or its methods have been present or modified.
 * It is a final class, meaning it cannot be subclassed.
 *
 * @version 0.0.1
 */
@Getter
@AsOf("0.0.1")
public final class BiomeResourceKey {

    /**
     * The ResourceLocation object that this key represents.
     */
    private final ResourceLocation resourceLocation;

    /**
     * Constructs a new BiomeResourceKey with the given key and path.
     * The key and path are converted to lower case before being used to create a new ResourceLocation.
     *
     * @param key  the key for the resource location
     * @param path the path for the resource location
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public BiomeResourceKey(@NotNull String key, @NotNull String path) {
        this.resourceLocation = new ResourceLocation(key.toLowerCase(), path.toLowerCase());
    }

    /**
     * Returns a string representation of the BiomeResourceKey.
     * The string is in the format "namespace:path".
     *
     * @return a string representation of the BiomeResourceKey
     * @version 0.0.1
     */
    @Override
    @AsOf("0.0.1")
    public String toString() {
        return resourceLocation.getNamespace() + ":" + resourceLocation.getPath();
    }

}