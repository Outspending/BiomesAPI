package me.outspending.biomesapi.biome;

import com.google.common.base.Preconditions;
import me.outspending.biomesapi.registry.BiomeResourceKey;
import me.outspending.biomesapi.BiomeSettings;
import me.outspending.biomesapi.renderer.ParticleRenderer;
import me.outspending.biomesapi.annotations.AsOf;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

@AsOf("0.0.2")
public interface CustomBiome {

    /**
     * Returns a new instance of the Builder class.
     *
     * @return a new Builder instance
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Returns the NamespacedKey of the CustomBiome.
     *
     * @return the NamespacedKey of the CustomBiome
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    @NotNull NamespacedKey toNamespacedKey();

    /**
     * Returns the BiomeResourceKey of the CustomBiome.
     *
     * @return the BiomeResourceKey of the CustomBiome
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    @NotNull BiomeResourceKey getResourceKey();

    /**
     * Returns the BiomeSettings of the CustomBiome.
     *
     * @return the BiomeSettings of the CustomBiome
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    @NotNull BiomeSettings getSettings();

    /**
     * Returns the fog color of the CustomBiome.
     *
     * @return the fog color of the CustomBiome
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    int getFogColor();

    /**
     * Returns the water color of the CustomBiome.
     *
     * @return the water color of the CustomBiome
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    int getWaterColor();

    /**
     * Returns the water fog color of the CustomBiome.
     *
     * @return the water fog color of the CustomBiome
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    int getWaterFogColor();

    /**
     * Returns the sky color of the CustomBiome.
     *
     * @return the sky color of the CustomBiome
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    int getSkyColor();

    /**
     * Returns the foliage color of the CustomBiome.
     *
     * @return the foliage color of the CustomBiome
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    int getFoliageColor();

    /**
     * Returns the grass color of the CustomBiome.
     *
     * @return the grass color of the CustomBiome
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    int getGrassColor();

    /**
     * Returns the ParticleRenderer of the CustomBiome.
     *
     * @return the ParticleRenderer of the CustomBiome
     * @since 0.0.2
     */
    @AsOf("0.0.2")
    @NotNull ParticleRenderer getParticleRenderer();

    /**
     * This class is used to create a new CustomBiome object.
     * It provides methods to set the properties of the CustomBiome.
     *
     * @version 0.0.2
     */
    @AsOf("0.0.2")
    class Builder {

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

            return new CustomBiomeImpl(
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
