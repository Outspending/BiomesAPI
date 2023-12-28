package me.outspending.biomesapi.misc;

import me.outspending.biomesapi.annotations.AsOf;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a 3D range of points defined by minimum and maximum coordinates in each axis (X, Y, Z).
 * This class is a record, which is an immutable data carrier.
 *
 * @version 0.0.1
 */
@AsOf("0.0.1")
public record PointRange3D(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {

    /**
     * Creates a new PointRange3D object based on two given locations.
     * The minimum and maximum coordinates are calculated for each axis based on the provided locations.
     *
     * @param from The first location.
     * @param to The second location.
     * @return A new PointRange3D object.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static @NotNull PointRange3D of(@NotNull Location from, @NotNull Location to) {
        return new PointRange3D(
                Math.min(from.getBlockX(), to.getBlockX()),
                Math.max(from.getBlockX(), to.getBlockX()),
                Math.min(from.getBlockY(), to.getBlockY()),
                Math.max(from.getBlockY(), to.getBlockY()),
                Math.min(from.getBlockZ(), to.getBlockZ()),
                Math.max(from.getBlockZ(), to.getBlockZ())
        );
    }

}