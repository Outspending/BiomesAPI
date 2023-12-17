package me.outspending.biomesapi;

import lombok.experimental.UtilityClass;
import me.outspending.biomesapi.annotations.AsOf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        if (from.getWorld().equals(to.getWorld())) {
            throw new IllegalArgumentException("Locations must be in the same world.");
        }

        List<Chunk> chunks = new ArrayList<>();

        int minX = Math.min(from.getBlockX(), to.getBlockX());
        int maxX = Math.max(from.getBlockX(), to.getBlockX());

        int minZ = Math.min(from.getBlockZ(), to.getBlockZ());
        int maxZ = Math.max(from.getBlockZ(), to.getBlockZ());

        World world = from.getWorld();
        for (int x = minX; x <= maxX; x += 16) {
            for (int z = minZ; z <= maxZ; z += 16) {
                chunks.add(world.getChunkAt(x >> 4, z >> 4));
            }
        }

        return chunks;
    }

    /**
     * Returns a list of players within a certain distance of a chunk.
     * This method calculates the players that are located within the specified distance of the chunk.
     *
     * @param chunk The chunk.
     * @param distance The distance.
     * @return A list of players within the specified distance of the chunk.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    private static @NotNull List<Player> getPlayersInDistance(@NotNull Chunk chunk, int distance) {
        List<Player> players = new ArrayList<>();

        World world = chunk.getWorld();
        Location location = chunk.getBlock(0, 0, 0).getLocation();
        for (Player player : world.getPlayers()) {
            if (player.getLocation().distance(location) <= distance) {
                players.add(player);
            }
        }

        return players;
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
     * Updates the biome of a chunk within a certain distance.
     * This method is a convenience method that calls the updateChunks method with a list containing the chunk and the specified distance.
     *
     * @param chunk The chunk to update.
     * @param distance The distance.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void updateChunk(@NotNull Chunk chunk, int distance) {
        updateChunks(List.of(chunk), distance);
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
     * Updates the biomes of the chunks between two locations within a certain distance.
     * This method is a convenience method that calls the updateChunks method with the chunks between the 'from' and 'to' locations and the specified distance.
     *
     * @param from The starting location.
     * @param to The ending location.
     * @param distance The distance.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void updateChunks(@NotNull Location from, @NotNull Location to, int distance) {
        updateChunks(getChunksBetweenLocations(from, to), distance);
    }

    /**
     * Updates the biomes of a list of chunks.
     * This method is a convenience method that calls the updateChunks method with the list of chunks and a default distance of 250.
     *
     * @param chunks The chunks to update.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void updateChunks(@NotNull List<Chunk> chunks) {
        updateChunks(chunks, 250);
    }

    /**
     * Updates the biomes of a list of chunks within a certain distance.
     * This method sends an update packet to all players within the specified distance of each chunk in the list.
     * The update packet contains the new biome data for the chunk.
     *
     * @param chunks The chunks to update.
     * @param distance The distance.
     * @version 0.0.1
     */
    @AsOf("0.0.1")
    public static void updateChunks(@NotNull List<Chunk> chunks, int distance) {
        CompletableFuture.runAsync(() -> {

            for (Chunk chunk : chunks) {
                LevelChunk craftChunk = (LevelChunk) ((CraftChunk) chunk).getHandle(ChunkStatus.BIOMES);
                List<Player> updatePlayers = getPlayersInDistance(chunk, distance);
                ClientboundLevelChunkPacketData packet = new ClientboundLevelChunkPacketData(craftChunk, null);
                for (Player player : updatePlayers) {
                    // Send update packet
                    ((CraftPlayer) player).getHandle().connection.send((Packet<?>) packet);
                }
            }

        });
    }

}
