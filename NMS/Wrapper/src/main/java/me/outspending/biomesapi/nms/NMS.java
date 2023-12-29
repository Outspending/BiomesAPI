package me.outspending.biomesapi.nms;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public interface NMS {

    /**
     * Checks if a given chunk is within the view distance of a player.
     *
     * @param player The player for whom to check the view distance.
     * @param chunk The chunk to check if it's within the player's view distance.
     * @return true if the chunk is within the player's view distance, false otherwise.
     */
    private boolean inChunkViewDistance(@NotNull Player player, @NotNull Chunk chunk) {
        Location playerLocation = player.getLocation();

        int viewDistance = Bukkit.getViewDistance();
        int playerChunkX = playerLocation.getChunk().getX();
        int playerChunkZ = playerLocation.getChunk().getZ();

        int targetChunkX = chunk.getX();
        int targetChunkZ = chunk.getZ();

        int deltaX = Math.abs(playerChunkX - targetChunkX);
        int deltaZ = Math.abs(playerChunkZ - targetChunkZ);

        return deltaX <= viewDistance && deltaZ <= viewDistance;
    }

    /**
     * Gets a list of players who are within the view distance of a given chunk.
     *
     * @param chunk The chunk for which to get the players within its view distance.
     * @return A list of players who are within the view distance of the chunk.
     */
    default List<Player> getPlayersInDistance(@NotNull Chunk chunk) {
        World world = chunk.getWorld();

        return world.getPlayers().stream()
                .filter(player -> inChunkViewDistance(player, chunk))
                .toList();
    }

    /**
     * Updates a list of chunks.
     *
     * @param chunks The chunks to update.
     */
    void updateChunks(@NotNull List<Chunk> chunks);

    /**
     * Locks or unlocks the biome registry.
     *
     * @param isLocked true to lock the biome registry, false to unlock it.
     */
    void biomeRegistryLock(boolean isLocked);

    /**
     * Unlocks the registry with a given supplier.
     *
     * @param supplier The supplier to use to unlock the registry.
     */
    void unlockRegistry(@NotNull Supplier<?> supplier);

    /**
     * Retrieves the biome registry from the Minecraft server.
     *
     * This method gets the server instance from the Bukkit API, accesses the registry of the server,
     * and retrieves the biome registry. If the biome registry cannot be retrieved, it throws a RuntimeException.
     *
     * @return The biome registry from the Minecraft server.
     * @throws RuntimeException if the biome registry cannot be retrieved.
     */
    @NotNull Object getRegistry();

    void updateBiome(@NotNull Location minLoc, @NotNull Location maxLoc, @NotNull NamespacedKey namespacedKey);

}
