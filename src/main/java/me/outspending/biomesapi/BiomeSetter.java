package me.outspending.biomesapi;

import lombok.experimental.UtilityClass;
import me.outspending.biomesapi.annotations.AsOf;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This utility class provides methods to set the biome of blocks, chunks, and regions in the game.
 * It uses the @AsOf annotation to indicate the version since the class or its methods have been present or modified.
 * It is a final class, meaning it cannot be subclassed.
 *
 * @version 0.0.1
 */
@UtilityClass
@AsOf("0.0.1")
public final class BiomeSetter {

    private static final UnsafeValues UNSAFE = Bukkit.getUnsafe();


    private static final int MAX_HEIGHT = 320;
    private static final int MIN_HEIGHT = -64;

    /**
     * Returns the RegionAccessor for the given location.
     *
     * @param location the location
     * @return the RegionAccessor for the location
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    private static @NotNull RegionAccessor getRegionAccessor(@NotNull Location location) {
        return location.getWorld();
    }

    /**
     * Sets the biome of a block to a custom biome.
     *
     * @param block the block
     * @param customBiome the custom biome
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void setBlockBiome(@NotNull Block block, @NotNull CustomBiome customBiome) {
        setBlockBiome(block, customBiome, false);
    }

    /**
     * Sets the biome of a block to a custom biome.
     * This method uses the UnsafeValues instance to perform unsafe operations.
     * It gets the location of the block and sets its biome to the custom biome.
     * If the 'updateBiome' flag is set to true, the biome of the block is updated immediately.
     *
     * @param block The block whose biome is to be set.
     * @param customBiome The custom biome to set for the block.
     * @param updateBiome A flag indicating whether to update the biome of the block immediately.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void setBlockBiome(@NotNull Block block, @NotNull CustomBiome customBiome, boolean updateBiome) {
        Location location = block.getLocation();
        RegionAccessor accessor = getRegionAccessor(location);

        UNSAFE.setBiomeKey(accessor, location.getBlockX(), location.getBlockY(), location.getBlockZ(), customBiome.toNamespacedKey());

        if (updateBiome) {
            BiomeUpdater.updateChunk(location.getChunk());
        }
    }

    /**
     * Sets the biome of a chunk to a custom biome.
     *
     * @param chunk the chunk
     * @param customBiome the custom biome
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void setChunkBiome(@NotNull Chunk chunk, @NotNull CustomBiome customBiome) {
        setChunkBiome(chunk, MIN_HEIGHT, MAX_HEIGHT, customBiome);
    }

    /**
     * Sets the biome of a chunk to a custom biome within the default height range.
     * This method is a convenience method that calls the setChunkBiome method with the default minimum and maximum heights.
     * If the 'updateBiome' flag is set to true, the biome of the chunk is updated immediately.
     *
     * @param chunk The chunk whose biome is to be set.
     * @param customBiome The custom biome to set for the chunk.
     * @param updateBiome A flag indicating whether to update the biome of the chunk immediately.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void setChunkBiome(@NotNull Chunk chunk, @NotNull CustomBiome customBiome, boolean updateBiome) {
        setChunkBiome(chunk, MIN_HEIGHT, MAX_HEIGHT, customBiome, updateBiome);
    }

    /**
     * Sets the biome of a chunk to a custom biome within a height range.
     *
     * @param chunk the chunk
     * @param minHeight the minimum height
     * @param maxHeight the maximum height
     * @param customBiome the custom biome
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void setChunkBiome(@NotNull Chunk chunk, int minHeight, int maxHeight, @NotNull CustomBiome customBiome) {
        setChunkBiome(chunk, minHeight, maxHeight, customBiome, false);
    }

   /**
     * Sets the biome of a chunk to a custom biome within a specified height range.
     * This method uses the UnsafeValues instance to perform unsafe operations.
     * It iterates over the blocks in the chunk within the specified height range and sets their biome to the custom biome.
     * If the 'updateBiome' flag is set to true, the biome of the chunk is updated immediately.
     *
     * @param chunk The chunk whose biome is to be set.
     * @param minHeight The minimum height within the chunk for the biome change.
     * @param maxHeight The maximum height within the chunk for the biome change.
     * @param customBiome The custom biome to set for the chunk.
     * @param updateBiome A flag indicating whether to update the biome of the chunk immediately.
     * @version 0.0.1
     */
   @AsOf("0.0.1")
   public static void setChunkBiome(
           @NotNull Chunk chunk,
           int minHeight,
           int maxHeight,
           @NotNull CustomBiome customBiome,
           boolean updateBiome
   ) {
        RegionAccessor accessor = chunk.getWorld();
        NamespacedKey key = customBiome.toNamespacedKey();

        int minX = chunk.getX() << 4;
        int maxX = minX + 16;

        int minZ = chunk.getZ() << 4;
        int maxZ = minZ + 16;

        for (int x = minX; x < maxX; x++) {
           for (int y = minHeight; y < maxHeight; y++) {
               for (int z = minZ; z < maxZ; z++) {
                   // Set the biome of each block to the custom biome
                   UNSAFE.setBiomeKey(accessor, x, y, z, key);
               }
           }
        }

        if (updateBiome) {
           BiomeUpdater.updateChunk(chunk);
        }
   }

