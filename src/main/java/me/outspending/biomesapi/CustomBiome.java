package me.outspending.biomesapi;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import me.outspending.biomesapi.annotations.AsOf;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.Color;
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

    // Optional Settings
    private ParticleRenderer particleRenderer;

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

            int fogColor,
            int waterColor,
            int waterFogColor,
            int skyColor,

            @NotNull ParticleRenderer particleRenderer
    ) {
        this.resourceKey = resourceKey;
        this.settings = settings;
        this.particleRenderer = particleRenderer;

        this.fogColor = fogColor;
        this.waterColor = waterColor;
        this.waterFogColor = waterFogColor;
        this.skyColor = skyColor;
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

            int fogColor,
            int waterColor,
            int waterFogColor,
            int skyColor,
            int foliageColor,
            int grassColor,

            @NotNull ParticleRenderer particleRenderer
    ) {
        this(resourceKey, settings, fogColor, waterColor, waterFogColor, skyColor, particleRenderer);
        this.foliageColor = foliageColor;
        this.grassColor = grassColor;
    }

    /**
     * This method converts the BiomeResourceKey of the custom biome to a NamespacedKey.
     *
     * @version 0.0.1
     * @return The NamespacedKey corresponding to the BiomeResourceKey of the custom biome.
     */
    @AsOf("0.0.1")
    public NamespacedKey toNamespacedKey() {
        ResourceLocation location = resourceKey.resourceLocation();
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

        private int fogColor;
        private int waterColor;
        private int waterFogColor;
        private int skyColor;

        private int foliageColor;
        private int grassColor;

        private ParticleRenderer particleRenderer;

        /**
         * Formats a hexadecimal color string by removing the leading '#' if present.
         * This method is used to ensure that the color strings are in the correct format for parsing.
         *
         * @param color the color string to format
         * @return the formatted color string
         * @version 0.0.1
         */
        @AsOf("0.0.1")
        private String formatHex(@NotNull String color) {
            if (color.startsWith("#")) color = color.substring(1);
            return color;
        }

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
            this.fogColor = Integer.parseInt(formatHex(fogColor), 16);
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
        public @NotNull Builder fogColor(@NotNull Color fogColor) {
            this.fogColor = fogColor.asRGB();
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
            this.waterColor = Integer.parseInt(formatHex(waterColor), 16);
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
        public @NotNull Builder waterColor(@NotNull Color waterColor) {
            this.waterColor = waterColor.asRGB();
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
            this.waterFogColor = Integer.parseInt(formatHex(waterFogColor), 16);
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
        public @NotNull Builder waterFogColor(@NotNull Color waterFogColor) {
            this.waterFogColor = waterFogColor.asRGB();
            return this;
        }

        /**
         * This method sets the sky color property of the CustomBiome.
         *
         * @param skyColor The sky color of the custom biome.
         * @version 0.1.0
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder skyColor(@NotNull String skyColor) {
            this.skyColor = Integer.parseInt(formatHex(skyColor), 16);
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
        public @NotNull Builder skyColor(@NotNull Color skyColor) {
            this.skyColor = skyColor.asRGB();
            return this;
        }

        /**
         * This method sets the foliage color property of the CustomBiome.
         *
         * @param foliageColor The foliage color of the custom biome.
         * @version 0.1.0
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder foliageColor(@NotNull String foliageColor) {
            this.foliageColor = Integer.parseInt(formatHex(foliageColor), 16);
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
        public @NotNull Builder foliageColor(@NotNull Color foliageColor) {
            this.foliageColor = foliageColor.asRGB();
            return this;
        }

        /**
         * This method sets the grass color property of the CustomBiome.
         *
         * @param grassColor The grass color of the custom biome.
         * @version 0.1.0
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder grassColor(@NotNull String grassColor) {
            this.grassColor = Integer.parseInt(formatHex(grassColor), 16);
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
        public @NotNull Builder grassColor(@NotNull Color grassColor) {
            this.grassColor = grassColor.asRGB();
            return this;
        }

        /**
         * This method sets the particle renderer property of the CustomBiome.
         *
         * @param particleRenderer The particle renderer of the custom biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder particleRenderer(@NotNull ParticleRenderer particleRenderer) {
            this.particleRenderer = particleRenderer;
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
            Preconditions.checkArgument(resourceKey != null, "Resource key must be set");
            Preconditions.checkArgument(settings != null, "Settings must be set");

            return new CustomBiome(
                    resourceKey,
                    settings,
                    fogColor,
                    waterColor,
                    waterFogColor,
                    skyColor,
                    foliageColor,
                    grassColor,
                    particleRenderer
            );
        }

    }

}
