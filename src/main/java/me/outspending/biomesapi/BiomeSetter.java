package me.outspending.biomesapi;

import lombok.experimental.UtilityClass;
import me.outspending.biomesapi.annotations.AsOf;
import me.outspending.biomesapi.biome.CustomBiome;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.RegionAccessor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
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
public interface BiomeSetter {

    /**
     * Returns the RegionAccessor for the given location.
     *
     * @param location the location
     * @return the RegionAccessor for the location
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    default @NotNull RegionAccessor getRegionAccessor(@NotNull Location location) {
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
    void setBlockBiome(@NotNull Block block, @NotNull CustomBiome customBiome);

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
    void setBlockBiome(@NotNull Block block, @NotNull CustomBiome customBiome, boolean updateBiome);

    /**
     * Sets the biome of a chunk to a custom biome.
     *
     * @param chunk the chunk
     * @param customBiome the custom biome
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    void setChunkBiome(@NotNull Chunk chunk, @NotNull CustomBiome customBiome);

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
    void setChunkBiome(@NotNull Chunk chunk, @NotNull CustomBiome customBiome, boolean updateBiome);

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
    void setChunkBiome(@NotNull Chunk chunk, int minHeight, int maxHeight, @NotNull CustomBiome customBiome);

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
   void setChunkBiome(@NotNull Chunk chunk, int minHeight, int maxHeight, @NotNull CustomBiome customBiome, boolean updateBiome);

    /**
     * Sets the biome of a bounding box to a custom biome.
     *
     * @param world the world
     * @param boundingBox the bounding box
     * @param customBiome the custom biome
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    void setBoundingBoxBiome(@NotNull World world, @NotNull BoundingBox boundingBox, @NotNull CustomBiome customBiome);

    /**
     * Sets the biome of a region to a custom biome.
     *
     * @param from the starting location
     * @param to the ending location
     * @param customBiome the custom biome
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    void setRegionBiome(@NotNull Location from, @NotNull Location to, @NotNull CustomBiome customBiome);

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
    void setRegionBiome(@NotNull Location from, @NotNull Location to, @NotNull CustomBiome customBiome, boolean updateBiome);

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
    void setRegionBiome(@NotNull World world, @NotNull Vector from, @NotNull Vector to, @NotNull CustomBiome customBiome);

    /**
     * Sets the biome of a region to a custom biome.
     * This method is a convenience method that calls the setRegionBiome method with the 'updateBiome' flag set to false.
     *
     * @param world the world
     * @param from the starting vector
     * @param to the ending vector
     * @param customBiome the custom biome
     * @param updateBiome a flag indicating whether to update the biome of the region immediately
     * @version 0.0.1
     */
    @AsOf("0.0.2")
    void setRegionBiome(@NotNull World world, @NotNull Vector from, @NotNull Vector to, @NotNull CustomBiome customBiome, boolean updateBiome);

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
     * @version 0.0.2
     */
    @AsOf("0.0.1")
    void setRegionBiome(@NotNull World world, @NotNull Location from, @NotNull Location to, @NotNull CustomBiome customBiome, boolean updateBiome);

}