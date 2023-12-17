package me.outspending.biomesapi;

import lombok.Getter;
import me.outspending.biomesapi.annotations.AsOf;
import net.minecraft.world.level.biome.Biome;

/**
 * This enum represents the temperature modifier for a biome in Minecraft.
 * It includes two values: NONE and FROZEN, which correspond to the TemperatureModifier values in the Biome class in the Minecraft code.
 * Each enum value includes a TemperatureModifier object, which can be retrieved using the getModifier method.
 *
 * @version 0.0.1
 */
@Getter
@AsOf("0.0.1")
public enum BiomeTempModifier {

    /**
     * This value represents no temperature modifier for a biome.
     * It corresponds to the NONE value in the TemperatureModifier enum in the Biome class in the Minecraft code.
     *
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    NONE(Biome.TemperatureModifier.NONE),

    /**
     * This value represents a frozen temperature modifier for a biome.
     * It corresponds to the FROZEN value in the TemperatureModifier enum in the Biome class in the Minecraft code.
     *
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    FROZEN(Biome.TemperatureModifier.FROZEN);

    /**
     * This field stores the TemperatureModifier object that corresponds to the enum value.
     */
    private final Biome.TemperatureModifier modifier;

    /**
     * This constructor creates a new BiomeTempModifier enum value with the provided TemperatureModifier.
     *
     * @param modifier The TemperatureModifier that corresponds to the enum value.
     */
    BiomeTempModifier(Biome.TemperatureModifier modifier) {
        this.modifier = modifier;
    }

}
