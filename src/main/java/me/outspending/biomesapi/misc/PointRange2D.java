package me.outspending.biomesapi.misc;

import me.outspending.biomesapi.annotations.AsOf;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a 2D range of points in a Minecraft world.
 * The range is defined by minimum and maximum X and Z coordinates.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
public record PointRange2D(int minX, int maxX, int minZ, int maxZ) {

    /**
     * Creates a new PointRange2D from two given locations.
     * The minimum and maximum X and Z coordinates are calculated from the given locations.
     *
     * @param from The first location.
     * @param to The second location.
     * @return A new PointRange2D that spans from the minimum to the maximum coordinates of the given locations.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static @NotNull PointRange2D of(@NotNull Location from, @NotNull Location to) {
        return new PointRange2D(
                Math.min(from.getBlockX(), to.getBlockX()),
                Math.max(from.getBlockX(), to.getBlockX()),
                Math.min(from.getBlockZ(), to.getBlockZ()),
                Math.max(from.getBlockZ(), to.getBlockZ())
        );
    }

}