    /**
     * Sets the biome of a bounding box to a custom biome.
     *
     * @param world the world
     * @param boundingBox the bounding box
     * @param customBiome the custom biome
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void setBoundingBoxBiome(@NotNull World world, @NotNull BoundingBox boundingBox, @NotNull CustomBiome customBiome) {
        setRegionBiome(world, boundingBox.getMin(), boundingBox.getMax(), customBiome);
    }

    /**
     * Sets the biome of a region to a custom biome.
     *
     * @param from the starting location
     * @param to the ending location
     * @param customBiome the custom biome
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void setRegionBiome(@NotNull Location from, @NotNull Location to, @NotNull CustomBiome customBiome) {
        if (from.getWorld().equals(to.getWorld())) {
            setRegionBiome(from.getWorld(), from.toVector(), to.toVector(), customBiome);
            return;
        }

        throw new IllegalArgumentException("Locations must be in the same world!");
    }

    /**
     * Sets the biome of a region to a custom biome.
     * This method is a convenience method that calls the setRegionBiome method with the 'updateBiome' flag set to false.
     *
     * @param from the starting location
     * @param to the ending location
     * @param customBiome the custom biome
     * @param updateBiome a flag indicating whether to update the biome of the region immediately
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void setRegionBiome(@NotNull Location from, @NotNull Location to, @NotNull CustomBiome customBiome, boolean updateBiome) {
        if (from.getWorld().equals(to.getWorld())) {
            setRegionBiome(from.getWorld(), from.toVector(), to.toVector(), customBiome, updateBiome);
            return;
        }

        throw new IllegalArgumentException("Locations must be in the same world!");
    }

    /**
     * Sets the biome of a region to a custom biome.
     *
     * @param world the world
     * @param from the starting vector
     * @param to the ending vector
     * @param customBiome the custom biome
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void setRegionBiome(@NotNull World world, @NotNull Vector from, @NotNull Vector to, @NotNull CustomBiome customBiome) {
        setRegionBiome(world, from, to, customBiome, false);
    }

    /**
     * Sets the biome of a region to a custom biome.
     * This method uses the UnsafeValues instance to perform unsafe operations.
     * It iterates over the blocks in the region defined by the 'from' and 'to' vectors and sets their biome to the custom biome.
     * If the 'updateBiome' flag is set to true, the biome of the region is updated immediately.
     *
     * @param world The world in which the region is located.
     * @param from The starting vector of the region.
     * @param to The ending vector of the region.
     * @param customBiome The custom biome to set for the region.
     * @param updateBiome A flag indicating whether to update the biome of the region immediately.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void setRegionBiome(
            @NotNull World world,
            @NotNull Vector from,
            @NotNull Vector to,
            @NotNull CustomBiome customBiome,
            boolean updateBiome
    ) {
        RegionAccessor accessor = getRegionAccessor(from.toLocation(world));
        NamespacedKey key = customBiome.toNamespacedKey();

        int minX = Math.min(from.getBlockX(), to.getBlockX());
        int maxX = Math.max(from.getBlockX(), to.getBlockX());

        int minY = Math.min(from.getBlockY(), to.getBlockY());
        int maxY = Math.max(from.getBlockY(), to.getBlockY());

        int minZ = Math.min(from.getBlockZ(), to.getBlockZ());
        int maxZ = Math.max(from.getBlockZ(), to.getBlockZ());

        int minHeight = Math.max(minY, MIN_HEIGHT);
        int maxHeight = Math.min(maxY, MAX_HEIGHT);

        // Iterate over the blocks in the region
        for (int x = minX; x <= maxX; x++) {
            for (int y = minHeight; y <= maxHeight; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    // Set the biome of each block to the custom biome
                    UNSAFE.setBiomeKey(accessor, x, y, z, key);
                }
            }
        }

        if (updateBiome) {
            BiomeUpdater.updateChunks(from.toLocation(world), to.toLocation(world));
        }
    }

}