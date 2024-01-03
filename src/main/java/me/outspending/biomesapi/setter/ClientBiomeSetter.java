package me.outspending.biomesapi.setter;

import me.outspending.biomesapi.biome.CustomBiome;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class ClientBiomeSetter implements BiomeSetter {

    private final Player client;

    public ClientBiomeSetter(@NotNull Player client) {
        this.client = client;
    }

    public Player getClient() {
        return client;
    }

    @Override
    public void setBlockBiome(@NotNull Block block, @NotNull CustomBiome customBiome) {
        setBlockBiome(block, customBiome, false);
    }

    @Override
    public void setBlockBiome(@NotNull Block block, @NotNull CustomBiome customBiome, boolean updateBiome) {
        // TODO: Method
    }

    @Override
    public void setChunkBiome(@NotNull Chunk chunk, @NotNull CustomBiome customBiome) {
        setChunkBiome(chunk, customBiome, false);
    }

    @Override
    public void setChunkBiome(@NotNull Chunk chunk, @NotNull CustomBiome customBiome, boolean updateBiome) {
        setChunkBiome(chunk, MIN_HEIGHT, MAX_HEIGHT, customBiome, false);
    }

    @Override
    public void setChunkBiome(@NotNull Chunk chunk, int minHeight, int maxHeight, @NotNull CustomBiome customBiome) {
        setChunkBiome(chunk, minHeight, maxHeight, customBiome, false);
    }

    @Override
    public void setChunkBiome(@NotNull Chunk chunk, int minHeight, int maxHeight, @NotNull CustomBiome customBiome, boolean updateBiome) {
        // TODO: Method
    }

    @Override
    public void setBoundingBoxBiome(@NotNull World world, @NotNull BoundingBox boundingBox, @NotNull CustomBiome customBiome) {
        // TODO: Method
    }

    @Override
    public void setRegionBiome(@NotNull Location from, @NotNull Location to, @NotNull CustomBiome customBiome) {
        setRegionBiome(from, to , customBiome, false);
    }

    @Override
    public void setRegionBiome(@NotNull Location from, @NotNull Location to, @NotNull CustomBiome customBiome, boolean updateBiome) {
        // TODO: Method
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

    }
}
