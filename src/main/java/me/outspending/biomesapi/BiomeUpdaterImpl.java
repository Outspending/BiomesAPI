package me.outspending.biomesapi;

import me.outspending.biomesapi.nms.NMSHandler;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BiomeUpdaterImpl implements BiomeUpdater {

    @Override
    public void updateChunk(Chunk chunk) {
        updateChunks(List.of(chunk));
    }

    @Override
    public void updateChunks(Location from, Location to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Locations cannot be null.");
        } else {
            List<Chunk> updateChunks = getChunksBetweenLocations(from, to);

            updateChunks(updateChunks);
        }
    }

    @Override
    public void updateChunks(@NotNull List<Chunk> chunks) {
        NMSHandler.executeNMS(nms -> nms.updateChunks(chunks));
    }

}
