package me.outspending.biomesapi;

import me.outspending.biomesapi.biome.CustomBiome;
import me.outspending.biomesapi.misc.PointRange3D;
import me.outspending.biomesapi.nms.NMSHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class BiomeSetterImpl implements BiomeSetter {

    @SuppressWarnings("deprecation")
    private static final UnsafeValues UNSAFE = Bukkit.getUnsafe();
    private static final BiomeUpdater BIOME_UPDATER = BiomeUpdater.of();

    private static final int MAX_HEIGHT = 320;
    private static final int MIN_HEIGHT = -64;

    @Override
    public void setBlockBiome(@NotNull Block block, @NotNull CustomBiome customBiome) {
        setBlockBiome(block, customBiome, false);
    }

    @Override
    public void setBlockBiome(@NotNull Block block, @NotNull CustomBiome customBiome, boolean updateBiome) {
        Location location = block.getLocation();
        RegionAccessor accessor = getRegionAccessor(location);

        UNSAFE.setBiomeKey(accessor, location.getBlockX(), location.getBlockY(), location.getBlockZ(), customBiome.toNamespacedKey());

        if (updateBiome) {
            BIOME_UPDATER.updateChunk(location.getChunk());
        }
    }

    @Override
    public void setChunkBiome(@NotNull Chunk chunk, @NotNull CustomBiome customBiome) {
        setChunkBiome(chunk, MIN_HEIGHT, MAX_HEIGHT, customBiome);
    }

    @Override
    public void setChunkBiome(@NotNull Chunk chunk, @NotNull CustomBiome customBiome, boolean updateBiome) {
        setChunkBiome(chunk, MIN_HEIGHT, MAX_HEIGHT, customBiome, updateBiome);
    }

    @Override
    public void setChunkBiome(@NotNull Chunk chunk, int minHeight, int maxHeight, @NotNull CustomBiome customBiome) {
        setChunkBiome(chunk, minHeight, maxHeight, customBiome, false);
    }

    @Override
    public void setChunkBiome(@NotNull Chunk chunk, int minHeight, int maxHeight, @NotNull CustomBiome customBiome, boolean updateBiome) {
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
            BIOME_UPDATER.updateChunk(chunk);
        }
    }

    @Override
    public void setBoundingBoxBiome(@NotNull World world, @NotNull BoundingBox boundingBox, @NotNull CustomBiome customBiome) {
        setRegionBiome(world, boundingBox.getMin(), boundingBox.getMax(), customBiome);
    }

    @Override
    public void setRegionBiome(@NotNull Location from, @NotNull Location to, @NotNull CustomBiome customBiome) {
        World world = from.getWorld();
        if (!world.equals(to.getWorld())) {
            throw new IllegalArgumentException("Locations must be in the same world!");
        }

        setRegionBiome(world, from, to, customBiome, false);
    }

    @Override
    public void setRegionBiome(@NotNull Location from, @NotNull Location to, @NotNull CustomBiome customBiome, boolean updateBiome) {
        World world = from.getWorld();
        if (!world.equals(to.getWorld())) {
            throw new IllegalArgumentException("Locations must be in the same world!");
        }

        setRegionBiome(world, from, to, customBiome, updateBiome);
    }

    @Override
    public void setRegionBiome(@NotNull World world, @NotNull Vector from, @NotNull Vector to, @NotNull CustomBiome customBiome) {
        setRegionBiome(world, from, to, customBiome, false);
    }

    @Override
    public void setRegionBiome(@NotNull World world, @NotNull Vector from, @NotNull Vector to, @NotNull CustomBiome customBiome, boolean updateBiome) {
        setRegionBiome(world, from.toLocation(world), to.toLocation(world), customBiome, updateBiome);
    }

    @Override
    public void setRegionBiome(@NotNull World world, @NotNull Location from, @NotNull Location to, @NotNull CustomBiome customBiome, boolean updateBiome) {
        NMSHandler.executeNMS(nms -> {
            PointRange3D range = PointRange3D.of(from, to);

            nms.updateBiome(range.getMinLocation(world), range.getMaxLocation(world), customBiome.toNamespacedKey());
        });

        if (updateBiome) {
            BIOME_UPDATER.updateChunks(from, to);
        }
    }

}
