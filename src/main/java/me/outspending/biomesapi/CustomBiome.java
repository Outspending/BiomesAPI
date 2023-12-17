package me.outspending.biomesapi;

import lombok.Getter;
import lombok.Setter;
import me.outspending.biomesapi.annotations.AsOf;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a custom biome in Minecraft.
 * It includes properties such as a BiomeResourceKey, BiomeSettings, and various color settings.
 * It also includes a nested Builder class for creating instances of CustomBiome.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
@Getter @Setter
public final class CustomBiome {

    // Required Settings
    private final BiomeResourceKey resourceKey;
    private final BiomeSettings settings;

    // Required Colors
    private final int fogColor;
    private final int waterColor;
    private final int waterFogColor;
    private final int skyColor;

    // Optional Colors
    private int foliageColor = 0;
    private int grassColor = 0;

    /**
     * This constructor creates a new CustomBiome object with the required settings and colors.
     *
     * @param resourceKey The BiomeResourceKey for the custom biome.
     * @param settings The BiomeSettings for the custom biome.
     * @param fogColor The fog color for the custom biome.
     * @param waterColor The water color for the custom biome.
     * @param waterFogColor The water fog color for the custom biome.
     * @param skyColor The sky color for the custom biome.
     *
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public CustomBiome(
            @NotNull BiomeResourceKey resourceKey,
            @NotNull BiomeSettings settings,

            @NotNull String fogColor,
            @NotNull String waterColor,
            @NotNull String waterFogColor,
            @NotNull String skyColor
    ) {
        this.resourceKey = resourceKey;
        this.settings = settings;
        this.fogColor = Integer.parseInt(fogColor, 16);
        this.waterColor = Integer.parseInt(waterColor, 16);
        this.waterFogColor = Integer.parseInt(waterFogColor, 16);
        this.skyColor = Integer.parseInt(skyColor, 16);
    }

    /**
     * This constructor creates a new CustomBiome object with the required settings and colors, as well as optional foliage and grass colors.
     *
     * @param resourceKey The BiomeResourceKey for the custom biome.
     * @param settings The BiomeSettings for the custom biome.
     * @param fogColor The fog color for the custom biome.
     * @param waterColor The water color for the custom biome.
     * @param waterFogColor The water fog color for the custom biome.
     * @param skyColor The sky color for the custom biome.
     * @param foliageColor The foliage color for the custom biome.
     * @param grassColor The grass color for the custom biome.
     *
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public CustomBiome(
            @NotNull BiomeResourceKey resourceKey,
            @NotNull BiomeSettings settings,

            @NotNull String fogColor,
            @NotNull String waterColor,
            @NotNull String waterFogColor,
            @NotNull String skyColor,
            @NotNull String foliageColor,
            @NotNull String grassColor
    ) {
        this(resourceKey, settings, fogColor, waterColor, waterFogColor, skyColor);
        this.foliageColor = Integer.parseInt(foliageColor, 16);
        this.grassColor = Integer.parseInt(grassColor, 16);
    }

    /**
     * This method converts the BiomeResourceKey of the custom biome to a NamespacedKey.
     *
     * @version 0.0.1
     * @return The NamespacedKey corresponding to the BiomeResourceKey of the custom biome.
     */
    @AsOf("0.0.1")
    public NamespacedKey toNamespacedKey() {
        ResourceLocation location = resourceKey.getResourceLocation();
        return new NamespacedKey(location.getNamespace(), location.getPath());
    }

    /**
     * This method creates a new Builder object for creating instances of CustomBiome.
     *
     * @version 0.0.1
     * @return a new Builder object.
     */
    @AsOf("0.0.1")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * This is a nested Builder class for creating instances of CustomBiome.
     * It uses the Builder pattern, where you call a chain of methods to set the properties,
     * and then call build() to create the object.
     *
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static final class Builder {

        private BiomeResourceKey resourceKey;
        private BiomeSettings settings;

        private String fogColor;
        private String waterColor;
        private String waterFogColor;
        private String skyColor;

        private String foliageColor;
        private String grassColor;

        /**
         * This method sets the BiomeResourceKey property of the CustomBiome.
         *
         * @param resourceKey The BiomeResourceKey of the custom biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder resourceKey(@NotNull BiomeResourceKey resourceKey) {
            this.resourceKey = resourceKey;
            return this;
        }

        /**
         * This method sets the BiomeSettings property of the CustomBiome.
         *
         * @param settings The BiomeSettings of the custom biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder settings(@NotNull BiomeSettings settings) {
            this.settings = settings;
            return this;
        }

        /**
         * This method sets the fog color property of the CustomBiome.
         *
         * @param fogColor The fog color of the custom biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder fogColor(@NotNull String fogColor) {
            this.fogColor = fogColor;
            return this;
        }

        /**
         * This method sets the water color property of the CustomBiome.
         *
         * @param waterColor The water color of the custom biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder waterColor(@NotNull String waterColor) {
            this.waterColor = waterColor;
            return this;
        }

        /**
         * This method sets the water fog color property of the CustomBiome.
         *
         * @param waterFogColor The water fog color of the custom biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder waterFogColor(@NotNull String waterFogColor) {
            this.waterFogColor = waterFogColor;
            return this;
        }

        /**
         * This method sets the sky color property of the CustomBiome.
         *
         * @param skyColor The sky color of the custom biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder skyColor(@NotNull String skyColor) {
            this.skyColor = skyColor;
            return this;
        }

        /**
         * This method sets the foliage color property of the CustomBiome.
         *
         * @param foliageColor The foliage color of the custom biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder foliageColor(@NotNull String foliageColor) {
            this.foliageColor = foliageColor;
            return this;
        }

        /**
         * This method sets the grass color property of the CustomBiome.
         *
         * @param grassColor The grass color of the custom biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder grassColor(@NotNull String grassColor) {
            this.grassColor = grassColor;
            return this;
        }

        /**
         * This method creates a new CustomBiome object with the properties set in the Builder.
         *
         * @version 0.0.1
         * @return a new CustomBiome object.
         */
        @AsOf("0.0.1")
        public @NotNull CustomBiome build() {
            return new CustomBiome(resourceKey, settings, fogColor, waterColor, waterFogColor, skyColor, foliageColor, grassColor);
        }

    }

}
