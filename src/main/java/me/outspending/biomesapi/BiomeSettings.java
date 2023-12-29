package me.outspending.biomesapi;

import me.outspending.biomesapi.annotations.AsOf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * This is a record class that represents the settings for a biome in Minecraft.
 * It includes properties such as depth, scale, temperature, downfall, and a temperature modifier.
 * It also includes a nested Builder class for creating instances of BiomeSettings.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
public record BiomeSettings(
        @Range(from = 0, to = 25) float depth,
        @Range(from = 0, to = 25) float scale,
        @Range(from = 0, to = 25) float temperature,
        @Range(from = 0, to = 25) float downfall,
        @NotNull BiomeTempModifier modifier
) {

    /**
     * This method creates a new Builder object for creating instances of BiomeSettings.
     *
     * @version 0.0.1
     * @return a new Builder object.
     */
    @AsOf("0.0.1")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * This method creates a new BiomeSettings object with default settings.
     * The default settings are a depth of 0.1, a scale of 0.2, a temperature of 0.5, a downfall of 0.5, and a temperature modifier of NONE.
     *
     * @version 0.0.1
     * @return a new BiomeSettings object with default settings.
     */
    @AsOf("0.0.1")
    public static @NotNull BiomeSettings defaultSettings() {
        return new BiomeSettings(0.1F, 0.2F, 0.5F, 0.5F, BiomeTempModifier.NONE);
    }

    /**
     * This is a nested Builder class for creating instances of BiomeSettings.
     * It uses the Builder pattern, where you call a chain of methods to set the properties,
     * and then call build() to create the object.
     *
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static final class Builder {

        private float depth = 0.1F;
        private float scale = 0.2F;
        private float temperature = 0.5F;
        private float downfall = 0.5F;
        private BiomeTempModifier modifier = BiomeTempModifier.NONE;

        /**
         * This method sets the depth property of the BiomeSettings.
         *
         * @param depth The depth of the biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder depth(@Range(from = 0, to = 25) float depth) {
            this.depth = depth;
            return this;
        }

        /**
         * This method sets the scale property of the BiomeSettings.
         *
         * @param scale The scale of the biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder scale(@Range(from = 0, to = 25) float scale) {
            this.scale = scale;
            return this;
        }

        /**
         * This method sets the temperature property of the BiomeSettings.
         *
         * @param temperature The temperature of the biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder temperature(@Range(from = 0, to = 25) float temperature) {
            this.temperature = temperature;
            return this;
        }

        /**
         * This method sets the downfall property of the BiomeSettings.
         *
         * @param downfall The downfall of the biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder downfall(@Range(from = 0, to = 25) float downfall) {
            this.downfall = downfall;
            return this;
        }

        /**
         * This method sets the temperature modifier property of the BiomeSettings.
         *
         * @param modifier The temperature modifier of the biome.
         * @version 0.0.1
         * @return The Builder object, for chaining method calls.
         */
        @AsOf("0.0.1")
        public @NotNull Builder modifier(@NotNull BiomeTempModifier modifier) {
            this.modifier = modifier;
            return this;
        }

        /**
         * This method creates a new BiomeSettings object with the properties set in the Builder.
         *
         * @version 0.0.1
         * @return a new BiomeSettings object.
         */
        @AsOf("0.0.1")
        public @NotNull BiomeSettings build() {
            return new BiomeSettings(depth, scale, temperature, downfall, modifier);
        }

    }

}
