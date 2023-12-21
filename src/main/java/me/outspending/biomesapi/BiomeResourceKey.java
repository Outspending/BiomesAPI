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
@AsOf("0.0.1")
public record BiomeResourceKey(@NotNull ResourceLocation resourceLocation) {

    /**
     * Creates a new BiomeResourceKey from the given key and path.
     * This is a static factory method that provides a convenient way to create a new BiomeResourceKey instance.
     * The key and path are used to create a new ResourceLocation, which is then used to create the BiomeResourceKey.
     *
     * @param key  the key for the resource location
     * @param path the path for the resource location
     * @return a new BiomeResourceKey with the given key and path
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static @NotNull BiomeResourceKey of(@NotNull String key, @NotNull String path) {
        return new BiomeResourceKey(key, path);
    }

    /**
     * Creates a new BiomeResourceKey from the given ResourceLocation.
     * This is a static factory method that provides a convenient way to create a new BiomeResourceKey instance.
     * The ResourceLocation is used directly to create the BiomeResourceKey.
     *
     * @param resourceLocation the ResourceLocation to create the BiomeResourceKey from
     * @return a new BiomeResourceKey with the given ResourceLocation
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static @NotNull BiomeResourceKey of(@NotNull ResourceLocation resourceLocation) {
        return new BiomeResourceKey(resourceLocation);
    }

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
        this(new ResourceLocation(key.toLowerCase(), path.toLowerCase()));
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

    /**
     * Overrides the equals method from the Object class.
     * This method is used to compare this BiomeResourceKey with another object.
     *
     * @param obj the object to compare this BiomeResourceKey with
     * @return true if the given object is a BiomeResourceKey and its namespace and path are equal to this BiomeResourceKey's namespace and path, false otherwise
     * @version 0.0.1
     */
    @Override
    @AsOf("0.0.1")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        BiomeResourceKey key = (BiomeResourceKey) obj;
        String namespace = key.resourceLocation().getNamespace();
        String path = key.resourceLocation().getPath();

        return namespace.equals(this.resourceLocation.getNamespace()) && path.equals(this.resourceLocation.getPath());
    }

}