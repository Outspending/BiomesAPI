package me.outspending.biomesapi;

import lombok.experimental.UtilityClass;
import me.outspending.biomesapi.annotations.AsOf;
import me.outspending.biomesapi.misc.PointRange2D;
import me.outspending.biomesapi.nms.NMS;
import me.outspending.biomesapi.nms.NMSHandler;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for updating biomes in Minecraft.
 * This class provides methods for updating the biomes of chunks and regions in a Minecraft world.
 * It uses the UnsafeValues instance to perform unsafe operations.
 *
 * @version 0.0.1
 */
@UtilityClass
@AsOf("0.0.1")
public final class BiomeUpdater {

    /**
     * Returns a list of chunks between two locations.
     * This method calculates the chunks that are located between the 'from' and 'to' locations.
     * The locations must be in the same world.
     *
     * @param from The starting location.
     * @param to The ending location.
     * @return A list of chunks between the two locations.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    private static @NotNull List<Chunk> getChunksBetweenLocations(@NotNull Location from, @NotNull Location to) {
        if (!from.getWorld().equals(to.getWorld())) {
            throw new IllegalArgumentException("Locations must be in the same world.");
        }

        List<Chunk> chunks = new ArrayList<>();

        PointRange2D range = PointRange2D.of(from, to);
        World world = from.getWorld();
        for (int x = range.minX(); x <= range.maxX(); x += 16) {
            for (int z = range.minZ(); z <= range.maxZ(); z += 16) {
                chunks.add(world.getChunkAt(x >> 4, z >> 4));
            }
        }

        return chunks;
    }

    /**
     * Updates the biome of a chunk.
     * This method is a convenience method that calls the updateChunks method with a list containing the chunk.
     *
     * @param chunk The chunk to update.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void updateChunk(@NotNull Chunk chunk) {
        updateChunks(List.of(chunk));
    }

    /**
     * Updates the biomes of the chunks between two locations.
     * This method is a convenience method that calls the updateChunks method with the chunks between the 'from' and 'to' locations.
     *
     * @param from The starting location.
     * @param to The ending location.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void updateChunks(@NotNull Location from, @NotNull Location to) {
        updateChunks(getChunksBetweenLocations(from, to));
    }

    /**
     * Updates the biomes of a list of chunks within a certain distance.
     * This method sends an update packet to all players within the specified distance of each chunk in the list.
     * The update packet contains the new biome data for the chunk.
     *
     * @param chunks The chunks to update.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void updateChunks(@NotNull List<Chunk> chunks) {
        Optional<NMS> nms = NMSHandler.getNMS();

        nms.ifPresent(nmsInstance -> nmsInstance.updateChunks(chunks));
    }

}
